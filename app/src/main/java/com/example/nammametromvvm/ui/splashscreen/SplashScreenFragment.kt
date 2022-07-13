package com.example.nammametromvvm.ui.splashscreen

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.nammametromvvm.R
import com.example.nammametromvvm.data.repositaries.network.responses.appUpdate.UpdateData
import com.example.nammametromvvm.databinding.BottomSheetDialogLayoutBinding
import com.example.nammametromvvm.databinding.FragmentSplashScreenBinding
import com.example.nammametromvvm.ui.splashscreen.enumReturn.SplashScreenEnum.ConfigEnum
import com.example.nammametromvvm.ui.splashscreen.enumReturn.SplashScreenEnum.ConfigEnum.UP_TO_DATE
import com.example.nammametromvvm.ui.splashscreen.enumReturn.SplashScreenEnum.UpdateEnum.*
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.GeneralException
import com.example.nammametromvvm.utility.logs.LoggerClass
import com.example.nammametromvvm.utility.ui.toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : Fragment() {


    private val viewModel by viewModels<SplashViewModel>()


    @Inject
    lateinit var loggerClass: LoggerClass


    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var updateDialogueBinding: BottomSheetDialogLayoutBinding

    @Inject
    lateinit var configurationsClass: Configurations


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savePhoneNumber()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIfUpdateCheckNeeded()
    }

    private fun checkIfUpdateCheckNeeded() {
        lifecycleScope.launch {
            try {
                if (viewModel.isUpdateCheckNeeded()) {
                    checkForAppUpdate()
                } else {
                    proceedAfterUpdateCheck()
                }
            } catch (e: GeneralException) {
                requireActivity().toast(getString(R.string.something_went_wrong))
                loggerClass.error(e)
            }
        }
    }

    private fun checkForAppUpdate() {
        lifecycleScope.launch {
            loadingInfo(true, getString(R.string.checking_for_update))
            val updateResponse = viewModel.checkForUpdate()
            loadingInfo(false)
            when (updateResponse.upgradeFlag) {
                NO_UPDATE.update -> proceedAfterUpdateCheck()
                OPTIONAL.update -> updateAvailableDialogue(updateResponse)
                MANDATORY.update -> updateAvailableDialogue(updateResponse)
                else -> {
                    if (viewModel.getUpgradeFlag() == MANDATORY.update) {
                        updateAvailableDialogue(updateResponse)
                    } else {
                        proceedAfterUpdateCheck()
                    }
                }
            }
            loadingInfo(false)
        }
    }

    private fun updateAvailableDialogue(updateResponse: UpdateData) {
        showBottomSheetDialog(updateResponse)
    }

    private fun proceedAfterUpdateCheck() {
        checkForConfiguration()
    }

    private fun checkForConfiguration() {
        lifecycleScope.launch {
            loadingInfo(true, getString(R.string.checking_for_configuration))
            val configDownloadReturn = viewModel.configDownload()
            loadingInfo(false)
            when (configDownloadReturn) {
                ConfigEnum.UPDATED.configReturn,
                UP_TO_DATE.configReturn,
                ConfigEnum.ERROR_BUT_PROCEED.configReturn -> {
                    proceedAfterConfigCheck()
                }
                ConfigEnum.ERROR.configReturn,
                ConfigEnum.NO_INTERNET.configReturn -> {
                    configErrorDialog(configDownloadReturn)
                }
            }
        }
    }

    private suspend fun proceedAfterConfigCheck() {
        loadingInfo(true, getString(R.string.checking_for_login_details))
        viewModel.isUserLoggedIn().collect { isUserLoggedIn ->

            loadingInfo(false)
            if (true) {
                navigateToHomeScreen()
            } else {
                if (viewModel.isMandatoryLogin()) {
                    navigateToLoginUserDetailsScreen()
                } else {
                    viewModel.isLoginSkipped().collect { isLoginSkipped ->
                        if (isLoginSkipped) {
                            navigateToHomeScreen()
                        } else {
                            navigateToLoginUserDetailsScreen()
                        }
                    }
                }

            }
        }
    }


    private fun loadingInfo(showAppUpdateInfo: Boolean, message: String = "") {
        if (showAppUpdateInfo) {
            binding.avLoading.smoothToShow()
            binding.tvInfo.text = message
            binding.tvInfo.visibility = View.VISIBLE
        } else {
            binding.avLoading.smoothToHide()
            binding.tvInfo.visibility = View.INVISIBLE
        }
    }

    private fun configErrorDialog(configError: Int) {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        updateDialogueBinding = BottomSheetDialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(updateDialogueBinding.root)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
        updateDialogueBinding.negativeButton.text =
            getString(R.string.close_app)
        updateDialogueBinding.positiveButton.text =
            getString(R.string.retry)
        when (configError) {
            ConfigEnum.ERROR.configReturn -> {
                updateDialogueBinding.tvHeader.text = getString(R.string.something_went_wrong)
                updateDialogueBinding.tvInfo.text = getString(R.string.something_went_wrong_info)
            }
            ConfigEnum.NO_INTERNET.configReturn -> {
                updateDialogueBinding.tvHeader.text = getString(R.string.no_internet)
                updateDialogueBinding.tvInfo.text = getString(R.string.no_internet_info)
            }
        }
        updateDialogueBinding.negativeButton.setOnClickListener {
            bottomSheetDialog.cancel()
            requireActivity().onBackPressed()
        }
        updateDialogueBinding.positiveButton.setOnClickListener {
            bottomSheetDialog.cancel()
            checkForConfiguration()
        }
    }

    private fun showBottomSheetDialog(updateResponse: UpdateData) {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        updateDialogueBinding = BottomSheetDialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(updateDialogueBinding.root)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
        when (updateResponse.upgradeFlag) {
            MANDATORY.update -> updateDialogueBinding.negativeButton.text =
                getString(R.string.close_app)
            ERROR.update -> {
                updateDialogueBinding.positiveButton.text =
                    getString(R.string.close_app)
                updateDialogueBinding.negativeButton.text =
                    getString(R.string.close_app)
                updateDialogueBinding.positiveButton.text =
                    getString(R.string.retry)
                updateDialogueBinding.tvHeader.text = getString(R.string.something_went_wrong)
                updateDialogueBinding.tvInfo.text = getString(R.string.something_went_wrong_info)
            }
        }
        updateDialogueBinding.negativeButton.setOnClickListener {
            bottomSheetDialog.cancel()
            when (updateResponse.upgradeFlag) {
                MANDATORY.update, ERROR.update -> {
                    requireActivity().onBackPressed()
                }
                OPTIONAL.update -> {
                    proceedAfterUpdateCheck()
                }
            }
        }
        updateDialogueBinding.positiveButton.setOnClickListener {
            bottomSheetDialog.cancel()
            when (updateResponse.upgradeFlag) {
                MANDATORY.update, OPTIONAL.update -> {
                    gotoPlayStore()
                }
                ERROR.update -> {
                    checkIfUpdateCheckNeeded()
                }
            }
        }
    }

    private fun gotoPlayStore() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.aum.nammametro")
                )
            )
        } catch (activityNotFoundException: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.aum.nammametro")
                )
            )
        }
    }


    private fun navigateToHomeScreen() {
        navigateTo(
            SplashScreenFragmentDirections.actionSplashScreenActivityToHomeFragment(
            )
        )
    }

    private fun navigateToLoginUserDetailsScreen() {
        navigateTo(
            SplashScreenFragmentDirections.actionSplashScreenActivityToLoginUserDetailsFragment(
                getString(R.string.navigated_from_splash_screen)
            )
        )
    }

    private fun navigateTo(navDirections: NavDirections) {
        findNavController().navigate(
            navDirections
        )
    }

    fun savePhoneNumber() {
        val phoneNumber = "9741028810"
        val credential: Credential = Credential.Builder(phoneNumber)
            .setAccountType("https://com.example.nammametromvvm")
            .build()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        Auth.CredentialsApi.save(mGoogleSignInClient.asGoogleApiClient(), credential)
            .setResultCallback { status ->
                Log.d("test1515", "savePhoneNumber: " + status.statusMessage)
            }
    }


}
package com.example.nammametromvvm.ui.splashscreen

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mymvvmsample.data.UpdateData
import com.example.nammametromvvm.R
import com.example.nammametromvvm.data.repositaries.entites.User
import com.example.nammametromvvm.databinding.ActivitySplashScreenBinding
import com.example.nammametromvvm.databinding.BottomSheetDialogLayoutBinding
import com.example.nammametromvvm.ui.homescreen.HomeActivity
import com.example.nammametromvvm.ui.login.ui.login.LoginActivity
import com.example.nammametromvvm.ui.splashscreen.enumReturn.SplashScreenEnum
import com.example.nammametromvvm.ui.splashscreen.enumReturn.SplashScreenEnum.UpdateEnum
import com.example.nammametromvvm.utility.GeneralException
import com.example.nammametromvvm.utility.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private lateinit var viewModel: SplashViewModel
    private val factory: SplashScreenViewModelFactory by instance()
    private val loggerClass: com.example.nammametromvvm.utility.logs.LoggerClass by instance()

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var updateDialogueBinding: BottomSheetDialogLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this, factory).get(
            SplashViewModel::class.java
        )
        viewModel.setUpLogs()
        checkIfUpdateCheckNeeded()

        saveUser()
    }

    private fun saveUser() {
        lifecycleScope.launch {
            val user = User(1, "pavan@gmail.com", "Pavan", "email_verified", "test", "test")
            viewModel.saveLoggedInUser(user)
        }
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
                toast(getString(R.string.something_went_wrong))
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
                UpdateEnum.NO_UPDATE.update -> proceedAfterUpdateCheck()
                UpdateEnum.OPTIONAL.update -> updateAvailableDialogue(updateResponse)
                UpdateEnum.MANDATORY.update -> updateAvailableDialogue(updateResponse)
                else -> {
                    if (viewModel.getUpgradeFlag() == UpdateEnum.MANDATORY.update) {
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
                SplashScreenEnum.ConfigEnum.UPDATED.configReturn,
                SplashScreenEnum.ConfigEnum.UP_TO_DATE.configReturn,
                SplashScreenEnum.ConfigEnum.ERROR_BUT_PROCEED.configReturn ->
                    proceedAfterConfigCheck()
                SplashScreenEnum.ConfigEnum.ERROR.configReturn,
                SplashScreenEnum.ConfigEnum.NO_INTERNET.configReturn ->
                    configErrorDialog(configDownloadReturn)

            }
        }

    }

    private suspend fun proceedAfterConfigCheck() {
        viewModel.userLoggedOut()
        loadingInfo(true, getString(R.string.checking_for_login_details))
        viewModel.isUserLoggedIn().collect { isUserLoggedIn ->
            loadingInfo(false)
            if (isUserLoggedIn) {
                navigateToHomeScreen()
            } else {
                navigateToLoginScreen()
            }
        }
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        startActivity(intent)
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        startActivity(intent)
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
        val bottomSheetDialog = BottomSheetDialog(this)
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
            SplashScreenEnum.ConfigEnum.ERROR.configReturn -> {
                updateDialogueBinding.tvHeader.text = getString(R.string.something_went_wrong)
                updateDialogueBinding.tvInfo.text = getString(R.string.something_went_wrong_info)
            }
            SplashScreenEnum.ConfigEnum.NO_INTERNET.configReturn -> {
                updateDialogueBinding.tvHeader.text = getString(R.string.no_internet)
                updateDialogueBinding.tvInfo.text = getString(R.string.no_internet_info)
            }
        }
        updateDialogueBinding.negativeButton.setOnClickListener {
            bottomSheetDialog.cancel()
            onBackPressed()
        }
        updateDialogueBinding.positiveButton.setOnClickListener {
            bottomSheetDialog.cancel()
            checkForConfiguration()
        }
    }

    private fun showBottomSheetDialog(updateResponse: UpdateData) {
        val bottomSheetDialog = BottomSheetDialog(this)
        updateDialogueBinding = BottomSheetDialogLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(updateDialogueBinding.root)
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
        when (updateResponse.upgradeFlag) {
            UpdateEnum.MANDATORY.update -> updateDialogueBinding.negativeButton.text =
                getString(R.string.close_app)
            UpdateEnum.ERROR.update -> {
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
                UpdateEnum.MANDATORY.update, UpdateEnum.ERROR.update -> {
                    onBackPressed()
                }
                UpdateEnum.OPTIONAL.update -> {
                    proceedAfterUpdateCheck()
                }
            }
        }
        updateDialogueBinding.positiveButton.setOnClickListener {
            bottomSheetDialog.cancel()
            when (updateResponse.upgradeFlag) {
                UpdateEnum.MANDATORY.update, UpdateEnum.OPTIONAL.update -> {
                    gotoPlayStore()
                }
                UpdateEnum.ERROR.update -> {
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
        } catch (anfe: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.aum.nammametro")
                )
            )
        }
    }
}
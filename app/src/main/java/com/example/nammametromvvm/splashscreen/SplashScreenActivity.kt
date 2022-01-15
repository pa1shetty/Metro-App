package com.example.nammametromvvm.splashscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mymvvmsample.data.UpdateData
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.ActivitySplashScreenBinding
import com.example.nammametromvvm.databinding.BottomSheetDialogLayoutBinding
import com.example.nammametromvvm.splashscreen.enumReturn.UpdateEnum
import com.example.nammametromvvm.utility.GeneralException
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import kotlin.system.exitProcess


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

    }

    private fun checkIfUpdateCheckNeeded() {
        lifecycleScope.launch {
            try {
                if (viewModel.isUpdateCheckNeeded()) {
                    checkForAppUpdate()
                    Log.d("test156", "Check For update")
                } else {
                    Log.d("test156", "No Need to Check For update")
                }
            } catch (e: GeneralException) {
                loggerClass.error(e)
                Log.d("test156", "GeneralException $e")
            }


        }
    }

    private fun checkForAppUpdate() {
        lifecycleScope.launch {
            Log.d("test151", "onCreate: ")
            appUpdateInfo(true)
            val updateResponse = viewModel.checkForUpdate()
            appUpdateInfo(false)
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
            appUpdateInfo(false)
        }
    }

    private fun updateAvailableDialogue(updateResponse: UpdateData) {
        showBottomSheetDialog(updateResponse)
    }

    private fun proceedAfterUpdateCheck() {
        Log.d("test162", "proceedAfterUpdateCheck")
    }

    private fun appUpdateInfo(showAppUpdateInfo: Boolean) {
        if (showAppUpdateInfo) {
            binding.avLoading.smoothToShow()
            binding.tvInfo.text = getString(R.string.checking_for_update)
        } else {
            binding.avLoading.smoothToHide()
            binding.tvInfo.visibility = View.INVISIBLE
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
            bottomSheetDialog.hide()
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
            bottomSheetDialog.hide()
            when (updateResponse.upgradeFlag) {
                UpdateEnum.MANDATORY.update, UpdateEnum.OPTIONAL.update -> {
                    viewModel.gotoPlayStore()
                }
                UpdateEnum.ERROR.update -> {
                    checkIfUpdateCheckNeeded()
                }
            }
        }
    }

}
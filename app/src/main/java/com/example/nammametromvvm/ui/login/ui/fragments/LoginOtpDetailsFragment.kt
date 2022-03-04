package com.example.nammametromvvm.ui.login.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.FragmentLoginOtpDetailsBinding
import com.example.nammametromvvm.ui.login.ui.fragments.LoginOtpDetailsFragmentDirections.Companion.actionLoginOtpDetailsFragmentToCardTopUpFragment
import com.example.nammametromvvm.ui.login.ui.fragments.LoginOtpDetailsFragmentDirections.Companion.actionLoginOtpDetailsFragmentToHomeFragment
import com.example.nammametromvvm.ui.login.ui.fragments.LoginOtpDetailsFragmentDirections.Companion.actionLoginOtpDetailsFragmentToProfileFragment
import com.example.nammametromvvm.ui.login.ui.fragments.LoginOtpDetailsFragmentDirections.Companion.actionLoginOtpDetailsFragmentToQrTicketsFragment
import com.example.nammametromvvm.ui.login.viewModel.LoginViewModel
import com.example.nammametromvvm.ui.login.viewModel.LoginViewModelFactory
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.GenericMethods
import com.example.nammametromvvm.utility.StatusEnum
import com.example.nammametromvvm.utility.broadcasts.SmsBroadcastReceiver
import com.example.nammametromvvm.utility.logs.CustomButton
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LoginOtpDetailsFragment : Fragment() {
    private val safeArgs: LoginOtpDetailsFragmentArgs by navArgs()

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentLoginOtpDetailsBinding

    @Inject
    lateinit var applicationContext: Context

    @Inject
    lateinit var genericMethods: GenericMethods
    private lateinit var customButton: CustomButton

    @Inject
    lateinit var configurationsClass: Configurations
    private lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

    @Inject
    lateinit var factory: LoginViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginOtpDetailsBinding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        startSmsUserConsent()
        registerToSmsBroadcastReceiver()
    }

    private fun startSmsUserConsent() {
        SmsRetriever.getClient(requireActivity()).also {
            it.startSmsUserConsent(null)
                .addOnSuccessListener {
                    Log.d(TAG, "LISTENING_SUCCESS")
                }
                .addOnFailureListener {
                    Log.d(TAG, "LISTENING_FAILURE")
                }
        }
    }

    private fun registerToSmsBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver().also {
            it.smsBroadcastReceiverListener =
                object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                    override fun onSuccess(intent: Intent?) {
                        if (intent != null) {
                            smsResultLauncher.launch(intent)
                        }
                    }

                    override fun onFailure() {
                    }
                }
        }


        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireActivity().registerReceiver(smsBroadcastReceiver, intentFilter)

    }

    var smsResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val message = data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            message?.let {
                requireActivity().unregisterReceiver(smsBroadcastReceiver)
                binding.etOtp.setText(loginViewModel.getOTPFromMessage(message))
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpConfiguration()
        setResendTimer()
        setProceedButton()
        setUpClick()
        setUpObserver()
    }

    private fun setUpConfiguration() {
        binding.etOtp.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(configurationsClass.getMaxOTPLength()))
        binding.tvMobileNumber.text = safeArgs.phoneNumber
    }

    private fun setUpObserver() {
        loginViewModel.otpFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer
            if (loginState.isDataValid) {
                customButton.enableLoginButton()
            } else {
                customButton.disableLoginButton()
            }
        })
    }

    private fun setProceedButton() {
        customButton = CustomButton(
            binding.cvProceed,
            binding.ivLogin,
            binding.pbLogin,
            requireActivity()
        )
        customButton.disableLoginButton()
    }

    private fun proceedAfterLoginClick() {
        genericMethods.hideKeypad(requireActivity())
        customButton.loadingLoginButton()
        lifecycleScope.launch {
            val loginResult = loginViewModel.verifyOtp(binding.etOtp.text.toString())
            if (loginResult.success) {
                navigateToHomeScreen()
            } else {
                genericMethods.showSnackBar(binding.root, loginResult.error, requireActivity())
                when (loginResult.error) {
                    StatusEnum.OTP_MISMATCH.statusReturn -> {
                        binding.etOtp.setText("")
                    }
                    StatusEnum.OTP_TIME_OUT.statusReturn -> {
                        binding.etOtp.setText("")
                    }
                }
            }
            customButton.stopLoadingLoginButton()
        }
    }

    private fun navigateToHomeScreen() {
        when (safeArgs.navigatedFrom) {
            getString(R.string.navigated_from_homescreen_profile) -> navigateTo(
                actionLoginOtpDetailsFragmentToProfileFragment()
            )
            getString(R.string.navigated_from_homescreen_top_up) -> navigateTo(
                actionLoginOtpDetailsFragmentToCardTopUpFragment()
            )
            getString(R.string.navigated_from_homescreen_qr_tickets) -> navigateTo(
                actionLoginOtpDetailsFragmentToQrTicketsFragment()
            )
            else -> navigateTo(
                actionLoginOtpDetailsFragmentToHomeFragment()
            )
        }

    }

    private fun navigateTo(navDirections: NavDirections) {
        findNavController().navigate(navDirections)
    }

    private fun setUpClick() {
        binding.etOtp.requestFocusFromTouch()
        genericMethods.showKeyPad(
            requireActivity(),
            binding.etOtp
        )
        binding.tvResend.setOnClickListener { resendOtp() }
        binding.tvEdit.setOnClickListener { requireActivity().onBackPressed() }
        binding.tvHelp.setOnClickListener {
            navigateTo(
                LoginOtpDetailsFragmentDirections.actionLoginOtpDetailsFragmentToLoginHelpFragment()
            )
        }
        binding.etOtp.apply {
            afterTextChanged {
                loginViewModel.otpDataChanged(
                    binding.etOtp.text.toString()
                )
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        if (binding.cvProceed.isEnabled) {
                            proceedAfterLoginClick()
                            return@setOnEditorActionListener false
                        } else Toast.makeText(
                            activity,
                            getString(R.string.enter_valid_otp),
                            Toast.LENGTH_SHORT
                        ).show()
                }
                true
            }
        }
        binding.cvProceed.setOnClickListener { proceedAfterLoginClick() }
    }


    private fun setResendTimer() {
        loginViewModel.startResendTimer()
        loginViewModel.mutableLiveDataResendTime.observe(
            viewLifecycleOwner
        ) {
            if (it == 0) {
                binding.tvResend.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.bmrcl_color
                    )
                )
                binding.tvResend.text = getString(R.string.resend)
                binding.tvResend.isClickable = true
                binding.tvResend.isFocusable = true
            } else {
                binding.tvResend.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.textColor
                    )
                )
                binding.tvResend.isClickable = false
                binding.tvResend.isFocusable = false
                binding.tvResend.text = getString(R.string.resend_in, it.toString())
            }

        }
    }

    private fun resendOtp() {
        requireActivity().unregisterReceiver(smsBroadcastReceiver)
        registerToSmsBroadcastReceiver()
        loginViewModel.stopResendTimer()
        loginViewModel.startResendTimer()
    }

    override fun onDetach() {
        loginViewModel.stopResendTimer()
        super.onDetach()
    }

    companion object {
        const val TAG = "SMS_USER_CONSENT"
    }

    override fun onDestroy() {
        super.onDestroy()
        // requireActivity().unregisterReceiver(smsBroadcastReceiver)
    }
}
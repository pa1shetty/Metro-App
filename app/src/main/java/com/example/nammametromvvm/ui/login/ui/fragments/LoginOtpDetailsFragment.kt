package com.example.nammametromvvm.ui.login.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.FragmentLoginOtpDetailsBinding
import com.example.nammametromvvm.ui.login.viewModel.LoginViewModel
import com.example.nammametromvvm.ui.login.viewModel.LoginViewModelFactory
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.GenericMethods
import com.example.nammametromvvm.utility.StatusEnum
import com.example.nammametromvvm.utility.logs.CustomButton
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
        findNavController().navigate(
            LoginOtpDetailsFragmentDirections.actionLoginOtpDetailsFragmentToHomeFragment()
        )
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
            findNavController().navigate(
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
        loginViewModel.stopResendTimer()
        loginViewModel.startResendTimer()
    }

    override fun onDetach() {
        loginViewModel.stopResendTimer()
        super.onDetach()
    }


}
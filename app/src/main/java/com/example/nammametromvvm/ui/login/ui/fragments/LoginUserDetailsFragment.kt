package com.example.nammametromvvm.ui.login.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.FragmentLoginUserDetailsBinding
import com.example.nammametromvvm.ui.login.viewModel.LoginViewModel
import com.example.nammametromvvm.ui.login.viewModel.LoginViewModelFactory
import com.example.nammametromvvm.utility.GenericMethods
import com.example.nammametromvvm.utility.StatusEnum
import com.example.nammametromvvm.utility.ui.CustomButton
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LoginUserDetailsFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentLoginUserDetailsBinding
    private val safeArgs: LoginUserDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var genericMethods: GenericMethods

    @Inject
    lateinit var factory: LoginViewModelFactory

    private lateinit var customButton: CustomButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginUserDetailsBinding.inflate(layoutInflater)
        return (binding.root)
    }


    private fun requestPhoneNumberHint() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            //.setEmailAddressIdentifierSupported(true)
            .build()
        val intent = Credentials.getClient(requireActivity()).getHintPickerIntent(hintRequest)
        val intentSenderRequest = IntentSenderRequest.Builder(intent.intentSender)
        phoneNumberHintLauncher.launch(intentSenderRequest.build())
    }

    @SuppressLint("SetTextI18n")
    var phoneNumberHintLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result: ActivityResult? ->
        if (result != null && result.data != null) {
            val data = result.data
            val credential: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
            if (credential != null) {
                // var phoneNum: String = credential.id
                var phoneNum = "+919741028810"
                if (phoneNum.contains("+91")) phoneNum = phoneNum.replace("+91", "")
                binding.etPhoneNumber.setText(phoneNum)
                binding.etPhoneNumber.setSelection(binding.etPhoneNumber.length())
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        requestPhoneNumberHint()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpConfiguration()
        setUpProceedButton()
        setUpClicks()
        setUpObserver()
    }

    private fun setUpConfiguration() {
        binding.etPhoneNumber.filters =
            arrayOf<InputFilter>(LengthFilter(loginViewModel.getMaxPhoneNumberDigit()))
        if (!loginViewModel.isMandatoryLogin()) {
            binding.tvSkip.visibility = View.VISIBLE
        }
    }

    private fun setUpObserver() {
        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer
            if (loginState.isDataValid) {
                customButton.enable()
            } else {
                customButton.disable()
            }
        })

        loginViewModel.otpRequestResult.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { otpRequestResponse ->
                it.getContentIfNotHandled()
                when (otpRequestResponse) {
                    StatusEnum.SUCCESS.statusReturn -> {
                        navigateToOtpScreen()
                    }
                    else -> {
                        genericMethods.showSnackBar(
                            binding.root,
                            otpRequestResponse,
                            requireActivity()
                        )
                    }
                }
                customButton.stopLoading()
                genericMethods.hideKeypad(requireActivity())
            }
        }
    }

    private fun setUpProceedButton() {
        customButton = CustomButton(
            binding.cvProceed,
            binding.pbLogin,
            requireActivity(),
            binding.ivLogin,
        )
        customButton.disable()
    }


    private fun setUpClicks() {
        binding.etPhoneNumber.requestFocusFromTouch()
        genericMethods.showKeyPad(
            requireActivity(),
            binding.etPhoneNumber
        )
        binding.tvHelp.setOnClickListener {
            navigateTo(LoginUserDetailsFragmentDirections.actionLoginUserDetailsFragmentToLoginHelpFragment())
        }
        binding.tvTNCClick.setOnClickListener {
            navigateTo(LoginUserDetailsFragmentDirections.actionLoginUserDetailsFragmentToTermAndConditionFragment())
        }
        binding.etPhoneNumber.apply {
            afterTextChanged {
                loginViewModel.phoneNumberDataChanged(
                    binding.etPhoneNumber.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        if (binding.cvProceed.isEnabled) {
                            proceedAfterLoginClick()
                            return@setOnEditorActionListener false
                        } else {
                            Toast.makeText(
                                activity,
                                context.getString(R.string.invalid_mobile_number),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                true
            }

            binding.cvProceed.setOnClickListener {
                proceedAfterLoginClick()
            }
        }
        binding.tvSkip.setOnClickListener { navigateToHomeScreen() }
    }


    private fun navigateToHomeScreen() {
        lifecycleScope.launch {
            loginViewModel.setLogInSkipped()
            if (safeArgs.navigatedFrom == getString(R.string.navigated_from_splash_screen)) {
                navigateTo(
                    LoginUserDetailsFragmentDirections.actionLoginUserDetailsFragmentToHomeFragment()
                )
            } else {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun proceedAfterLoginClick() {
        genericMethods.hideKeypad(requireActivity())
        customButton.startLoading()
        requestForOtp(binding.etPhoneNumber.text.toString())
    }

    private fun requestForOtp(phoneNumber: String) {
        loginViewModel.requestForOtp(phoneNumber)
    }

    private fun navigateToOtpScreen() {
        navigateTo(
            LoginUserDetailsFragmentDirections.actionLoginUserDetailsFragmentToLoginOtpDetailsFragment(
                binding.etPhoneNumber.text.toString(), safeArgs.navigatedFrom
            )
        )
    }

    private fun navigateTo(navDirections: NavDirections) {
        findNavController().navigate(
            navDirections
        )
    }
}


fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })


}
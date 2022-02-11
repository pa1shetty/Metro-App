package com.example.nammametromvvm.ui.login.ui.fragments

import android.content.Intent
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.FragmentLoginUserDetailsBinding
import com.example.nammametromvvm.ui.homescreen.activity.HomeActivity
import com.example.nammametromvvm.ui.login.ui.fragments.LoginUserDetailsFragmentDirections.Companion.actionLoginUserDetailsFragmentToLoginHelpFragment
import com.example.nammametromvvm.ui.login.ui.fragments.LoginUserDetailsFragmentDirections.Companion.actionLoginUserDetailsFragmentToLoginOtpDetails
import com.example.nammametromvvm.ui.login.ui.fragments.LoginUserDetailsFragmentDirections.Companion.actionLoginUserDetailsFragmentToTermAndConditionFragment
import com.example.nammametromvvm.ui.login.viewModel.LoginViewModel
import com.example.nammametromvvm.ui.login.viewModel.LoginViewModelFactory
import com.example.nammametromvvm.utility.GenericMethods
import com.example.nammametromvvm.utility.StatusEnum.SUCCESS
import com.example.nammametromvvm.utility.logs.CustomButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login_user_details.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LoginUserDetailsFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: FragmentLoginUserDetailsBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
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
                customButton.enableLoginButton()
            } else {
                customButton.disableLoginButton()
            }
        })


    }

    private fun setUpProceedButton() {
        customButton = CustomButton(
            binding.cvProceed,
            binding.ivLogin,
            binding.pbLogin,
            requireActivity()
        )
        customButton.disableLoginButton()
    }


    private fun setUpClicks() {
        binding.etPhoneNumber.requestFocusFromTouch()
        genericMethods.showKeyPad(
            requireActivity(),
            binding.etPhoneNumber
        )
        binding.tvHelp.setOnClickListener {
            navigateTo(actionLoginUserDetailsFragmentToLoginHelpFragment())
        }
        binding.tvTNCClick.setOnClickListener {
            navigateTo(actionLoginUserDetailsFragmentToTermAndConditionFragment())
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
            val intent = Intent(requireContext(), HomeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            startActivity(intent)
        }
    }

    private fun proceedAfterLoginClick() {
        genericMethods.hideKeypad(requireActivity())
        customButton.loadingLoginButton()
        requestForOtp(etPhoneNumber.text.toString())
    }

    private fun requestForOtp(phoneNumber: String) {
        lifecycleScope.launch {
            when (val requestOrpResponse = loginViewModel.requestForOtp(phoneNumber)) {
                SUCCESS.statusReturn -> {
                    navigateToOtpScreen()
                }
                else -> {
                    genericMethods.showSnackBar(binding.root, requestOrpResponse, requireActivity())
                }
            }
            customButton.stopLoadingLoginButton()
            genericMethods.hideKeypad(requireActivity())

        }
    }

    private fun navigateToOtpScreen() {
        navigateTo(
            actionLoginUserDetailsFragmentToLoginOtpDetails(
                binding.etPhoneNumber.text.toString()
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
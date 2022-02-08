package com.example.nammametromvvm.ui.login.ui.login.UserDetails

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nammametromvvm.R
import com.example.nammametromvvm.databinding.FragmentLoginUserDetailsBinding
import com.example.nammametromvvm.ui.login.ui.login.LoggedInUserView
import com.example.nammametromvvm.ui.login.ui.login.LoginViewModel
import com.example.nammametromvvm.ui.login.ui.login.LoginViewModelFactory
import com.example.nammametromvvm.ui.login.ui.login.enumClass.LoginScreenEnum.ButtonStatusEnum
import com.example.nammametromvvm.utility.GenericMethods
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginUserDetailsBinding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val password = binding.phoneNumber
        val login = binding.login
        disableLoginButton()
        setUpClicks()

        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer
            if (loginState.passwordError != null) {
                disableLoginButton()
            } else {
                enableLoginButton()
            }
        })

        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                loginViewModel.loginResult.value?.error
            }
        })



        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        if (login.isEnabled) {
                            proceedAfterLoginClick()
                            return@setOnEditorActionListener false
                        } else {
                            Toast.makeText(
                                activity,
                                "Please Enter valid mobile number",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                true
            }

            login.setOnClickListener {
                proceedAfterLoginClick()
            }
        }
    }

    private fun enableLoginButton() {
        loginButtonStatus(ButtonStatusEnum.ENABLE.status)
    }

    private fun disableLoginButton() {
        loginButtonStatus(ButtonStatusEnum.DISABLE.status)
    }

    private fun loadingLoginButton() {
        loginButtonStatus(ButtonStatusEnum.LOADING.status)

    }

    private fun stopLoadingLoginButton() {
        loginButtonStatus(ButtonStatusEnum.NOT_LOADING.status)
    }

    private fun loginButtonStatus(status: Int) {
        when (status) {
            ButtonStatusEnum.ENABLE.status -> {
                binding.login.isEnabled = true
                binding.ivLogin.setColorFilter(
                    ContextCompat.getColor(
                        requireContext().applicationContext,
                        R.color.bmrcl_color
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            ButtonStatusEnum.DISABLE.status -> {
                binding.login.isEnabled = false
                binding.ivLogin.setColorFilter(
                    ContextCompat.getColor(
                        requireContext().applicationContext,
                        R.color.lightGray
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            ButtonStatusEnum.LOADING.status -> {
                binding.login.isEnabled = false
                binding.pbLogin.visibility = View.VISIBLE
                binding.ivLogin.visibility = View.GONE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            }
            else -> {
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                binding.login.isEnabled = true
                binding.pbLogin.visibility = View.GONE
                binding.ivLogin.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpClicks() {

        binding.phoneNumber.requestFocusFromTouch()
        genericMethods.showKeyPad(
            requireActivity(),
            binding.phoneNumber
        )
        binding.tvHelp.setOnClickListener { findNavController().navigate(R.id.action_loginUserDetailsFragment_to_loginHelpFragment) }
        binding.tvTNCClick.setOnClickListener { findNavController().navigate(R.id.action_loginUserDetailsFragment_to_termAndConditionFragment) }

    }

    private fun proceedAfterLoginClick() {
        genericMethods.hideKeypad(requireActivity())
        loadingLoginButton()
        registerUser(phoneNumber.text.toString())
    }

    private fun registerUser(phoneNumber: String) {
        lifecycleScope.launch {
            if (loginViewModel.registerUser(phoneNumber)) {
                stopLoadingLoginButton()
                genericMethods.hideKeypad(requireActivity())
                findNavController().navigate(R.id.action_loginUserDetailsFragment_to_loginOtpDetails)

            }
        }
    }


    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            requireActivity().application,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()

    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(
            requireActivity().application, errorString, Toast.LENGTH_SHORT
        ).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })


}
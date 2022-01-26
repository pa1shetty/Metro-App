package com.example.nammametromvvm.ui.login.ui.login

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymvvmsample.data.repositaries.DataBaseRepository
import com.example.mymvvmsample.data.repositaries.NetworkRepository
import com.example.nammametromvvm.R
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.ui.login.data.LoginDataSource
import com.example.nammametromvvm.ui.login.data.LoginRepository
import com.example.nammametromvvm.ui.login.data.Result
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import com.example.nammametromvvm.utility.logs.Logs


class LoginViewModel(
    private val application: Application,
    private val logs: Logs,
    private val networkRepository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dateMethods: DateMethods,
    private val loggerClass: LoggerClass,
    private val dataBaseRepository: DataBaseRepository,
    private val loginRepository: LoginRepository,
    private val dataSource: LoginDataSource
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(password: String) {
        if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.PHONE.matcher(username).matches()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length == 10
    }

    suspend fun registerUser(phoneNumber: String): Boolean {
        dataStoreRepository.saveCToken(networkRepository.registerUser(phoneNumber).data.cToken)
        Log.d("test503", "registerUser: "+dataStoreRepository.getCToken())
        return true
    }

}
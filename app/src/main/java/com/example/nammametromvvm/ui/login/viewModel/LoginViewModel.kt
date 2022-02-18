package com.example.nammametromvvm.ui.login.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammametromvvm.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.ui.login.data.LoginFormState
import com.example.nammametromvvm.ui.login.data.LoginResult
import com.example.nammametromvvm.utility.*
import com.example.nammametromvvm.utility.StatusEnum.*
import com.example.nammametromvvm.utility.logs.LoggerClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class LoginViewModel(
    private val networkRepository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val configurations: Configurations,
    private val loggerClass: LoggerClass,
    private val userRegistration: UserRegistration
) : ViewModel() {

    private val _phoneNumberForm = MutableLiveData<LoginFormState>()
    private val _otpForm = MutableLiveData<LoginFormState>()
    private val _otpRequestResult = MutableLiveData<Event<Int>>()

    val loginFormState: LiveData<LoginFormState> = _phoneNumberForm
    val otpFormState: LiveData<LoginFormState> = _otpForm

    private var _countDownTimerOtpResend: CountDownTimer? = null
    private var _mutableLiveDataResendTime = MutableLiveData(configurations.getMaxOTPResendWait())
    private var _isTimerRunning = false

    val mutableLiveDataResendTime: LiveData<Int>
        get() = _mutableLiveDataResendTime


    val otpRequestResult: LiveData<Event<Int>>
        get() = _otpRequestResult

    fun phoneNumberDataChanged(mobileNumber: String) {
        if (!isPhoneNumberValid(mobileNumber)) {
            _phoneNumberForm.value = LoginFormState(isDataValid = false)
        } else {
            _phoneNumberForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder mobile number validation check
    private fun isPhoneNumberValid(mobileNumber: String): Boolean {
        return mobileNumber.length == getMaxPhoneNumberDigit()
    }

    fun otpDataChanged(otp: String) {
        if (!isOtpValid(otp)) {
            _otpForm.value = LoginFormState(isDataValid = false)
        } else {
            _otpForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder mobile number validation check
    private fun isOtpValid(otp: String): Boolean {
        return otp.length == configurations.getMaxOTPLength()
    }

    fun requestForOtp(otp: String) {
        viewModelScope.launch {
            try {
                dataStoreRepository.saveCToken(networkRepository.requestForOtp(otp).data.cToken)
                _otpRequestResult.postValue(Event(SUCCESS.statusReturn))
            } catch (e: ApiException) {
                loggerClass.error(e)
                _otpRequestResult.postValue(Event(ERROR.statusReturn))
            } catch (e: NoInternetException) {
                loggerClass.error(e)
                _otpRequestResult.postValue(Event(NO_INTERNET.statusReturn))
            } catch (e: ErrorException) {
                loggerClass.error(e)
                _otpRequestResult.postValue(Event(ERROR.statusReturn))
            } catch (e: SocketTimeoutException) {
                loggerClass.error(e)
                _otpRequestResult.postValue(Event(ERROR.statusReturn))
            }
        }
    }

    suspend fun verifyOtp(otp: String) =
        withContext(Dispatchers.IO) {
            try {
                userRegistration.userLoggedIn(networkRepository.verifyOtp(otp))
                return@withContext LoginResult(success = true)
            } catch (e: ApiException) {
                loggerClass.error(e)
                return@withContext (LoginResult(error = ERROR.statusReturn))
            } catch (e: NoInternetException) {
                loggerClass.error(e)
                return@withContext (LoginResult(error = NO_INTERNET.statusReturn))
            } catch (e: ErrorException) {
                loggerClass.error(e)
                return@withContext (LoginResult(error = e.code))
            } catch (e: SocketTimeoutException) {
                loggerClass.error(e)
                return@withContext (LoginResult(error = ERROR.statusReturn))
            }
        }


    fun startResendTimer() {
        if (!_isTimerRunning) {
            _countDownTimerOtpResend = object :
                CountDownTimer(configurations.getMaxOTPResendWait() * 1000L, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    _isTimerRunning = true
                    _mutableLiveDataResendTime.value =
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished + 1000).toInt()
                }

                override fun onFinish() {
                    _mutableLiveDataResendTime.value = 0
                }
            }.start()
        }
    }

    fun stopResendTimer() {
        _isTimerRunning = false
        _countDownTimerOtpResend!!.cancel()
    }

    fun getMaxPhoneNumberDigit() = configurations.getMaxMobileNumberDigit()
    fun isMandatoryLogin() = configurations.isLoginMandatory()
    suspend fun setLogInSkipped() = dataStoreRepository.setLoginSkipped()

}
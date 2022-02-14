package com.example.nammametromvvm.ui.login.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nammametromvvm.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.ui.login.data.LoginFormState
import com.example.nammametromvvm.ui.login.data.LoginResult
import com.example.nammametromvvm.utility.ApiException
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.ErrorException
import com.example.nammametromvvm.utility.NoInternetException
import com.example.nammametromvvm.utility.StatusEnum.*
import com.example.nammametromvvm.utility.logs.LoggerClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


class LoginViewModel(
    private val networkRepository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val configurations: Configurations,
    private val loggerClass: LoggerClass
) : ViewModel() {

    private val _phoneNumberForm = MutableLiveData<LoginFormState>()
    private val _otpForm = MutableLiveData<LoginFormState>()
    private val _otpRequestResult = MutableLiveData<Int>()

    val loginFormState: LiveData<LoginFormState> = _phoneNumberForm
    val otpFormState: LiveData<LoginFormState> = _otpForm

    private var countDownTimerOtpResend: CountDownTimer? = null
    var mutableLiveDataResendTime = MutableLiveData(configurations.getMaxOTPResendWait())
    private var isTimerRunning = false

    val OtpRequestResult: LiveData<Int>
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

    suspend fun requestForOtp(otp: String) =
        withContext(Dispatchers.IO) {
            try {
                dataStoreRepository.saveCToken(networkRepository.requestForOtp(otp).data.cToken)
                return@withContext SUCCESS.statusReturn
            } catch (e: ApiException) {
                loggerClass.error(e)
                return@withContext ERROR.statusReturn
            } catch (e: NoInternetException) {
                loggerClass.error(e)
                return@withContext NO_INTERNET.statusReturn
            } catch (e: ErrorException) {
                loggerClass.error(e)
                return@withContext ERROR.statusReturn
            } catch (e: SocketTimeoutException) {
                loggerClass.error(e)
                return@withContext ERROR.statusReturn
            }
        }

    suspend fun verifyOtp(otp: String) =
        withContext(Dispatchers.IO) {
            try {
                val otpVerificationData = networkRepository.verifyOtp(otp)
                dataStoreRepository.saveCKey(otpVerificationData.cKey)
                dataStoreRepository.saveUserName(otpVerificationData.name)
                dataStoreRepository.saveUserEmail(otpVerificationData.email)
                dataStoreRepository.userLoggedIn()
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
        if (!isTimerRunning) {
            countDownTimerOtpResend = object :
                CountDownTimer(configurations.getMaxOTPResendWait() * 1000L, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    isTimerRunning = true
                    mutableLiveDataResendTime.value =
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished + 1000).toInt()
                }

                override fun onFinish() {
                    mutableLiveDataResendTime.value = 0
                }
            }.start()
        }
    }

    fun stopResendTimer() {
        isTimerRunning = false
        countDownTimerOtpResend!!.cancel()
    }

    fun getMaxPhoneNumberDigit() = configurations.getMaxMobileNumberDigit()
    fun isMandatoryLogin() = configurations.isLoginMandatory()
    suspend fun setLogInSkipped()= dataStoreRepository.setLoginSkipped()

}
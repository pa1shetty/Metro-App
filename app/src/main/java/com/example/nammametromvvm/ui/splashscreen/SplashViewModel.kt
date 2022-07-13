package com.example.nammametromvvm.ui.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammametromvvm.data.repositaries.DataBaseRepository
import com.example.nammametromvvm.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.database.module.User
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.data.repositaries.network.responses.appUpdate.UpdateData
import com.example.nammametromvvm.ui.splashscreen.enumReturn.SplashScreenEnum.ConfigEnum.*
import com.example.nammametromvvm.ui.splashscreen.enumReturn.SplashScreenEnum.UpdateEnum
import com.example.nammametromvvm.utility.ApiException
import com.example.nammametromvvm.utility.AppConstants.appUpdateCheckTime
import com.example.nammametromvvm.utility.AppConstants.appUpdateTimeDifferenceType
import com.example.nammametromvvm.utility.AppConstants.dataStoreDefaultValue
import com.example.nammametromvvm.utility.AppConstants.defaultModifiedOn
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.ErrorException
import com.example.nammametromvvm.utility.NoInternetException
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dateMethods: DateMethods,
    private val loggerClass: LoggerClass,
    private val dataBaseRepository: DataBaseRepository,
    private val configurations: Configurations,
) : ViewModel() {
    suspend fun checkForUpdate() =
        withContext(Dispatchers.IO) {
            try {
                val updateResponse = networkRepository.checkForUpdate()
                dataStoreRepository.saveLastAppUpdateDate(dateMethods.dateTimeInString())
                dataStoreRepository.saveUpgradeFlag(updateResponse.UpdateData.upgradeFlag.toString())
                return@withContext updateResponse.UpdateData
            } catch (e: ApiException) {
                loggerClass.error(e)
                return@withContext UpdateData(UpdateEnum.ERROR.update)
            } catch (e: NoInternetException) {
                loggerClass.error(e)
                return@withContext UpdateData(UpdateEnum.ERROR.update)
            } catch (e: ErrorException) {
                loggerClass.error(e)
                return@withContext UpdateData(UpdateEnum.ERROR.update)
            } catch (e: SocketTimeoutException) {
                loggerClass.error(e)
                return@withContext UpdateData(UpdateEnum.ERROR.update)
            }
        }

    suspend fun isUpdateCheckNeeded(): Boolean {
        var isUpdateCheckNeeded = false
        val lastAppUpdateCheckTimeInString = dataStoreRepository.getLastAppUpdateCheckedTime()
        if (lastAppUpdateCheckTimeInString == dataStoreDefaultValue) {
            isUpdateCheckNeeded = true
        } else {
            if (dateMethods.findDifferenceBetweenDates(
                    dateMethods.stringToDate(lastAppUpdateCheckTimeInString),
                    dateMethods.stringToDate(dateMethods.dateTimeInString()),
                    appUpdateTimeDifferenceType
                ) > appUpdateCheckTime
            ) {
                isUpdateCheckNeeded = true
            }
            dateMethods.stringToDate(dateMethods.dateTimeInString())
            dateMethods.stringToDate(lastAppUpdateCheckTimeInString)
        }
        return isUpdateCheckNeeded
    }


    suspend fun getUpgradeFlag() = dataStoreRepository.getUpgradeFlag().toInt()
    private suspend fun getConfigLastModifiedOn() = dataStoreRepository.getConfigLastModifiedOn()


    suspend fun configDownload() =
        withContext(Dispatchers.IO) {
            try {
                networkRepository.configDownload()
                return@withContext UPDATED.configReturn
            } catch (e: ApiException) {
                loggerClass.error(e)
                return@withContext configDownloadErrorReturn(e, ERROR.configReturn)
            } catch (e: NoInternetException) {
                loggerClass.error(e)
                return@withContext configDownloadErrorReturn(e, NO_INTERNET.configReturn)
            } catch (e: ErrorException) {
                loggerClass.error(e)
                return@withContext UP_TO_DATE.configReturn
            } catch (e: SocketTimeoutException) {
                loggerClass.error(e)
                return@withContext configDownloadErrorReturn(e, ERROR.configReturn)
            }
        }

    private suspend fun configDownloadErrorReturn(e: Exception, errorCode: Int): Int {
        loggerClass.error(e)
        if (getConfigLastModifiedOn() == defaultModifiedOn) {
            return errorCode
        }
        return ERROR_BUT_PROCEED.configReturn
    }

    fun saveLoggedInUser(user: User) {
        viewModelScope.launch { dataBaseRepository.saveUser(user) }
    }

    suspend fun isUserLoggedIn(): Flow<Boolean> =
        withContext(Dispatchers.IO) { dataStoreRepository.isUserLoggedInAsFlow() }

    fun isMandatoryLogin(): Boolean = configurations.isLoginMandatory()
    suspend fun isLoginSkipped(): Flow<Boolean> =
        withContext(Dispatchers.IO) { dataStoreRepository.isLoginSkipped() }


}

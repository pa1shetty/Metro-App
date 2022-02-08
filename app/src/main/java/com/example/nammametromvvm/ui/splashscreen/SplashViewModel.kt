package com.example.nammametromvvm.ui.splashscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mymvvmsample.data.UpdateData
import com.example.mymvvmsample.data.repositaries.DataBaseRepository
import com.example.mymvvmsample.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.data.repositaries.entites.User
import com.example.nammametromvvm.ui.splashscreen.enumReturn.SplashScreenEnum.*
import com.example.nammametromvvm.utility.ApiException
import com.example.nammametromvvm.utility.AppConstants
import com.example.nammametromvvm.utility.AppConstants.appUpdateCheckTime
import com.example.nammametromvvm.utility.AppConstants.appUpdateTimeDifferenceType
import com.example.nammametromvvm.utility.AppConstants.dataStoreDefaultValue
import com.example.nammametromvvm.utility.AppConstants.defaultModifiedOn
import com.example.nammametromvvm.utility.ErrorException
import com.example.nammametromvvm.utility.NoInternetException
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import com.example.nammametromvvm.utility.logs.Logs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.net.SocketTimeoutException
import java.util.*

class SplashViewModel(
    application: Application,
    private val logs: Logs,
    private val networkRepository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dateMethods: DateMethods,
    private val loggerClass: LoggerClass,
    private val dataBaseRepository: DataBaseRepository
) : AndroidViewModel(application) {
    suspend fun checkForUpdate() =
        withContext(Dispatchers.IO) {
            try {
                val updateResponse = networkRepository.checkForUpdate()
                dataStoreRepository.saveLastAppUpdateDate(dateMethods.currentTimeInString())
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
                    dateMethods.stringToDate(dateMethods.currentTimeInString()),
                    appUpdateTimeDifferenceType
                ) > appUpdateCheckTime
            ) {
                isUpdateCheckNeeded = true
            }
            dateMethods.stringToDate(dateMethods.currentTimeInString())
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
                return@withContext ConfigEnum.UPDATED.configReturn
            } catch (e: ApiException) {
                return@withContext configDownloadErrorReturn(e, ConfigEnum.ERROR.configReturn)
            } catch (e: NoInternetException) {
                return@withContext configDownloadErrorReturn(e, ConfigEnum.NO_INTERNET.configReturn)
            } catch (e: ErrorException) {
                return@withContext ConfigEnum.UP_TO_DATE.configReturn
            } catch (e: SocketTimeoutException) {
                return@withContext configDownloadErrorReturn(e, ConfigEnum.ERROR.configReturn)
            }
        }

    private suspend fun configDownloadErrorReturn(e: Exception, errorCode: Int): Int {
        loggerClass.error(e)
        if (getConfigLastModifiedOn() == defaultModifiedOn) {
            return errorCode
        }
        return ConfigEnum.ERROR_BUT_PROCEED.configReturn
    }

    suspend fun saveLoggedInUser(user: User) =
        withContext(Dispatchers.IO) { dataBaseRepository.saveUser(user) }
    suspend fun isUserLoggedIn(): Flow<Boolean> = withContext(Dispatchers.IO) { dataStoreRepository.isUserLoggedIn() }
    suspend fun userLoggedIn()= dataStoreRepository.userLoggedIn()
    suspend fun userLoggedOut()= dataStoreRepository.userLoggedOut()

}

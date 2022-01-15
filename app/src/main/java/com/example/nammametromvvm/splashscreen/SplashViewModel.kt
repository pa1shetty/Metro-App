package com.example.nammametromvvm.splashscreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.mymvvmsample.data.Update
import com.example.mymvvmsample.data.UpdateData
import com.example.mymvvmsample.data.network.MyApi.Companion.gson
import com.example.mymvvmsample.data.repositaries.UserRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreSetting
import com.example.nammametromvvm.splashscreen.enumReturn.UpdateEnum
import com.example.nammametromvvm.utility.ApiError
import com.example.nammametromvvm.utility.ApiException
import com.example.nammametromvvm.utility.AppConstants
import com.example.nammametromvvm.utility.AppConstants.appUpdateCheckTime
import com.example.nammametromvvm.utility.AppConstants.appUpdateTimeDifferenceType
import com.example.nammametromvvm.utility.AppConstants.dataStoreDefaultValue
import com.example.nammametromvvm.utility.NoInternetException
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import com.example.nammametromvvm.utility.logs.Logs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.SocketTimeoutException
import java.util.*

class SplashViewModel(
    application: Application,
    private val logs: Logs,
    private val userRepository: UserRepository,
    private val dataStoreSetting: DataStoreSetting,
    private val dateMethods: DateMethods,
    private val loggerClass: LoggerClass
) : AndroidViewModel(application) {
    fun setUpLogs() {
        logs.toFile(
            AppConstants.INTERNAL_LOG_PATH + File.separator + AppConstants.FILE_NAME,
            AppConstants.NO_OF_FILES
        )
    }

    suspend fun checkForUpdate() =
        withContext(Dispatchers.IO) {
            try {
                val updateResponse = gson.fromJson(
                    userRepository.checkForUpdate(),
                    Update::class.java
                )
                dataStoreSetting.saveLastAppUpdateDate(dateMethods.currentTimeInString())
                dataStoreSetting.saveUpgradeFlag(updateResponse.UpdateData.upgradeFlag.toString())
                return@withContext updateResponse.UpdateData
            } catch (e: ApiException) {
                loggerClass.error(e)
                return@withContext UpdateData(UpdateEnum.ERROR.update)
            } catch (e: NoInternetException) {
                loggerClass.error(e)
                Log.d("test156", "NoInternetException")
                return@withContext UpdateData(UpdateEnum.ERROR.update)
            } catch (e: ApiError) {
                loggerClass.error(e)
                return@withContext UpdateData(UpdateEnum.ERROR.update)
            } catch (e: SocketTimeoutException) {
                loggerClass.error(e)
                return@withContext UpdateData(UpdateEnum.ERROR.update)
            }
        }

    suspend fun isUpdateCheckNeeded(): Boolean {
        var isUpdateCheckNeeded = false
        val lastAppUpdateCheckTimeInString = dataStoreSetting.getLastAppUpdateCheckedTime()
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

    fun gotoPlayStore() {
        Log.d("test162", "Go to playstore")
    }

    suspend fun getUpgradeFlag() = dataStoreSetting.getUpgradeFlag().toInt()

}
package com.example.nammametromvvm.splashscreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymvvmsample.data.repositaries.UserRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreSetting
import com.example.nammametromvvm.utility.AppConstants
import com.example.nammametromvvm.utility.AppConstants.dataStoreDefaultValue
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.Logs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SplashViewModel(
    application: Application,
    private val logs: Logs,
    private val userRepository: UserRepository,
    private val dataStoreSetting: DataStoreSetting,
    private val dateMethods: DateMethods
) : AndroidViewModel(application) {
    // val readFromDataStore = dataStoreSetting.getLastAppUpdateDate.asLiveData()
    fun setUpLogs() {
        logs.toFile(
            AppConstants.INTERNAL_LOG_PATH + File.separator + AppConstants.FILE_NAME,
            AppConstants.NO_OF_FILES
        )
    }

    // suspend fun getLastUpdateTime()= dataStoreSetting.getLastAppUpdateDate.asLiveData()
    suspend fun checkForUpdate() =
        withContext(Dispatchers.IO) { userRepository.checkForUpdate() }

    fun saveToDataStore(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreSetting.saveLastAppUpdateDate(name)
        }
    }

    suspend fun isUpdateCheckNeeded(): Boolean {
        var isUpdateCheckNeeded = false
        val lastAppUpdateCheckTimeInString = dataStoreSetting.getLastAppUpdateDate()
        val test=dateMethods.currentTimeInString();
        Log.d("test151", "isUpdateCheckNeeded: "+dateMethods.currentTimeInString())
        if (lastAppUpdateCheckTimeInString == dataStoreDefaultValue) {
            isUpdateCheckNeeded = true
        } else {
            val currentTimeInDate = dateMethods.stringToDate(dateMethods.currentTimeInString())
            val lastAppUpdateCheckTimeInDate = dateMethods.stringToDate(lastAppUpdateCheckTimeInString)
        }

        return isUpdateCheckNeeded

    }
}
@file:Suppress("unused")

package com.example.nammametromvvm.utility

import android.app.Application
import com.example.nammametromvvm.utility.AppConstants.INTERNAL_LOG_PATH
import com.example.nammametromvvm.utility.AppConstants.LOG_FOLDER_NAME
import com.example.nammametromvvm.utility.AppConstants.configurations
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltAndroidApp
class ApplicationClass : Application() {
    @Inject
    lateinit var configurationsClass: Configurations

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        INTERNAL_LOG_PATH =
            applicationContext.getExternalFilesDir(LOG_FOLDER_NAME).toString() + File.separator
        GlobalScope.launch { configurations = configurationsClass.getConfigurationsFromDb() }
    }


}
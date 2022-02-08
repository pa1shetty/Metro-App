@file:Suppress("unused")

package com.example.nammametromvvm.utility

import android.app.Application
import com.example.nammametromvvm.utility.AppConstants.INTERNAL_LOG_PATH
import com.example.nammametromvvm.utility.AppConstants.LOG_FOLDER_NAME
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        INTERNAL_LOG_PATH =
            applicationContext.getExternalFilesDir(LOG_FOLDER_NAME).toString() + File.separator
    }



}
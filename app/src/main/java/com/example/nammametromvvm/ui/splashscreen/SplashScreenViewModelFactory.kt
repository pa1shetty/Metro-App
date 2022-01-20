package com.example.nammametromvvm.ui.splashscreen

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymvvmsample.data.repositaries.DataBaseRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.mymvvmsample.data.repositaries.NetworkRepository
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import com.example.nammametromvvm.utility.logs.Logs

@SuppressLint("CustomSplashScreen")
class SplashScreenViewModelFactory(
    private val application: Application,
    private val logs: Logs,
    private val networkRepository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dateMethods: DateMethods,
    private val loggerClass: LoggerClass,
    private val dataBaseRepository: DataBaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(application, logs, networkRepository,dataStoreRepository,dateMethods,loggerClass,dataBaseRepository) as T
    }
}
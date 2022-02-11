package com.example.nammametromvvm.ui.splashscreen

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymvvmsample.data.repositaries.DataBaseRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.mymvvmsample.data.repositaries.NetworkRepository
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.LoggerClass
import javax.inject.Inject
@Suppress("UNCHECKED_CAST")
@SuppressLint("CustomSplashScreen")
class SplashScreenViewModelFactory @Inject constructor(
    private val application: Application,
    private val networkRepository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dateMethods: DateMethods,
    private val loggerClass: LoggerClass,
    private val dataBaseRepository: DataBaseRepository,
    private val configurations: Configurations
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(
            application,
            networkRepository,
            dataStoreRepository,
            dateMethods,
            loggerClass,
            dataBaseRepository,
            configurations
        ) as T
    }
}
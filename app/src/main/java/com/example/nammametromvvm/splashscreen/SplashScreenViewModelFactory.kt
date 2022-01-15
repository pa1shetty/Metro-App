package com.example.nammametromvvm.splashscreen

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreSetting
import com.example.mymvvmsample.data.repositaries.UserRepository
import com.example.nammametromvvm.utility.date.DateMethods
import com.example.nammametromvvm.utility.logs.Logs

@SuppressLint("CustomSplashScreen")
class SplashScreenViewModelFactory(
    private val application: Application,
    private val logs: Logs,
    private val userRepository: UserRepository,
    private val dataStoreSetting: DataStoreSetting,
    private val dateMethods: DateMethods
    ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(application, logs, userRepository,dataStoreSetting,dateMethods) as T
    }
}
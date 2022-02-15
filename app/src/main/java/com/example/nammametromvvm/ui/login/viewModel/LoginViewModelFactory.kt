package com.example.nammametromvvm.ui.login.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nammametromvvm.data.repositaries.NetworkRepository
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.utility.Configurations
import com.example.nammametromvvm.utility.UserRegistration
import com.example.nammametromvvm.utility.logs.LoggerClass
import javax.inject.Inject

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory @Inject constructor(
    private val application: Application,
    private val networkRepository: NetworkRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val configurations: Configurations,
    private val loggerClass: LoggerClass,
    private val userRegistration: UserRegistration
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(
            application,
            networkRepository,
            dataStoreRepository,
            configurations,
            loggerClass,
            userRegistration
        ) as T
    }
}
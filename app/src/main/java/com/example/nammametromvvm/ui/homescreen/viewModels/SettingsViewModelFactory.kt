package com.example.nammametromvvm.ui.homescreen.viewModels

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.utility.UserRegistration
import com.example.nammametromvvm.utility.language.LocaleManager
import com.example.nammametromvvm.utility.theme.HandleTheme
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@SuppressLint("CustomSplashScreen")
class SettingsViewModelFactory @Inject constructor(
    private val application: Application,
    private val userRegistration: UserRegistration,
    private val dataStoreRepository: DataStoreRepository,
    private val handleTheme: HandleTheme,
    private val localeManager: LocaleManager
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            application,
            userRegistration,
            dataStoreRepository,
            handleTheme,
            localeManager
        ) as T
    }
}
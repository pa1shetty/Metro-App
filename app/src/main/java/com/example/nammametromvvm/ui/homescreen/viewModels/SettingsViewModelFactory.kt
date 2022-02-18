package com.example.nammametromvvm.ui.homescreen.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nammametromvvm.utility.UserRegistration
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@SuppressLint("CustomSplashScreen")
class SettingsViewModelFactory @Inject constructor(
    private val userRegistration: UserRegistration
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            userRegistration
        ) as T
    }
}
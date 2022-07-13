package com.example.nammametromvvm.ui.homescreen.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.utility.UserRegistration
import com.example.nammametromvvm.utility.language.LocaleManager
import com.example.nammametromvvm.utility.theme.HandleTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRegistration: UserRegistration,
    private val dataStoreRepository: DataStoreRepository,
    private val handleTheme: HandleTheme,
    private val localeManager: LocaleManager,
) : ViewModel() {
    suspend fun logOut(): Boolean =
        withContext(Dispatchers.IO) {
            userRegistration.userLoggedOut()
        }

    suspend fun getCurrentTheme(): String {
        return handleTheme.getCurrentTheme()
    }

    fun saveCurrentTheme(themeName: String) {
        viewModelScope.launch {
            handleTheme.saveCurrentTheme(themeName)
        }
    }

    fun getThemes() = handleTheme.getThemes()

    suspend fun isUserLoggedIn(): Flow<Boolean> =
        withContext(Dispatchers.IO) { dataStoreRepository.isUserLoggedInAsFlow() }


    fun setNewLocale(context: Context, language: String) {
        viewModelScope.launch {
            localeManager.setNewLocale(context, language)
        }
    }

    fun getLanguages() = localeManager.setUpLanguages()
    suspend fun getCurrentLanguage() = localeManager.getCurrentLanguage()

}
package com.example.nammametromvvm.data.repositaries.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.nammametromvvm.data.repositaries.datastore.PreferencesKeys.currentLanguage
import com.example.nammametromvvm.data.repositaries.datastore.PreferencesKeys.currentTheme
import com.example.nammametromvvm.ui.splashscreen.enumReturn.SplashScreenEnum.UpdateEnum.NO_UPDATE
import com.example.nammametromvvm.utility.AesLibrary
import com.example.nammametromvvm.utility.AppConstants.dataStoreDefaultValue
import com.example.nammametromvvm.utility.AppConstants.dataStoreName
import com.example.nammametromvvm.utility.AppConstants.defaultLanguageValue
import com.example.nammametromvvm.utility.AppConstants.defaultModifiedOn
import com.example.nammametromvvm.utility.AppConstants.defaultThemeValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(dataStoreName)

class DataStoreRepository @Inject constructor(
    context: Context,
    private var aesLibrary: AesLibrary
) {
    private val dataStore = context.dataStore

    private suspend fun saveStringData(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preference ->
            preference[key] = aesLibrary.encryptData(value)
        }
    }

    @Suppress("unused")
    private fun getStringDataOld(key: Preferences.Key<String>): Flow<String> =
        dataStore.data
            .catch {
                if (this is IOException) {
                    emit(emptyPreferences())
                }
            }.map { preference ->
                var value = dataStoreDefaultValue
                preference[key]?.let {
                    value = aesLibrary.decryptData(
                        it
                    )
                }
                value
            }

    suspend fun saveLastAppUpdateDate(lastAppUpdateDateValue: String) {
        saveStringData(PreferencesKeys.lastAppUpdateDate, lastAppUpdateDateValue)
    }

    suspend fun saveUpgradeFlag(upgradeFlag: String) {
        saveStringData(PreferencesKeys.upgradeFlag, upgradeFlag)
    }

    suspend fun getUpgradeFlag(): String {
        return getStringData(
            PreferencesKeys.upgradeFlag,
            NO_UPDATE.update.toString()
        )
    }

    suspend fun saveConfigLastModifiedOn(lastConfigLastModifiedOnValue: String) {
        saveStringData(PreferencesKeys.configLastModifiedOn, lastConfigLastModifiedOnValue)
    }

    suspend fun saveCToken(cToken: String) {
        saveStringData(PreferencesKeys.cToken, cToken)
    }


    suspend fun getCToken(): String {
        return getStringData(
            PreferencesKeys.cToken
        )
    }

    suspend fun saveSplTkn(cToken: String) {
        saveStringData(PreferencesKeys.splTkn, cToken)
    }

    suspend fun getSplTkn(): String {
        return getStringData(
            PreferencesKeys.splTkn
        )
    }

    suspend fun saveCKey(cKey: String = dataStoreDefaultValue) {
        saveStringData(PreferencesKeys.cKey, cKey)
    }

    @Suppress("unused")
    suspend fun getCKey(): String {
        return getStringData(
            PreferencesKeys.cKey
        )
    }

    suspend fun saveUserName(userName: String = dataStoreDefaultValue) {
        saveStringData(PreferencesKeys.userName, userName)
    }

    @Suppress("unused")
    suspend fun getUserName(): String {
        return getStringData(
            PreferencesKeys.userName
        )
    }

    suspend fun saveUserEmail(userName: String = dataStoreDefaultValue) {
        saveStringData(PreferencesKeys.userEmail, userName)
    }

    @Suppress("unused")
    suspend fun getUserEmail(): String {
        return getStringData(
            PreferencesKeys.userEmail
        )
    }


    suspend fun getConfigLastModifiedOn(): String {
        return getStringData(
            PreferencesKeys.configLastModifiedOn,
            defaultModifiedOn
        )
    }


    suspend fun getLastAppUpdateCheckedTime(): String {
        return getStringData(PreferencesKeys.lastAppUpdateDate)
    }

    private suspend fun getStringData(
        key: Preferences.Key<String>,
        defaultValue: String = dataStoreDefaultValue
    ): String {
        val preferences = dataStore.data.first()
        var value = defaultValue
        preferences[key]?.let {
            value = aesLibrary.decryptData(
                it
            )
        }
        return value
    }

    private suspend fun getBooleanData(
        key: Preferences.Key<Boolean>,
        defaultValue: Boolean = false
    ): Boolean {
        val preferences = dataStore.data.first()
        var value = defaultValue
        preferences[key]?.let {
            value = it
        }
        return value
    }

    suspend fun isUserLoggedIn() = getBooleanData(PreferencesKeys.userLoggedIn)

    fun isUserLoggedInAsFlow() = dataStore.data.catch { exception -> // 1
        if (exception is IOException) { // 2
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[PreferencesKeys.userLoggedIn] ?: false }

    fun isLoginSkipped() = dataStore.data.catch { exception -> // 1
        if (exception is IOException) { // 2
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[PreferencesKeys.loginSkipped] ?: false }

    suspend fun userLoggedIn() {
        saveUserLogInData(true)
    }

    @Suppress("unused")
    suspend fun userLoggedOut() {
        saveUserLogInData(false)
    }

    private suspend fun saveUserLogInData(loginBoolean: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.userLoggedIn] = loginBoolean
        }
    }

    suspend fun setLoginSkipped() {
        saveLoginSkipped(true)
    }

    @Suppress("unused")
    suspend fun clearLoginSkipped() {
        saveLoginSkipped(false)
    }

    private suspend fun saveLoginSkipped(loginSkipBoolean: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.loginSkipped] = loginSkipBoolean
        }
    }

    fun getUserNameFlow() = getStringAsFlow(PreferencesKeys.userName)

    suspend fun saveCurrentTheme(currentTheme: String) {
        saveStringData(PreferencesKeys.currentTheme, currentTheme)
    }

    suspend fun saveCurrentLanguage(currentLanguage: String = defaultLanguageValue) {
        saveStringData(PreferencesKeys.currentLanguage, currentLanguage)
    }

    fun getCurrentThemeAsFlow() = getStringAsFlow(currentTheme, defaultThemeValue)
    fun getCurrentLanguageAsFlow() = getStringAsFlow(currentLanguage, defaultLanguageValue)
    suspend fun getCurrentLanguage() = getStringData(currentLanguage, defaultLanguageValue)
    suspend fun getCurrentTheme() = getStringData(currentTheme, defaultThemeValue)


    private fun getStringAsFlow(
        key: Preferences.Key<String>,
        defaultValue: String = dataStoreDefaultValue
    ): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
            it[key]?.let { it1 -> aesLibrary.decryptData(it1) } ?: defaultValue
        }

    suspend fun clearDatastore() {
        dataStore.edit {
            it.clear()
        }
    }
}
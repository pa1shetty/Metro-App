package com.example.nammametromvvm.data.repositaries.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.codingwithjks.datastorepreferences.dataStoreSetting.PreferencesKeys
import com.example.nammametromvvm.splashscreen.enumReturn.UpdateEnum
import com.example.nammametromvvm.utility.AesLibrary
import com.example.nammametromvvm.utility.AppConstants.dataStoreDefaultValue
import com.example.nammametromvvm.utility.AppConstants.dataStoreName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(dataStoreName)

class DataStoreSetting(context: Context, private var aesLibrary: AesLibrary) {
    private val dataStore = context.dataStore

    private suspend fun saveStringData(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preference ->
            preference[key] = aesLibrary.encryptData(value)
        }
    }

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
            UpdateEnum.NO_UPDATE.update.toString()
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


}
package com.example.nammametromvvm.data.repositaries.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val name = stringPreferencesKey("name")
    val lastAppUpdateDate = stringPreferencesKey("lastAppUpdateDate")
    val upgradeFlag = stringPreferencesKey("upgradeFlag")
    val configLastModifiedOn = stringPreferencesKey("configLastModifiedOn")
    val userLoggedIn = booleanPreferencesKey("userLoggedIn")
    val cToken = stringPreferencesKey("cToken")

}
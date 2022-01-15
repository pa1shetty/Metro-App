package com.codingwithjks.datastorepreferences.dataStoreSetting

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val name = stringPreferencesKey("name")
    val lastAppUpdateDate = stringPreferencesKey("lastAppUpdateDate")

}
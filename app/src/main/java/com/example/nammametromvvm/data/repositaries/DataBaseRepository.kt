package com.example.nammametromvvm.data.repositaries

import android.content.Context
import com.example.nammametromvvm.data.repositaries.entites.AppDatabase
import com.example.nammametromvvm.data.repositaries.entites.Config
import com.example.nammametromvvm.data.repositaries.entites.User
import com.example.nammametromvvm.utility.AppConstants.configurations
import javax.inject.Inject

class DataBaseRepository @Inject constructor(
    private val db: AppDatabase,
) {

    fun saveConfig(config: List<Config>) {
        db.getConfigDao().nukeConfigurable()
        db.getConfigDao().saveConfig(config)
        configurations = getConfigs()
    }

    fun getConfigs() = db.getConfigDao().getConfigs()
    @Suppress("unused")
    fun getConfig(key: String) = db.getConfigDao().getConfig(key)
    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)
    @Suppress("unused")
    fun getUser() = db.getUserDao().getUser()
    fun clearDb(context: Context) = db.deleteDb(context)
}
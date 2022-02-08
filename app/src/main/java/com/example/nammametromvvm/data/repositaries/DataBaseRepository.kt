package com.example.mymvvmsample.data.repositaries

import com.example.nammametromvvm.data.repositaries.entites.AppDatabase
import com.example.nammametromvvm.data.repositaries.entites.Config
import com.example.nammametromvvm.data.repositaries.entites.User
import javax.inject.Inject

class DataBaseRepository  @Inject constructor(
    private val db: AppDatabase
) {

    fun saveConfig(config: List<Config>) {
        db.getConfigDao().nukeTConfigable()
        db.getConfigDao().saveConfig(config)
    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getUser()
}
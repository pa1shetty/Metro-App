package com.example.nammametromvvm.data.repositaries.entites

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
@Entity
interface ConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveConfig(config: List<Config>)

    @Query("SELECT * FROM config")
    @JvmSuppressWildcards
    fun getUser(): LiveData<Config>

    @Query("DELETE FROM config")
    fun nukeTConfigable()
}
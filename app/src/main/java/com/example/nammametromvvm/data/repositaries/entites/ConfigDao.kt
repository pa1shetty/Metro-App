package com.example.nammametromvvm.data.repositaries.entites

import androidx.room.*

@Dao
@Entity
interface ConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveConfig(config: List<Config>)


    @Query("SELECT configValue FROM config WHERE configName = :configName")
    fun getConfig(configName: String): String

    @Query("SELECT * FROM config")
    @JvmSuppressWildcards
    fun getConfigs(): List<Config>



    @Query("DELETE FROM config")
    fun nukeConfigurable()

}
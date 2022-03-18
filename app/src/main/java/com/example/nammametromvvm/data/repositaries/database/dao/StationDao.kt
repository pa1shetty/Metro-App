package com.example.nammametromvvm.data.repositaries.database.dao

import androidx.room.*
import com.example.nammametromvvm.data.repositaries.network.responses.stationLists.Station
import kotlinx.coroutines.flow.Flow

@Dao
@Entity
interface StationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveStations(stations: List<Station>)


    @Query("SELECT * FROM station Where  englishName LIKE :searchQuery OR kannadaName LIKE :searchQuery")
    @JvmSuppressWildcards
    fun getStations(searchQuery: String): Flow<List<Station>>


    @Query("DELETE FROM station")
    fun nukeStations()

}
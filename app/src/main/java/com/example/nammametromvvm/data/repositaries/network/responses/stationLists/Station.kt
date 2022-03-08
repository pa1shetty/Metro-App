package com.example.nammametromvvm.data.repositaries.network.responses.stationLists


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "station")
data class Station(
    @SerializedName("englishName")
    val englishName: String,
    @SerializedName("kannadaName")
    val kannadaName: String,
    @SerializedName("stationCode")
    @PrimaryKey
    val stationCode: String,
    @SerializedName("stationID")
    val stationID: String
)
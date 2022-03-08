package com.example.nammametromvvm.data.repositaries.network.responses.stationLists


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("stations")
    val stations: List<Station>,
    @SerializedName("update_needed")
    val updateNeeded: Int
)
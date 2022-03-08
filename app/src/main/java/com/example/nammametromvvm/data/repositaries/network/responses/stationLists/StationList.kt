package com.example.nammametromvvm.data.repositaries.network.responses.stationLists


import com.google.gson.annotations.SerializedName

data class StationList(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("splTkn")
    val splTkn: String,
    @SerializedName("status")
    val status: String
)
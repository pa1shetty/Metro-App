package com.example.nammametromvvm.data.repositaries.network.responses.Login


import com.google.gson.annotations.SerializedName

data class RegiesterResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)
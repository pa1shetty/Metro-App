package com.example.nammametromvvm.data.repositaries.network.responses.Login.getOtp


import com.google.gson.annotations.SerializedName

data class GetOtp(
    @SerializedName("data")
    val `data`: GetOtpData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)
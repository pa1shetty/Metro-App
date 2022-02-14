package com.example.nammametromvvm.data.repositaries.network.responses.Login.getOtp


import com.google.gson.annotations.SerializedName

data class GetOtpData(
    @SerializedName("cToken")
    val cToken: String
)
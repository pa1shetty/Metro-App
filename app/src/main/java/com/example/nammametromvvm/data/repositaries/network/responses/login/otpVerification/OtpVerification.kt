package com.example.nammametromvvm.data.repositaries.network.responses.Login.otpVerification


import com.google.gson.annotations.SerializedName

data class OtpVerification(
    @SerializedName("data")
    val `data`: OtpVerificationData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)
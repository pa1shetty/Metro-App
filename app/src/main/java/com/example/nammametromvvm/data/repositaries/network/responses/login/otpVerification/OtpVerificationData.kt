package com.example.nammametromvvm.data.repositaries.network.responses.login.otpVerification


import com.google.gson.annotations.SerializedName

data class OtpVerificationData(
    @SerializedName("cKey")
    val cKey: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String
)
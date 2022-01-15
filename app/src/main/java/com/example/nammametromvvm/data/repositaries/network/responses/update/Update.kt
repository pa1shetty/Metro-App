package com.example.mymvvmsample.data


import com.google.gson.annotations.SerializedName

data class Update(
    @SerializedName("data")
    val updateData: updateData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)
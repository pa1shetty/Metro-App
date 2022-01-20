package com.example.nammametromvvm.data.repositaries.network.responses.ConfigDownload


import com.google.gson.annotations.SerializedName

data class ConfigDownloadResponse(
    @SerializedName("data")
    val configData: ConfigData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)
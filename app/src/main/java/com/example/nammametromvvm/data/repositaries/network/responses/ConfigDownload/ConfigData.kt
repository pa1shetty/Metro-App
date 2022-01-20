package com.example.nammametromvvm.data.repositaries.network.responses.ConfigDownload


import com.google.gson.annotations.SerializedName

data class ConfigData(
    @SerializedName("config")
    val config: Config,
    @SerializedName("modified_on")
    val modifiedOn: String
)
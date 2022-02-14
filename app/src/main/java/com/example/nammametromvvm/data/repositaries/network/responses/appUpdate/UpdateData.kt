package com.example.nammametromvvm.data.repositaries.network.responses.appUpdate


import com.google.gson.annotations.SerializedName

data class UpdateData(
    @SerializedName("upgrade_flag")
    var upgradeFlag: Int,
    @SerializedName("new_version")
    val newVersion: String=""
)
package com.example.mymvvmsample.data


import com.google.gson.annotations.SerializedName

data class UpdateData(
    @SerializedName("upgrade_flag")
    var upgradeFlag: Int,
    @SerializedName("new_version")
    val newVersion: String=""
)
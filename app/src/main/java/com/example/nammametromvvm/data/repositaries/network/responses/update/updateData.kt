package com.example.mymvvmsample.data


import com.google.gson.annotations.SerializedName

data class updateData(
  
    @SerializedName("new_version")
    val newVersion: String,
    @SerializedName("upgrade_flag")
    val upgradeFlag: String
)
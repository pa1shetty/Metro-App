package com.example.nammametromvvm.data.repositaries.network.responses.appUpdate


import com.google.gson.annotations.SerializedName

data class Update(

    @SerializedName("data")
    val UpdateData: UpdateData,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String

)


package com.example.mymvvmsample.data.network.responses

import com.example.myretrofit.TicketDetails.Data
import com.google.gson.annotations.SerializedName

data class TicketData(
    @SerializedName("data")
    val data: Data,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)



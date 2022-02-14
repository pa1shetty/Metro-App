package com.example.nammametromvvm.data.repositaries.network.responses.ticketDetails


import com.google.gson.annotations.SerializedName

data class PssngrData(
    @SerializedName("qrData")
    val qrData: String,
    @SerializedName("ticketId")
    val ticketId: String,
    @SerializedName("txnStatus")
    val txnStatus: String,
    @SerializedName("unitFare")
    val unitFare: String,
    @SerializedName("usedOn")
    val usedOn: String
)
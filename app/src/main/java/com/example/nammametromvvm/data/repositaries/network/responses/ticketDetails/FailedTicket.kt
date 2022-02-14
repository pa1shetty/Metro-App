package com.example.nammametromvvm.data.repositaries.network.responses.ticketDetails


import com.google.gson.annotations.SerializedName

data class FailedTicket(
    @SerializedName("fromStop")
    val fromStop: String,
    @SerializedName("nmbrOfPssngrs")
    val nmbrOfPssngrs: String,
    @SerializedName("paymentID")
    val paymentID: String,
    @SerializedName("toStop")
    val toStop: String,
    @SerializedName("totalFare")
    val totalFare: String,
    @SerializedName("txnDateTime")
    val txnDateTime: String,
    @SerializedName("txnID")
    val txnID: String,
    @SerializedName("txnStatus")
    val txnStatus: String
)
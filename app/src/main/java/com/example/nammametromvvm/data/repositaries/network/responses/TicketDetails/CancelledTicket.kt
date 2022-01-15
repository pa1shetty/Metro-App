package com.example.myretrofit.TicketDetails


import com.google.gson.annotations.SerializedName

data class CancelledTicket(
    @SerializedName("cancelledOn")
    val cancelledOn: String,
    @SerializedName("fromStop")
    val fromStop: String,
    @SerializedName("nmbrOfPssngrs")
    val nmbrOfPssngrs: String,
    @SerializedName("paymentID")
    val paymentID: String,
    @SerializedName("refundStatus")
    val refundStatus: String,
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
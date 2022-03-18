package com.example.nammametromvvm.data.repositaries.network.responses.ticketDetails

import com.example.nammametromvvm.data.repositaries.database.module.QrTicket
import com.google.gson.annotations.SerializedName

data class Data(
    val splTkn: String,
    @SerializedName("tickets")
    val qrTickets: List<QrTicket>
)
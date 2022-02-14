package com.example.nammametromvvm.data.repositaries.network.responses.ticketDetails


import com.google.gson.annotations.SerializedName

data class ActiveTickets(
    @SerializedName("pending_tickets")
    val pendingTickets: List<Any>,
    @SerializedName("unused_tickets")
    val unusedTickets: List<Any>
)
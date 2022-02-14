package com.example.nammametromvvm.data.repositaries.network.responses.ticketDetails


import com.google.gson.annotations.SerializedName
@Suppress("unused")
data class Data(
    @SerializedName("active_tickets")
    val activeTickets: ActiveTickets,
    @SerializedName("recent_tickets")
    val recentTickets: RecentTickets,
    @SerializedName("splTkn")
    val splTkn: String
)
package com.example.myretrofit.TicketDetails


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("active_tickets")
    val activeTickets: ActiveTickets,
    @SerializedName("recent_tickets")
    val recentTickets: RecentTickets,
    @SerializedName("splTkn")
    val splTkn: String
)
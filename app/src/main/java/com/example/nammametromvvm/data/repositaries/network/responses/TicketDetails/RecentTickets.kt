package com.example.myretrofit.TicketDetails


import com.google.gson.annotations.SerializedName

data class RecentTickets(
    @SerializedName("cancelled_tickets")
    val cancelledTickets: List<CancelledTicket>,
    @SerializedName("expired_tickets")
    val expiredTickets: List<ExpiredTicket>,
    @SerializedName("failed_tickets")
    val failedTickets: List<FailedTicket>,
    @SerializedName("used_tickets")
    val usedTickets: List<UsedTicket>
)
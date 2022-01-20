package com.example.nammametromvvm.data.repositaries.network.responses.ConfigDownload


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity
data class Config(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @SerializedName("max_active_tickets")
    val maxActiveTickets: String,
    @SerializedName("max_passenger_count")
    val maxPassengerCount: String,
    @SerializedName("max_recent_tickets")
    val maxRecentTickets: String,
    @SerializedName("max_suggestion_routes")
    val maxSuggestionRoutes: String,
    @SerializedName("qr_update_seconds")
    val qrUpdateSeconds: String,
    @SerializedName("ticket_prior_days")
    val ticketPriorDays: String
)
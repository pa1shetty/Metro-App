package com.example.nammametromvvm.data.repositaries.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qrTicket")
data class QrTicket(
    val cancelledOn: String,
    val expiredOn: String,
    val fromStop: String,
    val nmbrOfPssngrs: String,
    val paymentID: String,
    val paymentMode: String,
    val qrData: String,
    val qrSerialNumber: String,
    val refundStatus: String,
    val toStop: String,
    val totalFare: String,
    val txnDateTime: String,
    @PrimaryKey(autoGenerate = false)
    val txnID: String,
    val txnStatus: String,
    val unitFare: String,
    val usedOn: String,
    val validFrom: String,
    val validTill: String
)

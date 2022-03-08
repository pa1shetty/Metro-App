package com.example.nammametromvvm.utility


enum class StatusEnum(val statusReturn: Int) {
    SUCCESS(0),
    OTP_MISMATCH(1),
    OTP_TIME_OUT(2),
    ERROR(-1),
    NO_INTERNET(-2),
}

enum class TicketType(val ticketType: Int) {
    PENDING(1),
    FAILED(2),
    UNUSED(4),
    CANCELLED(5),
    USED(7),
    EXPIRED(8)
}
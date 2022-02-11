package com.example.nammametromvvm.utility


enum class StatusEnum(val statusReturn: Int) {
    SUCCESS(0),
    OTP_MISMATCH(1),
    OTP_TIME_OUT(2),
    ERROR(-1),
    NO_INTERNET(-2),
}
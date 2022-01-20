package com.example.nammametromvvm.ui.splashscreen.enumReturn

class SplashScreenEnum {
    enum class UpdateEnum(val update: Int) {
        NO_UPDATE(0),
        OPTIONAL(1),
        MANDATORY(2),
        ERROR(3),
    }
    enum class ConfigEnum(val configReturn: Int) {
        UPDATED(0),
        UP_TO_DATE(1),
        NO_INTERNET(2),
        ERROR_BUT_PROCEED(3),
        ERROR(4),
    }
}
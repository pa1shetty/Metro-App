@file:Suppress("unused")

package com.example.nammametromvvm.utility

import com.example.nammametromvvm.data.repositaries.entites.Config
import com.example.nammametromvvm.utility.date.DateDifferenceTypeEnum

object AppConstants {
    const val FILE_EXTENSION = ".log"

    @Suppress("unused")
    const val NO_OF_FILES = 10
    const val MAX_FILE_SIZE = 1024 * 1024 * 2 //2 MB
    const val LOG_FOLDER_NAME = "Logs"

    const val FILE_NAME = "NAMMA_METRO_LOGS"
    lateinit var INTERNAL_LOG_PATH: String
    const val dataStoreName = "user_specific_details"
    const val dataStoreDefaultValue = "none"
    const val defaultThemeValue = "System"
    const val defaultLanguageValue = ""

    const val defaultModifiedOn = "NA"
    lateinit var configurations: List<Config>

    const val appUpdateCheckTime = 24
    val appUpdateTimeDifferenceType = DateDifferenceTypeEnum.HOURS.DifferenceType




}
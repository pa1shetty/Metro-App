package com.example.nammametromvvm.utility

import com.example.nammametromvvm.utility.logs.Logs

object AppConstants {
    var logs: Logs? =null;
    const val STORAGE_REQUEST = 101
    const val FILE_EXTENSION = ".log"
    const val NO_OF_FILES = 10
    const val MAX_FILE_SIZE = 1024 * 1024 * 2 //2 MB
    const val FOLDER_NAME = "Namma Metro"
    const val LOG_FOLDER_NAME = "Logs"

    const val FILE_NAME = "NAMMA_METRO_LOGS"
    var INTERNAL_LOG_PATH:String = "";
    const val dataStoreName = "user_specific_details"
    const val dataStoreDefaultValue = "none"

}
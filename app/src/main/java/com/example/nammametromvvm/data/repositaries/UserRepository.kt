package com.example.mymvvmsample.data.repositaries

import com.example.mymvvmsample.data.network.MyApi
import com.example.mymvvmsample.data.network.SafeApiRequest
import com.example.nammametromvvm.BuildConfig
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.reqCheckForUpdate
import com.google.gson.JsonObject
import org.json.JSONObject

class UserRepository(
    private val api: MyApi
) : SafeApiRequest() {




    suspend fun checkForUpdate(): String {
        return apiRequest { api.checkForUpdate(reqCheckForUpdate, BuildConfig.VERSION_NAME) }
        //return "{\"status\":\"OK\",\"msg\":\"\",\"data\":{\"new_version\":\"1.9\",\"upgrade_flag\":\"1\"}}\n"
    }

}
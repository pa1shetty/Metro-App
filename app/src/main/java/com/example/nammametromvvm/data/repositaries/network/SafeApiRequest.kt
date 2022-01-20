package com.example.mymvvmsample.data.network

import com.example.nammametromvvm.data.repositaries.network.ErrorException
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.okLbl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.errorCodeLbl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.messageLbl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.msgLbl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.statusLbl
import com.example.nammametromvvm.utility.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response


abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): String {
        val response = call.invoke()
        if (response.isSuccessful) {
            val jsonObject = JSONObject(response.body().toString())
            if (jsonObject.getString(statusLbl).equals(okLbl)) {
                return response.body().toString()
            } else {
                if (jsonObject.has(errorCodeLbl)) {
                    throw ErrorException(
                        jsonObject.getString(msgLbl),
                        jsonObject.getInt(errorCodeLbl)
                    )
                } else {
                    throw ErrorException(
                        jsonObject.getString(msgLbl),
                    )
                }
            }
        } else {
            val message = StringBuffer()
            val error = response.errorBody()?.toString()
            error?.let {
                try {
                    message.append(JSONObject(it).getString(messageLbl))
                    message.append("\n")
                } catch (e: JSONException) {
                }
            }
            message.append("Error code: ${response.code()}")
            throw ApiException(message.toString())
        }
    }


}
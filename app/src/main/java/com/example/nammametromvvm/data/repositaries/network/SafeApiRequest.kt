package com.example.mymvvmsample.data.network

import android.util.Log
import com.example.nammametromvvm.utility.ApiError
import com.example.nammametromvvm.utility.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response


abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): JSONObject {
        val response = call.invoke()
        if (response.isSuccessful) {
            val jsonObject = JSONObject(response.body().toString())
            if (jsonObject.getString("status").equals("OK")) {
                return jsonObject
            } else {
                Log.d("test145", "apiRequest: " + response.body())
                throw ApiError(jsonObject.getString("msg"))
            }

        } else {
            val message = StringBuffer()
            val error = response.errorBody()?.toString()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                    message.append("\n")
                } catch (e: JSONException) {
                }
            }
            message.append("Error code: ${response.code()}")
            throw ApiException(message.toString())
        }
    }


}
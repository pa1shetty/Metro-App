package com.example.nammametromvvm.data.repositaries.network

import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.CONNECTION_TIME_OUT
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.READ_TIME_OUT
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.cTokenLbl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.getBaseUrl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.modifiedOnLbl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.mserver
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.otpLbl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.phoneNumberLbl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.requestTypeLbl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.typeIdLbl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.versionLbl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface MyApi {
    @Suppress("unused")
    @POST(mserver)
    suspend fun checkForUpdate(
        @Query(requestTypeLbl) requestType: String,
        @Query(versionLbl) splTkn: String,
    ): Response<JsonObject>

    @POST(mserver)
    suspend fun commonDownload(
        @Query(requestTypeLbl) requestType: String,
        @Query(typeIdLbl) downloadType: String,
        @Query(modifiedOnLbl) modifiedOn: String,
    ): Response<JsonObject>

    @POST(mserver)
    suspend fun requestForOtp(
        @Query(requestTypeLbl) requestType: String,
        @Query(phoneNumberLbl) phoneNumber: String,
    ): Response<JsonObject>

    @POST(mserver)
    suspend fun verifyOtp(
        @Query(requestTypeLbl) requestType: String,
        @Query(otpLbl) otp: String,
        @Query(cTokenLbl) cToken: String
    ): Response<JsonObject>


    companion object {
        var gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {
            val okHttpClient: OkHttpClient.Builder =
                OkHttpClient.Builder().addInterceptor(networkConnectionInterceptor)
            okHttpClient.connectTimeout(CONNECTION_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)
            okHttpClient.readTimeout(READ_TIME_OUT.toLong(), TimeUnit.MILLISECONDS)

            return Retrofit.Builder()
                .client(okHttpClient.build())
                .baseUrl(getBaseUrl(BaseUrlTypeEnum.ONE_NOT_ONE.baseUrlType))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(MyApi::class.java)
        }
    }
}

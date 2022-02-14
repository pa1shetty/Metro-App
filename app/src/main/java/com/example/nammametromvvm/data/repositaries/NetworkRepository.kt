package com.example.nammametromvvm.data.repositaries

import android.util.Log
import com.example.nammametromvvm.data.repositaries.network.responses.appUpdate.Update
import com.example.nammametromvvm.data.repositaries.network.SafeApiRequest
import com.example.nammametromvvm.data.repositaries.datastore.DataStoreRepository
import com.example.nammametromvvm.data.repositaries.entites.Config
import com.example.nammametromvvm.data.repositaries.network.MyApi
import com.example.nammametromvvm.data.repositaries.network.MyApi.Companion.gson
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.configLbl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.dataLbl
import com.example.nammametromvvm.data.repositaries.network.NetworkConstants.modifiedOnLbl
import com.example.nammametromvvm.data.repositaries.network.RequestTypeEnum
import com.example.nammametromvvm.data.repositaries.network.responses.login.getOtp.GetOtp
import com.example.nammametromvvm.data.repositaries.network.responses.login.otpVerification.OtpVerification
import com.example.nammametromvvm.data.repositaries.network.responses.login.otpVerification.OtpVerificationData
import com.example.nammametromvvm.utility.Configurations
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val api: MyApi,
    private val dataBaseRepository: DataBaseRepository,
    private val dataStoreRepository: DataStoreRepository,
    @Suppress("unused")
    private val configurations: Configurations
) : SafeApiRequest() {


    fun checkForUpdate(): Update {
        val test =
            "{\"status\":\"OK\",\"msg\":\"\",\"data\":{\"new_version\":\"1.9\",\"upgrade_flag\":\"1\"}}\n"
        /*return gson.fromJson(
            apiRequest { api.checkForUpdate(RequestTypeEnum.CheckForUpdate.requestType, BuildConfig.VERSION_NAME) },
            Update::class.java
        )*/

        return gson.fromJson(
            test,
            Update::class.java
        )
    }

    @Suppress("unused")
    private suspend fun commonDownload(downloadType: String, modifiedOn: String): String {
        Log.d("test155", "commonDownload: $downloadType $modifiedOn")
        return apiRequest {
            api.commonDownload(
                RequestTypeEnum.Download.requestType,
                downloadType,
                modifiedOn
            )
        }
    }

    suspend fun configDownload() {
        Log.d("test45", "configDownload: ")
        //31122021125150
        /*return  gson.fromJson(
            commonDownload(DownloadTypeEnum.Configuration.downloadType,dataStoreRepository.getConfigLastModifiedOn()),
            ConfigDownloadResponse::class.java
        )*/

        val test = "{\n" +
                "  \"status\": \"OK\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\": {\n" +
                "    \"modified_on\": \"31122021125200\",\n" +
                "    \"config\": {\n" +
                "      \"max_passenger_count\": \"16\",\n" +
                "      \"qr_update_seconds\": \"180\",\n" +
                "      \"ticket_prior_days\": \"7\",\n" +
                "      \"max_recent_tickets\": \"15\",\n" +
                "      \"max_active_tickets\": \"10\",\n" +
                "      \"max_phone_number\": \"11\",\n" +
                "      \"max_otp_length\": \"6\",\n" +
                "      \"resend_wait_second\": \"10\",\n" +
                "      \"login_mandatory\": \"0\",\n" +
                "      \"max_suggestion_routes\": \"3\"\n" +
                "    }\n" +
                "  }\n" +
                "}"


        val configObject = JSONObject(test).getJSONObject(dataLbl).getJSONObject(configLbl)
        val configList: ArrayList<Config> = ArrayList()
        for (i in 0 until Objects.requireNonNull(configObject.names()).length()) {
            val config = Config()
            config.configName = Objects.requireNonNull(configObject.names()).getString(i)
            config.configValue =
                configObject[Objects.requireNonNull(configObject.names()).getString(i)] as String
            configList.add(config)
        }
        dataBaseRepository.saveConfig(configList)
        dataStoreRepository.saveConfigLastModifiedOn(
            JSONObject(test).getJSONObject(dataLbl).getString(modifiedOnLbl)
        )
    }

    suspend fun requestForOtp(phoneNumber: String): GetOtp {
        return gson.fromJson(apiRequest {
            api.requestForOtp(
                RequestTypeEnum.Regiester.requestType,
                phoneNumber,
            )
        }, GetOtp::class.java)
    }

    suspend fun verifyOtp(otp: String): OtpVerificationData {
        return gson.fromJson(apiRequest {
            api.verifyOtp(
                RequestTypeEnum.VerifyOtp.requestType,
                otp,
                dataStoreRepository.getCToken()
            )
        }, OtpVerification::class.java).data
    }

}
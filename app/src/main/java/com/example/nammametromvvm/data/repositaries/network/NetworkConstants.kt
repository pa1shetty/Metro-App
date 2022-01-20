package com.example.nammametromvvm.data.repositaries.network

object NetworkConstants {

    const val CONNECTION_TIME_OUT = 1000 * 4
    const val READ_TIME_OUT = 1000 * 12


    const val requestTypeLbl = "requestType"
    const val statusLbl = "status"
    const val versionLbl = "version"
    const val okLbl = "OK"
    const val errorCodeLbl = "errorCode"
    const val messageLbl = "message"
    const val msgLbl = "msg"
    const val dataLbl = "data"
    const val configLbl = "config"



    const val modifiedOnLbl = "modified_on"
    const val typeIdLbl = "type_id"
    fun getBaseUrl(urlType: Int = BaseUrlTypeEnum.Uat.baseUrlType): String {
        return when (urlType) {
            BaseUrlTypeEnum.PavanLocal.baseUrlType -> "http://10.8.0.10:8084//bwi_mobile/"
            else -> {
                "http://uatmetrolocal.auminfotech.in:7004/bwi_mobile/"
            }
        }
    }

}

enum class RequestTypeEnum(val requestType: String) {
    CheckForUpdate("115"),
    Download("113"),
}

enum class DownloadTypeEnum(val downloadType: String) {
    Configuration("3"),
}

enum class BaseUrlTypeEnum(val baseUrlType: Int) {
    Uat(0),
    PavanLocal(1),
}
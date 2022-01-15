

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("errorCode")
    val errorCode: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)
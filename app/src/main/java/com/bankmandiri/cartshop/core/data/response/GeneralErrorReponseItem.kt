package com.bankmandiri.cartshop.core.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class GeneralErrorReponseItem(
    @SerializedName("error")
    var error: String?,
    @SerializedName("path")
    var path: String?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("timestamp")
    var timestamp: String?
)
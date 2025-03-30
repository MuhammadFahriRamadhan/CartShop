package com.bankmandiri.cartshop.core.data.request


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class LoginRequest(
    @SerializedName("password")
    var password: String?,
    @SerializedName("username")
    var username: String?
)
package com.bankmandiri.cartshop.core.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.bankmandiri.cartshop.core.domain.model.Login

@Keep
data class LoginResponse(
    @SerializedName("token")
    var token: String?
) {
    fun toLogin() : Login {
        return Login(token)
    }
}
package com.bankmandiri.cartshop.core.data.response

import com.google.gson.annotations.SerializedName

open class Response<T>(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("message")
    val message : String?,
    @SerializedName("data")
    val data : T)
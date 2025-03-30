package com.bankmandiri.cartshop.core.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Rating(
    @SerializedName("count")
    var count: Int?,
    @SerializedName("rate")
    var rate: Double?
)
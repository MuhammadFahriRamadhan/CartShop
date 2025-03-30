package com.bankmandiri.cartshop.core.data.response

import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val id: Int,
    val name: String,
    val price: Int,
    val image: String,
    val description: String,
    val category: String
)
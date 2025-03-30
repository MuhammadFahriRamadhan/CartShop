package com.bankmandiri.cartshop.core.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.bankmandiri.cartshop.core.data.response.Rating
import com.bankmandiri.cartshop.core.local.ProductCartEntity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Product(
    val id: Int? = 0,
    val name: String? = "",
    val price: Double? = 0.0,
    val image: String? = "",
    val description: String? = "",
    val category: String? = ""
) : Parcelable
package com.bankmandiri.cartshop.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCart (
    val id: Int? = 0,
    val name: String? = "",
    val price: Double? = 0.0,
    val image: String? = "",
    val description: String? = "",
    val category: String? = "",
    var quantity : Int? = 0
) : Parcelable
package com.bankmandiri.cartshop.core.util

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(price: Double?): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    return formatter.format(price)
}
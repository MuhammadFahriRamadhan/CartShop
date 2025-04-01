package com.bankmandiri.cartshop.core.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class ProductCartEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double,
    val image: String,
    val description: String,
    val category: String,
    var quantity : Int,
    var isPaid : Boolean = false,
    var userId : Int
)
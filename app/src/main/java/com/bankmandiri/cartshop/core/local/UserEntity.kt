package com.bankmandiri.cartshop.core.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val password: String
)
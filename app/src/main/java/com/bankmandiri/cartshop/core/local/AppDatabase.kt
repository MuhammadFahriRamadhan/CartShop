package com.bankmandiri.cartshop.core.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class,ProductCartEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract  fun ProductCartDao() : ProductCartDao
}
package com.bankmandiri.cartshop.core.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bankmandiri.cartshop.core.domain.model.Login

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user : UserEntity) : Long?


    @Query("SELECT * FROM users WHERE name = :userName")
    suspend fun getUser(userName : String): UserEntity?
}
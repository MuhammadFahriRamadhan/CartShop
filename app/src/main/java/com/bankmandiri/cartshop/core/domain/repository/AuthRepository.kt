package com.bankmandiri.cartshop.core.domain.repository

import com.bankmandiri.cartshop.core.data.request.LoginRequest
import com.bankmandiri.cartshop.core.data.response.LoginResponse
import com.bankmandiri.cartshop.core.domain.model.Login
import com.bankmandiri.cartshop.core.local.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
     suspend fun loginUser(loginRequest: LoginRequest) : Flow<Login>
     suspend fun saveUserLogin(username : String,password : String)
     suspend fun findUser(username : String,password : String) : Flow<UserEntity?>
}
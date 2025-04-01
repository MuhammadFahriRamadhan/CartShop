package com.bankmandiri.cartshop.core.data.repositoryimpl

import com.bankmandiri.cartshop.core.data.remote.ApiService
import com.bankmandiri.cartshop.core.data.request.LoginRequest
import com.bankmandiri.cartshop.core.domain.model.Login
import com.bankmandiri.cartshop.core.domain.repository.AuthRepository
import com.bankmandiri.cartshop.core.exception.Failure
import com.bankmandiri.cartshop.core.local.UserDao
import com.bankmandiri.cartshop.core.local.UserEntity
import com.bankmandiri.cartshop.core.local.manager.SpManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit

class AuthRepositoryImpl(private val apiService : ApiService,private val userDao: UserDao,private val spManager : SpManager) : AuthRepository  {
    override suspend fun loginUser(loginRequest: LoginRequest): Flow<Login> {
        return flow {
            try {
                val response = apiService.loginUser(loginRequest)
                if (response.token?.isNotEmpty() == true){
                    emit(response.toLogin())
                }
            } catch (e: Exception) {
                throw Throwable(e.message)
            }
        }
    }

    override suspend fun saveUserLogin(username: String, password: String) {
        userDao.insertUser(UserEntity(name = username, password = password))
    }

    override suspend fun findUser(username: String, password: String) : Flow<UserEntity?> {
        return  flow {
            try {
                val user  = userDao.getUser(username)
                 if (user == null) {
                      userDao.insertUser(UserEntity(name = username, password = password))
                     val userFind = userDao.getUser(username)
                     spManager.setUserId(userFind?.id ?: 0)
                     emit(UserEntity(name = username, password = password))
                } else if (user.password != password) {
                     throw Throwable("Invalid password")
                } else {
                     spManager.setUserId(user.id)
                     emit(user)
                }
            } catch (e: Exception) {
                throw Throwable(e.message)
            }
        }

    }
}
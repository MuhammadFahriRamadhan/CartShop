package com.bankmandiri.cartshop.core.data.remote

import com.bankmandiri.cartshop.core.data.request.LoginRequest
import com.bankmandiri.cartshop.core.data.response.LoginResponse
import com.bankmandiri.cartshop.core.data.response.ProductListResponseItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {
    @POST("/auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest) : LoginResponse

    @GET("/products")
    suspend fun getProducts() : List<ProductListResponseItem>
}
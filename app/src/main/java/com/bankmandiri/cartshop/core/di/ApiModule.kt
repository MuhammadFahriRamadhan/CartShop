package com.bankmandiri.cartshop.core.di

import com.bankmandiri.cartshop.core.data.remote.ApiService
import retrofit2.Retrofit

class ApiModule {
    companion object {
        fun provideApiService(retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}
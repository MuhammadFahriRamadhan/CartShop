package com.bankmandiri.cartshop

import android.app.Application
import com.bankmandiri.cartshop.core.di.apiModule
import com.bankmandiri.cartshop.core.di.cacheModule
import com.bankmandiri.cartshop.core.di.databaseModule
import com.bankmandiri.cartshop.core.di.networkModule
import com.bankmandiri.cartshop.core.di.repoModule
import com.bankmandiri.cartshop.core.di.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CartShopApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CartShopApp)
            modules(listOf(networkModule,databaseModule,cacheModule,apiModule,repoModule,viewModel))
        }
    }
}
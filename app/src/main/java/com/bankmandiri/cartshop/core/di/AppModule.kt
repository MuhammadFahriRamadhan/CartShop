package com.bankmandiri.cartshop.core.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.bankmandiri.cartshop.core.data.repositoryimpl.AuthRepositoryImpl
import com.bankmandiri.cartshop.core.data.repositoryimpl.ProductRepositoryImpl
import com.bankmandiri.cartshop.core.domain.repository.AuthRepository
import com.bankmandiri.cartshop.core.domain.repository.ProductRepository
import com.bankmandiri.cartshop.core.local.AppDatabase
import com.bankmandiri.cartshop.core.local.manager.SpManager
import com.bankmandiri.cartshop.core.local.manager.SpManagerImpl
import com.bankmandiri.cartshop.ui.login.LoginViewModel
import com.bankmandiri.cartshop.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module {
    single { ApiModule.provideApiService(get()) }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(get<Application>(), AppDatabase::class.java, "app-database").fallbackToDestructiveMigration().build()
    }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().ProductCartDao() }
}

val cacheModule = module {
    single<SpManager> { SpManagerImpl(get()) }
    single { androidContext().getSharedPreferences(SpManagerImpl.PREFS_NAME, Context.MODE_PRIVATE) }
}

val networkModule = module {
    single { NetworkModule.provideOkhttpClient(get()) }
    single { NetworkModule.provideRetrofitService(get()) }
    single { NetworkModule.httpInterceptor() }
}

 val repoModule = module {
    single <AuthRepository> { AuthRepositoryImpl(get(),get(),get()) }
    single <ProductRepository> { ProductRepositoryImpl(get(),get(),get()) }
 }

val viewModel = module {
    viewModel { LoginViewModel() }
    viewModel { MainViewModel() }
}


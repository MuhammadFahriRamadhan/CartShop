package com.bankmandiri.cartshop.core.local.manager


interface SpManager {
    fun setIsLogin(status : Boolean)
    fun getIsLogin() : Boolean
    fun setUsername(name : String)
    fun getUsername() : String?
    fun invalidate()
}
package com.bankmandiri.cartshop.core.local.manager

import android.content.SharedPreferences
import com.bankmandiri.cartshop.core.util.update
import com.google.gson.Gson


class SpManagerImpl(private val sharedPreferences: SharedPreferences) : SpManager {

    companion object {
        const val PREFS_NAME = "sp_cart_shop"
        const val LOGIN_STATUS = "login_status"
        const val SET_USER_NAME = "user_name"
        const val SET_USER_ID = "user_id"
    }
    private val gson = Gson()

    override fun setIsLogin(status: Boolean) {
       sharedPreferences.update(status to LOGIN_STATUS)
    }

    override fun getIsLogin(): Boolean {
       return sharedPreferences.getBoolean(LOGIN_STATUS,false)
    }

    override fun setUsername(name: String) {
        sharedPreferences.update(name to SET_USER_NAME)
    }

    override fun setUserId(userId: Int) {
        sharedPreferences.update(userId to SET_USER_ID)
    }

    override fun getUserId(): Int {
       return  sharedPreferences.getInt(SET_USER_ID,0)
    }

    override fun getUsername(): String? {
        return sharedPreferences.getString(SET_USER_NAME,"")
    }

    override fun invalidate() {
        sharedPreferences.update( false to LOGIN_STATUS)
        sharedPreferences.update("" to SET_USER_NAME)
        sharedPreferences.update(0 to SET_USER_ID)
    }


}
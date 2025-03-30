package com.bankmandiri.cartshop.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bankmandiri.cartshop.core.base.BaseViewModel
import com.bankmandiri.cartshop.core.data.request.LoginRequest
import com.bankmandiri.cartshop.core.domain.repository.AuthRepository
import com.bankmandiri.cartshop.core.local.manager.SpManager
import com.bankmandiri.cartshop.core.util.SingleLiveEvent
import com.bankmandiri.cartshop.core.util.getGeneralErrorServer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

class LoginViewModel : BaseViewModel(), KoinComponent {
    private val authRepository : AuthRepository = get()
    private val spManager : SpManager = get()
    val isLoginComplete  =  SingleLiveEvent<Boolean>()

    fun login(username : String, password : String) {
        viewModelScope.launch {
            safeScopeFun {
               handleFailure(it.getGeneralErrorServer())
            }.launch {
                authRepository.loginUser(LoginRequest(password, username))
                    .onStart { isLoadingLiveData.postValue(true) }
                    .collect{
                        isLoginComplete.postValue(true)
                        spManager.setIsLogin(true)
                        spManager.setUsername(username)
                        authRepository.saveUserLogin(username,password)
                    }
            }
        }
    }

    fun findUser(username : String, password : String){
        viewModelScope.launch {
            safeScopeFun {
                handleFailure(it.getGeneralErrorServer())
            }.launch {
                authRepository.findUser(username,password)
                    .onStart { isLoadingLiveData.postValue(true) }
                    .onCompletion { isLoadingLiveData.postValue(false) }
                    .collect{
                        isLoginComplete.postValue(true)
                        spManager.setIsLogin(true)
                        spManager.setUsername(username)
                    }
            }
        }
    }

}
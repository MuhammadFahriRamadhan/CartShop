package com.bankmandiri.cartshop.ui.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bankmandiri.cartshop.core.base.BaseViewModel
import com.bankmandiri.cartshop.core.di.viewModel
import com.bankmandiri.cartshop.core.domain.model.Product
import com.bankmandiri.cartshop.core.domain.model.ProductCart
import com.bankmandiri.cartshop.core.domain.repository.ProductRepository
import com.bankmandiri.cartshop.core.local.ProductCartEntity
import com.bankmandiri.cartshop.core.local.manager.SpManager
import com.bankmandiri.cartshop.core.util.SingleLiveEvent
import com.bankmandiri.cartshop.core.util.getGeneralErrorServer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MainViewModel : BaseViewModel(), KoinComponent {

    private val spManager : SpManager = get()
    private val productRepository : ProductRepository = get()
    private var productsData = mutableListOf<Product>()
    val productsLiveEvent: MutableSharedFlow<List<Product>?> = MutableSharedFlow(replay = 1)
    val isCheckoutSuccessEvent : MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 1)
    val productCartLiveEvent : MutableSharedFlow<List<ProductCartEntity?>> = MutableSharedFlow(replay = 1)
    fun islogin() = spManager.getIsLogin()
    fun getUserName() = spManager.getUsername()


    fun getProducts() {
        viewModelScope.launch {
            safeScopeFun {
                handleFailure(it.getGeneralErrorServer())
            }.launch {
                productRepository.getProducts()
                    .onStart { isLoadingLiveData.postValue(true) }
                    .onCompletion { isLoadingLiveData.postValue(false) }
                    .collect{ products ->
                        productsData.clear()
                        productsData.add(Product(category = "Semua"))
                        if (products != null) {
                            productsData.addAll(products)
                        }
                        productsLiveEvent.tryEmit(productsData)
                    }
            }
        }
    }

    fun getProductCartList() {
        viewModelScope.launch {
            safeScopeFun {
                handleFailure(it.getGeneralErrorServer())
            }.launch {
                productRepository.getProductCarts().collect{
                    productCartLiveEvent.emit(it.map { it?.copy() }.toList())
                    it.map {
                        Log.i("TAGEDS", "updateProduct:getProductCarts "+it?.quantity)
                    }

                }
            }
        }
    }

    fun updateProduct(product: Product,quantity : Int){
        viewModelScope.launch {
            safeScopeFun {
                getProductCartList()
                handleFailure(it.getGeneralErrorServer())
                Log.i("TAGEDS", "updateProduct: ")
            }.launch {
                val productEntity = ProductCartEntity(
                    id = product.id ?: 0,
                    name = product.name.orEmpty(),
                    price = product.price ?: 0.0,
                    image =  product.image.orEmpty(),
                    description = product.description.orEmpty(),
                    category = product.category.orEmpty(),
                    quantity = quantity,
                )
                productRepository.addedProductCart(productEntity)
            }
        }
    }

    fun updateProductCart(productEntity: ProductCartEntity?){
        viewModelScope.launch {
            safeScopeFun {
                handleFailure(it.getGeneralErrorServer())
            }.launch {
                if (productEntity != null) {
                    productRepository.updateProductCart(productEntity).collect{
                        getProductCartList()
                    }
                }
            }
        }
    }

    fun deleteTransaction(id : Int){
        viewModelScope.launch {
            safeScopeFun {
                handleFailure(it.getGeneralErrorServer())
            }.launch {
                productRepository.deleteTransaction(id).collect{
                    getProductCartList()
                }
            }
        }
    }

    fun checkoutTransaction(status : Boolean){
        viewModelScope.launch {
            safeScopeFun {
                handleFailure(it.getGeneralErrorServer())
            }.launch {
                productRepository.checkoutTransaction(status).collect{
                    isCheckoutSuccessEvent.emit(it)
                }
            }
        }
    }

    fun setupLogout() {
        spManager.invalidate()
    }

}
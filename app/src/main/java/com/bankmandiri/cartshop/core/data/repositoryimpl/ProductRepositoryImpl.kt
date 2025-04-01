package com.bankmandiri.cartshop.core.data.repositoryimpl

import com.bankmandiri.cartshop.core.data.remote.ApiService
import com.bankmandiri.cartshop.core.domain.model.Product
import com.bankmandiri.cartshop.core.domain.repository.ProductRepository
import com.bankmandiri.cartshop.core.local.ProductCartDao
import com.bankmandiri.cartshop.core.local.ProductCartEntity
import com.bankmandiri.cartshop.core.local.manager.SpManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepositoryImpl(private val apiService: ApiService,private val productCartDao: ProductCartDao,val spManager: SpManager) : ProductRepository {

    override suspend fun getProducts(): Flow<List<Product>?> {
        return flow {
            try {
                val response = apiService.getProducts()
                if (response.isNotEmpty()){
                    emit(response.map { it.toProduct() })
                }else{
                    throw Throwable("Product is empty")
                }
            }catch (e : Exception){
                throw Throwable(e.message)
            }
        }
    }

    override suspend fun insertProductCart(productCart: ProductCartEntity) {
        try {
            productCartDao.insertProductCart(productCart)
        }catch (e : Exception){
            throw Throwable(e.message)
        }
    }

    override suspend fun updateProductCart(productCart: ProductCartEntity) : Flow<Boolean>{
       return flow {
           try {
               val productEntity = productCartDao.findProductCart(productCart.id,spManager.getUserId())
               if (productEntity == null){
                   productCartDao.insertProductCart(productCart)
               }else {
                   productCartDao.updateProductCart(productCart.id,productCart.quantity,spManager.getUserId())
               }
               emit(true)
           }catch (e : Exception){
               throw Throwable(e.message)
           }
       }
    }

    override suspend fun addedProductCart(productCart: ProductCartEntity) {
        try {
            val productEntity = productCartDao.findProductCart(productCart.id,spManager.getUserId())
            if (productEntity == null){
                productCartDao.insertProductCart(productCart)
            }else {
                val updateQuantity = productCart.quantity + productEntity.quantity
                productCartDao.updateProductCart(productCart.id,updateQuantity,spManager.getUserId())
            }
            throw  Throwable("update data added")
        }catch (e : Exception){
            throw Throwable(e.message)
        }
    }

    override suspend fun getProductCarts(id : Int): Flow<List<ProductCartEntity?>> {
       return flow {
           try {
             val productsCart =  productCartDao.getProductCarts(id)
             if (productsCart.isNotEmpty() ){
                emit(productsCart)
             }else{
                 throw Throwable("refresh")
             }
           }catch (e : Exception){
               throw Throwable(e.message)
           }
       }
    }

    override suspend fun deleteTransaction(id: Int) : Flow<Boolean> {
        return flow {
            try {
                val rowsUpdated = productCartDao.deleteTransaction(id)
                emit(true)
            }catch (e : Exception){
                throw Throwable(e.message)
            }
        }
    }

    override suspend fun checkoutTransaction(paid: Boolean): Flow<Boolean> {
       return flow {
           try {
               val rowsUpdated = productCartDao.checkoutTransaction(paid)
               if (rowsUpdated > 0) {
                   // Success: Callback or further processing
                 emit(true)
               } else {
                   // Failed: No rows were updated
                  emit(false)
               }
           }catch (e : Exception){
               throw Throwable(e.message)
           }
       }
    }


}
package com.bankmandiri.cartshop.core.domain.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bankmandiri.cartshop.core.data.response.ProductListResponseItem
import com.bankmandiri.cartshop.core.domain.model.Product
import com.bankmandiri.cartshop.core.local.ProductCartEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts() : Flow<List<Product>?>
    suspend fun insertProductCart(productCart : ProductCartEntity)
    suspend fun updateProductCart(productCart: ProductCartEntity) : Flow<Boolean>
    suspend fun addedProductCart(productCart: ProductCartEntity)
    suspend fun getProductCarts(): Flow<List<ProductCartEntity?>>
    suspend fun deleteTransaction(id: Int) : Flow<Boolean>
    suspend fun checkoutTransaction(paid: Boolean): Flow<Boolean>
}
package com.bankmandiri.cartshop.core.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductCartDao {
    @Insert
    suspend fun insertProductCart(productCart : ProductCartEntity)

    @Query("SELECT * FROM transactions WHERE id = :id AND isPaid = 0 AND userId = :userId")
    suspend fun findProductCart(id : Int,userId : Int): ProductCartEntity?

    @Query("UPDATE transactions SET quantity = :increment WHERE id = :id AND isPaid = 0 AND userId = :userId")
    suspend fun updateProductCart(id: Int, increment: Int,userId: Int)

    @Query("SELECT * FROM transactions WHERE isPaid = 0 AND userId= :id")
    suspend fun getProductCarts(id : Int): List<ProductCartEntity?>

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransaction(id: Int): Int

    @Query("UPDATE transactions SET isPaid = :paid")
    suspend fun checkoutTransaction(paid: Boolean): Int

}
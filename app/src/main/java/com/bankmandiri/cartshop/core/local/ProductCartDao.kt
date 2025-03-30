package com.bankmandiri.cartshop.core.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductCartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductCart(productCart : ProductCartEntity)

    @Query("SELECT * FROM transactions WHERE id = :id AND isPaid = 0")
    suspend fun findProductCart(id : Int): ProductCartEntity?

    @Query("UPDATE transactions SET quantity = :increment WHERE id = :id AND isPaid = 0")
    suspend fun updateProductCart(id: Int, increment: Int)

    @Query("SELECT * FROM transactions WHERE isPaid = 0")
    suspend fun getProductCarts(): List<ProductCartEntity?>

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransaction(id: Int): Int

    @Query("UPDATE transactions SET isPaid = :paid")
    suspend fun checkoutTransaction(paid: Boolean): Int

}
package com.bankmandiri.cartshop.core.data.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.bankmandiri.cartshop.core.domain.model.Product

@Keep
data class ProductListResponseItem(
    @SerializedName("category")
    var category: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("price")
    var price: Double?,
    @SerializedName("rating")
    var rating: Rating?,
    @SerializedName("title")
    var title: String?
) {
    fun toProduct() : Product {
        return Product(
           category = category,
           description =  description,
           id = id,
           image = image,
           price =  price,
           name = title
        )
    }
}
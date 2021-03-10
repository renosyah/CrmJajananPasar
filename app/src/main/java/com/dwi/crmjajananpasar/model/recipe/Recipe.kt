package com.dwi.crmjajananpasar.model.recipe

import com.dwi.crmjajananpasar.model.BaseModel
import com.dwi.crmjajananpasar.model.product.Product
import com.google.gson.annotations.SerializedName

class Recipe (
    @SerializedName("id")
    var id:Int = 0,

    @SerializedName("product_id")
    var productId : Int = 0,

    @SerializedName("product")
    var product: Product = Product()

) : BaseModel {
    fun clone(): Recipe {
        return Recipe(
            this.id,
            this.productId,
            this.product.clone()
        )
    }
}
package com.dwi.crmjajananpasar.model.product

import com.dwi.crmjajananpasar.model.BaseModel
import com.google.gson.annotations.SerializedName

class Product(
    @SerializedName("id")
    var id:Int = 0,

    @SerializedName("category_id")
    var categoryId : Int = 0,

    @SerializedName("name")
    var name : String = "",

    @SerializedName("price")
    var price : Int = 0,

    @SerializedName("stock")
    var stock : Int = 0,

    @SerializedName("rating")
    var rating : Int = 0,

    @SerializedName("image_url")
    var imageUrl : String = "",

    @SerializedName("detail")
    var detail : String = "",

    @SerializedName("product_type")
    var productType : Int = 0,

    @SerializedName("default_qty")
    var defaultQty : Int = 0,

    @SerializedName("exp_date")
    var expDate : String = ""

) : BaseModel {
    fun clone() : Product {
        return Product(
            this.id,
            this.categoryId,
            this.name,
            this.price,
            this.stock,
            this.rating,
            this.imageUrl,
            this.detail,
            this.productType,
            this.defaultQty,
            this.expDate
        )
    }
}
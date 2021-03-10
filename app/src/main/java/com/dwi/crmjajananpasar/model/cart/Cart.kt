package com.dwi.crmjajananpasar.model.cart

import com.dwi.crmjajananpasar.model.BaseModel
import com.dwi.crmjajananpasar.model.product.Product
import com.google.gson.annotations.SerializedName


class Cart(
    @SerializedName("id")
    var id:Int = 0,

    @SerializedName("customer_id")
    var customerId : Int = 0,

    @SerializedName("product_id")
    var productId : Int = 0,

    @SerializedName("quantity")
    var quantity : Int = 0,

    @SerializedName("price")
    var price : Int = 0,

    @SerializedName("sub_total")
    var subTotal : Int = 0,

    @SerializedName("product")
    var product : Product = Product()

) : BaseModel {

    constructor(customerId : Int) : this() {
        this.customerId = customerId
    }

    fun clone() : Cart {
        return Cart(
            this.id,
            this.customerId,
            this.productId,
            this.quantity,
            this.price,
            this.subTotal,
            Product()
        )
    }
}
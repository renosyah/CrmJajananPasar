package com.dwi.crmjajananpasar.model.checkout

import com.dwi.crmjajananpasar.model.BaseModel
import com.google.gson.annotations.SerializedName

class Checkout (
    @SerializedName("customer_id")
    var customerId : Int = 0,

    @SerializedName("payment_id")
    var paymentId : Int = 0,

    @SerializedName("address")
    var address : String = "",

    @SerializedName("total")
    var total : Int = 0

) : BaseModel {

    fun clone() : Checkout {
        return Checkout(
            this.customerId,
            this.paymentId,
            this.address,
            this.total
        )
    }
}
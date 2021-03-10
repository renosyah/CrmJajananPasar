package com.dwi.crmjajananpasar.model.cart

import com.dwi.crmjajananpasar.model.BaseModel
import com.google.gson.annotations.SerializedName

class TotalCart(
    @SerializedName("item")
    var item : Int = 0,

    @SerializedName("total")
    var total : Int = 0

) : BaseModel {
    fun clone() : TotalCart{
        return TotalCart(
            this.item,
            this.total
        )
    }
}
package com.dwi.crmjajananpasar.model.payment

import com.dwi.crmjajananpasar.model.BaseModel
import com.google.gson.annotations.SerializedName

class Payment(
    @SerializedName("id")
     var id:Int = 0,

     @SerializedName("name")
     var name : String = "",

     @SerializedName("detail")
     var detail : String = "",

    var selected : Boolean = false

) : BaseModel {

    constructor(id: Int) : this() {
        this.id = id
    }

    fun clone() : Payment {
        return Payment(
            this.id,
            this.name,
            this.detail
        )
    }
}
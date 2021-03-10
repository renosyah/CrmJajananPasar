package com.dwi.crmjajananpasar.model.customer

import com.dwi.crmjajananpasar.model.BaseModel
import com.google.gson.annotations.SerializedName

class Customer(
    @SerializedName("id")
    var id : Int = 0,

    @SerializedName("name")
    var name : String = "",

    @SerializedName("username")
    var username : String = "",

    @SerializedName("email")
    var email : String = "",

    @SerializedName("password")
    var password : String = ""

) : BaseModel {

    fun clone() : Customer {
        return Customer(
            this.id,
            this.name,
            this.username,
            this.email,
            this.password
        )
    }
}
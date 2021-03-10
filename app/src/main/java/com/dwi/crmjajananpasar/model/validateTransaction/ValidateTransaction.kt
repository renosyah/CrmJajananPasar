package com.dwi.crmjajananpasar.model.validateTransaction

import com.dwi.crmjajananpasar.model.BaseModel
import com.google.gson.annotations.SerializedName

class ValidateTransaction(
    @SerializedName("id")
    var id : Int = 0,

    @SerializedName("transaction_id")
    var transactionId : Int = 0,

    @SerializedName("image_url")
    var imageUrl : String = ""

) : BaseModel {
    fun clone() : ValidateTransaction {
        return ValidateTransaction(
            this.id,
            this.transactionId,
            this.imageUrl
        )
    }
}
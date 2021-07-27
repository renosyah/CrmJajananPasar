package com.dwi.crmjajananpasar.model.transaction

import com.dwi.crmjajananpasar.model.BaseModel
import com.google.gson.annotations.SerializedName

class Transaction(
    @SerializedName("id")
    var id : Int = 0,

    @SerializedName("ref_id")
    var refId : String = "",

    @SerializedName("customer_id")
    var customerId : Int = 0,

    @SerializedName("payment_id")
    var paymentId : Int = 0,

    @SerializedName("address")
    var address : String= "",

    @SerializedName("shipment_fee")
    var shipmentFee : Int = 0,

    @SerializedName("total")
    var total : Int = 0,

    @SerializedName("expired_date")
    var expiredDate : String = "",

    @SerializedName("transaction_date")
    var transactionDate : String = ""


) : BaseModel {

    constructor(refId: String) : this() {
        this.refId = refId
    }

    fun clone() : Transaction {
        return Transaction(
            this.id,
            this.refId,
            this.customerId,
            this.paymentId,
            this.address,
            this.shipmentFee,
            this.total,
            this.expiredDate,
            this.transactionDate
        )
    }
}
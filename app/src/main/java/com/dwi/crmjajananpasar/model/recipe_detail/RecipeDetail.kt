package com.dwi.crmjajananpasar.model.recipe_detail

import com.dwi.crmjajananpasar.model.BaseModel
import com.google.gson.annotations.SerializedName

class RecipeDetail(
    @SerializedName("id")
    var id:Int = 0,

    @SerializedName("receipt_id")
    var receiptId : Int = 0,

    @SerializedName("step")
    var step: Int = 0,

    @SerializedName("text")
    var text: String = ""

) : BaseModel {
    fun clone(): RecipeDetail {
        return  RecipeDetail(
            this.id,
            this.receiptId,
            this.step,
            this.text
        )
    }
}
package com.dwi.crmjajananpasar.model.category

import com.dwi.crmjajananpasar.model.BaseModel
import com.google.gson.annotations.SerializedName

class Category(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var name : String = "",

    @SerializedName("image_url")
    var imageUrl : String = ""

) : BaseModel {
    fun clone() : Category {
        return Category(
            this.id,
            this.name,
            this.imageUrl
        )
    }
}
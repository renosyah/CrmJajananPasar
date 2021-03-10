package com.dwi.crmjajananpasar.model.uploadResponse

import com.dwi.crmjajananpasar.model.BaseModel
import com.google.gson.annotations.SerializedName

class UploadResponse(
    @SerializedName("file_name")
    var fileName : String = "",

    @SerializedName("url")
    var url  : String = ""

) : BaseModel {
    fun clone() : UploadResponse {
        return UploadResponse(
            this.fileName,
            this.url
        )
    }
}
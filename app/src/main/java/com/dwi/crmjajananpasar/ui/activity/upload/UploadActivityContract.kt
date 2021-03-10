package com.dwi.crmjajananpasar.ui.activity.upload


import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.uploadResponse.UploadResponse
import com.dwi.crmjajananpasar.model.validateTransaction.ValidateTransaction
import okhttp3.MultipartBody

class UploadActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onUploaded(uploadResponse: UploadResponse)
        fun showProgressUpload(show: Boolean)
        fun showErrorUpload(e: String)

        fun onValidated()
        fun showProgressValidate(show: Boolean)
        fun showErrorValidate(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun upload(file: MultipartBody.Part, enableLoading :Boolean)
        fun addValidateTransaction(validateTransaction: ValidateTransaction, enableLoading :Boolean)
    }
}
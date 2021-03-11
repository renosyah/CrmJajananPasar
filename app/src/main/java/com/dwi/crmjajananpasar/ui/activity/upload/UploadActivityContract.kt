package com.dwi.crmjajananpasar.ui.activity.upload


import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.uploadResponse.UploadResponse
import com.dwi.crmjajananpasar.model.validateTransaction.ValidateTransaction
import okhttp3.MultipartBody

// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
class UploadActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    interface View: BaseContract.View {

        // fungsi fungsi response
        fun onUploaded(uploadResponse: UploadResponse)
        fun showProgressUpload(show: Boolean)
        fun showErrorUpload(e: String)

        fun onValidated()
        fun showProgressValidate(show: Boolean)
        fun showErrorValidate(e: String)
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun upload(file: MultipartBody.Part, enableLoading :Boolean)
        fun addValidateTransaction(validateTransaction: ValidateTransaction, enableLoading :Boolean)
    }
}
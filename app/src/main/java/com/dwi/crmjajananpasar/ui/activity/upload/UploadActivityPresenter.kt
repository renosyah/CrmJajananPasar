package com.dwi.crmjajananpasar.ui.activity.upload

import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.uploadResponse.UploadResponse
import com.dwi.crmjajananpasar.model.validateTransaction.ValidateTransaction
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

// adalah class presenter untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi yg berkaitan dengan proses request data
class UploadActivityPresenter : UploadActivityContract.Presenter {

    // deklarasi variabel
    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: UploadActivityContract.View

    // fungsi request yg akan dipanggil oleh view
    override fun upload(file: MultipartBody.Part, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressUpload(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.upload(file)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<UploadResponse>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressUpload(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorUpload(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onUploaded(result.Data!!)
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressUpload(false)
                }
                view.showErrorUpload(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    // fungsi request yg akan dipanggil oleh view
    override fun addValidateTransaction(validateTransaction: ValidateTransaction, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressValidate(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.addValidateTransaction(validateTransaction.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressValidate(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorValidate(result.Error!!)
                    }
                    if (result.Data != "") {
                        view.onValidated()
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressValidate(false)
                }
                view.showErrorValidate(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    // untuk saat ini kosong
    // belum dibutuhkan
    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    // fungsi inisialisasi view
    override fun attach(view: UploadActivityContract.View) {
        this.view = view
    }

}
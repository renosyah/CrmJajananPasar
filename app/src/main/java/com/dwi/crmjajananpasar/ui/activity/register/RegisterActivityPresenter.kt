package com.dwi.crmjajananpasar.ui.activity.register

import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.customer.Customer
import com.dwi.crmjajananpasar.service.RetrofitService
import com.dwi.crmjajananpasar.ui.activity.login.LoginActivityContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

// adalah class presenter untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi yg berkaitan dengan proses request data
class RegisterActivityPresenter : RegisterActivityContract.Presenter {

    // deklarasi variabel
    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: RegisterActivityContract.View

    // fungsi request yg akan dipanggil oleh view
    override fun register(customer: Customer, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressRegister(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.register(customer.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<Customer>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressRegister(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorRegister(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onRegister(result.Data!!)
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressRegister(false)
                }
                view.showErrorRegister(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    // untuk saat ini kosong
    // belum dibutuhkan
    override fun subscribe() {

    }
    // fungsi untuk membersihkan subscipsi request
    override fun unsubscribe() {
        subscriptions.clear()
    }

    // fungsi inisialisasi view
    override fun attach(view: RegisterActivityContract.View) {
        this.view = view
    }

}
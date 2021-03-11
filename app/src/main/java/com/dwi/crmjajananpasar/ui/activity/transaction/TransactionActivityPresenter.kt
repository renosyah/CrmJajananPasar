package com.dwi.crmjajananpasar.ui.activity.transaction

import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.payment.Payment
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.transaction.Transaction
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

// adalah class presenter untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi yg berkaitan dengan proses request data
class TransactionActivityPresenter : TransactionActivityContract.Presenter {

    // deklarasi variabel
    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: TransactionActivityContract.View

    // fungsi request yg akan dipanggil oleh view
    override fun transactionByRef(transaction: Transaction, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressTransactionByRef(true)
        }
        val subscribe = api.oneTransactionByRef(transaction.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<Transaction>? ->
                if (enableLoading) {
                    view.showProgressTransactionByRef(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorTransactionByRef(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onTransactionByRef(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressTransactionByRef(false)
                }
                view.showErrorTransactionByRef(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    // fungsi request yg akan dipanggil oleh view
    override fun payment(requestListModel: RequestListModel, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressPayment(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.allPayment(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Payment>>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressPayment(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorPayment(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onPayment(result.Data!!)
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressPayment(false)
                }
                view.showErrorPayment(t.message!!)
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
    override fun attach(view: TransactionActivityContract.View) {
        this.view = view
    }

}
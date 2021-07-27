package com.dwi.crmjajananpasar.ui.activity.cod

import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.transaction.Transaction
import com.dwi.crmjajananpasar.model.validateTransaction.ValidateTransaction
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CodActivityPresenter : CodActivityContract.Presenter {

    // deklarasi variabel
    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: CodActivityContract.View

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

    override fun updateTransaction(transaction: Transaction, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressTransactionUpdate(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.updateTransaction(transaction.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressTransactionUpdate(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorTransactionUpdate(result.Error!!)
                    }
                    if (result.Data != "") {
                        view.onTransactionUpdated()
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressTransactionUpdate(false)
                }
                view.showErrorTransactionUpdate(t.message!!)
            })

        subscriptions.add(subscribe)
    }

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

    override fun subscribe() {
    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: CodActivityContract.View) {
        this.view = view
    }
}
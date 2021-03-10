package com.dwi.crmjajananpasar.ui.activity.register

import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.customer.Customer
import com.dwi.crmjajananpasar.service.RetrofitService
import com.dwi.crmjajananpasar.ui.activity.login.LoginActivityContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RegisterActivityPresenter : RegisterActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: RegisterActivityContract.View

    override fun register(customer: Customer, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressRegister(true)
        }
        val subscribe = api.register(customer.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<Customer>? ->
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
                if (enableLoading) {
                    view.showProgressRegister(false)
                }
                view.showErrorRegister(t.message!!)
            })

        subscriptions.add(subscribe)
    }


    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: RegisterActivityContract.View) {
        this.view = view
    }

}
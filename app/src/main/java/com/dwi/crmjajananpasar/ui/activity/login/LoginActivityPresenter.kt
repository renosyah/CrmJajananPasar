package com.dwi.crmjajananpasar.ui.activity.login

import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.customer.Customer
import com.dwi.crmjajananpasar.service.RetrofitService
import com.dwi.crmjajananpasar.ui.activity.login.LoginActivityContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginActivityPresenter : LoginActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: LoginActivityContract.View

    override fun login(customer: Customer, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressLogin(true)
        }
        val subscribe = api.login(customer.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<Customer>? ->
                if (enableLoading) {
                    view.showProgressLogin(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorLogin(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onLogin(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressLogin(false)
                }
                view.showErrorLogin(t.message!!)
            })

        subscriptions.add(subscribe)
    }


    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: LoginActivityContract.View) {
        this.view = view
    }

}
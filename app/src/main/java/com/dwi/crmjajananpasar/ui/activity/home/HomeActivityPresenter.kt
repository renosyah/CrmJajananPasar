package com.dwi.crmjajananpasar.ui.activity.home

import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.cart.TotalCart
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeActivityPresenter : HomeActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: HomeActivityContract.View


    override fun banner(requestListModel: RequestListModel, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressBanner(true)
        }
        val subscribe = api.allProductRecommended(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Product>>? ->
                if (enableLoading) {
                    view.showProgressBanner(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorBanner(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onBanner(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressBanner(false)
                }
                view.showErrorBanner(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun recommended(requestListModel: RequestListModel, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressRecommended(true)
        }
        val subscribe = api.allProductRecommended(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Product>>? ->
                if (enableLoading) {
                    view.showProgressRecommended(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorRecommended(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onRecommended(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressRecommended(false)
                }
                view.showErrorRecommended(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun product(requestListModel: RequestListModel, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressProduct(true)
        }
        val subscribe = api.allProduct(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Product>>? ->
                if (enableLoading) {
                    view.showProgressProduct(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorProduct(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onProduct(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressProduct(false)
                }
                view.showErrorProduct(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun cartTotal(cart: Cart, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressCartTotal(true)
        }
        val subscribe = api.getTotal(cart.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<TotalCart>? ->
                if (enableLoading) {
                    view.showProgressCartTotal(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorCartTotal(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onCartTotal(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressCartTotal(false)
                }
                view.showErrorCartTotal(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: HomeActivityContract.View) {
        this.view = view
    }

}
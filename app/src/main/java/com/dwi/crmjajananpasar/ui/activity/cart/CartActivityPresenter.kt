package com.dwi.crmjajananpasar.ui.activity.cart

import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.cart.TotalCart
import com.dwi.crmjajananpasar.model.checkout.Checkout
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.recipe.Recipe
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CartActivityPresenter : CartActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: CartActivityContract.View

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

    override fun cart(requestListModel: RequestListModel, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressCart(true)
        }
        val subscribe = api.allCart(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Cart>>? ->
                if (enableLoading) {
                    view.showProgressCart(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorCart(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onCart(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressCart(false)
                }
                view.showErrorCart(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun updateCart(cart: Cart, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressUpdateCart(true)
        }
        val subscribe = api.updateCart(cart.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->
                if (enableLoading) {
                    view.showProgressUpdateCart(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorUpdateCart(result.Error!!)
                    }
                    if (result.Data != "") {
                        view.onUpdateCart()
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressUpdateCart(false)
                }
                view.showErrorUpdateCart(t.message!!)
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

    override fun checkout(c: Checkout, enableLoading :Boolean) {
        if (enableLoading) {
            view.showProgressCheckout(true)
        }
        val subscribe = api.checkout(c.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->
                if (enableLoading) {
                    view.showProgressCheckout(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorCheckout(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onCheckout(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressCheckout(false)
                }
                view.showErrorCheckout(t.message!!)
            })

        subscriptions.add(subscribe)
    }


    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: CartActivityContract.View) {
        this.view = view
    }

}
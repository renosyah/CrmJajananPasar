package com.dwi.crmjajananpasar.ui.activity.detail_product

import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.recipe.Recipe
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailProductActivityPresenter : DetailProductActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: DetailProductActivityContract.View

    override fun recipe(requestListModel: RequestListModel, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressRecipe(true)
        }
        val subscribe = api.allRecipe(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Recipe>>? ->
                if (enableLoading) {
                    view.showProgressRecipe(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorRecipe(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onRecipe(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressRecipe(false)
                }
                view.showErrorRecipe(t.message!!)
            })

        subscriptions.add(subscribe)
    }


    override fun addCart(cart: Cart, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressAddCart(true)
        }
        val subscribe = api.addCart(cart.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->
                if (enableLoading) {
                    view.showProgressAddCart(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorAddCart(result.Error!!)
                    }
                    if (result.Data != "") {
                        view.onAddCart()
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressAddCart(false)
                }
                view.showErrorAddCart(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: DetailProductActivityContract.View) {
        this.view = view
    }

}
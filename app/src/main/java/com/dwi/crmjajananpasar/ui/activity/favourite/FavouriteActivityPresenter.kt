package com.dwi.crmjajananpasar.ui.activity.favourite

import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavouriteActivityPresenter : FavouriteActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: FavouriteActivityContract.View

    override fun favourite(requestListModel: RequestListModel, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressFavourite(true)
        }
        val subscribe = api.allProductFavourite(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Product>>? ->
                if (enableLoading) {
                    view.showProgressFavourite(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorFavourite(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onFavourite(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressFavourite(false)
                }
                view.showErrorFavourite(t.message!!)
            })

        subscriptions.add(subscribe)
    }


    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: FavouriteActivityContract.View) {
        this.view = view
    }

}
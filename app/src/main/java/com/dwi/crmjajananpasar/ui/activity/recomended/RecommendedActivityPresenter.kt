package com.dwi.crmjajananpasar.ui.activity.recomended

import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RecommendedActivityPresenter : RecommendedActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: RecommendedActivityContract.View

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


    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: RecommendedActivityContract.View) {
        this.view = view
    }

}
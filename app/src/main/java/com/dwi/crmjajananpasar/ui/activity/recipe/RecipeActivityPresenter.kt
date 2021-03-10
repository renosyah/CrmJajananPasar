package com.dwi.crmjajananpasar.ui.activity.recipe

import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.recipe.Recipe
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RecipeActivityPresenter : RecipeActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: RecipeActivityContract.View

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


    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: RecipeActivityContract.View) {
        this.view = view
    }

}
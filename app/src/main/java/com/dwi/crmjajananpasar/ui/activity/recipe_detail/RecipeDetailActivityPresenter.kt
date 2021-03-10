package com.dwi.crmjajananpasar.ui.activity.recipe_detail

import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.recipe.Recipe
import com.dwi.crmjajananpasar.model.recipe_detail.RecipeDetail
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RecipeDetailActivityPresenter : RecipeDetailActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: RecipeDetailActivityContract.View

    override fun recipeDetail(requestListModel: RequestListModel, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressRecipeDetail(true)
        }
        val subscribe = api.allRecipeDetail(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<RecipeDetail>>? ->
                if (enableLoading) {
                    view.showProgressRecipeDetail(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorRecipeDetail(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onRecipeDetail(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressRecipeDetail(false)
                }
                view.showErrorRecipeDetail(t.message!!)
            })

        subscriptions.add(subscribe)
    }


    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: RecipeDetailActivityContract.View) {
        this.view = view
    }

}
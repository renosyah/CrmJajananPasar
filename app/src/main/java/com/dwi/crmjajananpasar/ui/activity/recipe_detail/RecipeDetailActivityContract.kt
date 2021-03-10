package com.dwi.crmjajananpasar.ui.activity.recipe_detail

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.recipe.Recipe
import com.dwi.crmjajananpasar.model.recipe_detail.RecipeDetail


class RecipeDetailActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onRecipeDetail(data : ArrayList<RecipeDetail>)
        fun showProgressRecipeDetail(show: Boolean)
        fun showErrorRecipeDetail(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun recipeDetail(requestListModel: RequestListModel, enableLoading :Boolean)
    }
}
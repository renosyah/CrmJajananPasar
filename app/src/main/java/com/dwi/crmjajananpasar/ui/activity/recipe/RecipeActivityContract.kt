package com.dwi.crmjajananpasar.ui.activity.recipe

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.recipe.Recipe


class RecipeActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onRecipe(data : ArrayList<Recipe>)
        fun showProgressRecipe(show: Boolean)
        fun showErrorRecipe(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun recipe(requestListModel: RequestListModel, enableLoading :Boolean)
    }
}
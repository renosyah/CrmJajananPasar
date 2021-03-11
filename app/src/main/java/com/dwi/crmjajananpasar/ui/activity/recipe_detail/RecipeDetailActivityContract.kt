package com.dwi.crmjajananpasar.ui.activity.recipe_detail

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.recipe.Recipe
import com.dwi.crmjajananpasar.model.recipe_detail.RecipeDetail

// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
class RecipeDetailActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    interface View: BaseContract.View {


        // fungsi fungsi response
        fun onRecipeDetail(data : ArrayList<RecipeDetail>)
        fun showProgressRecipeDetail(show: Boolean)
        fun showErrorRecipeDetail(e: String)
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    interface Presenter: BaseContract.Presenter<View> {

        // fungsi fungsi request
        fun recipeDetail(requestListModel: RequestListModel, enableLoading :Boolean)
    }
}
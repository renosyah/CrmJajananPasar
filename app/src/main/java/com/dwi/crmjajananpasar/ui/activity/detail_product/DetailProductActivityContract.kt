package com.dwi.crmjajananpasar.ui.activity.detail_product

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.recipe.Recipe

// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
class DetailProductActivityContract {


    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    interface View: BaseContract.View {

        // fungsi fungsi response
        fun onRecipe(data : ArrayList<Recipe>)
        fun showProgressRecipe(show: Boolean)
        fun showErrorRecipe(e: String)

        fun onAddCart()
        fun showProgressAddCart(show: Boolean)
        fun showErrorAddCart(e: String)
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    interface Presenter: BaseContract.Presenter<View> {

        // fungsi fungsi request
        fun recipe(requestListModel: RequestListModel, enableLoading :Boolean)
        fun addCart(cart: Cart, enableLoading :Boolean)
    }
}
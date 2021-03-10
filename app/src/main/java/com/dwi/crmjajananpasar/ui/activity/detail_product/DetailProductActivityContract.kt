package com.dwi.crmjajananpasar.ui.activity.detail_product

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.recipe.Recipe


class DetailProductActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onRecipe(data : ArrayList<Recipe>)
        fun showProgressRecipe(show: Boolean)
        fun showErrorRecipe(e: String)

        fun onAddCart()
        fun showProgressAddCart(show: Boolean)
        fun showErrorAddCart(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun recipe(requestListModel: RequestListModel, enableLoading :Boolean)
        fun addCart(cart: Cart, enableLoading :Boolean)
    }
}
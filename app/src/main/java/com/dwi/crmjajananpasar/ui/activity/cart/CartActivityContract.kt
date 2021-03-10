package com.dwi.crmjajananpasar.ui.activity.cart

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.cart.TotalCart
import com.dwi.crmjajananpasar.model.checkout.Checkout
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.recipe.Recipe


class CartActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onRecommended(data : ArrayList<Product>)
        fun showProgressRecommended(show: Boolean)
        fun showErrorRecommended(e: String)

        fun onCart(data : ArrayList<Cart>)
        fun showProgressCart(show: Boolean)
        fun showErrorCart(e: String)

        fun onUpdateCart()
        fun showProgressUpdateCart(show: Boolean)
        fun showErrorUpdateCart(e: String)

        fun onCartTotal(totalCart : TotalCart)
        fun showProgressCartTotal(show: Boolean)
        fun showErrorCartTotal(e: String)

        fun onCheckout(refId : String)
        fun showProgressCheckout(show: Boolean)
        fun showErrorCheckout(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun recommended(requestListModel: RequestListModel, enableLoading :Boolean)
        fun cart(requestListModel: RequestListModel, enableLoading :Boolean)
        fun updateCart(cart: Cart, enableLoading :Boolean)
        fun cartTotal(cart: Cart, enableLoading :Boolean)
        fun checkout(c : Checkout, enableLoading :Boolean)
    }
}
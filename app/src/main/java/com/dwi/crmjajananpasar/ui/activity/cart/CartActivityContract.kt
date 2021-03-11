package com.dwi.crmjajananpasar.ui.activity.cart

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.cart.TotalCart
import com.dwi.crmjajananpasar.model.checkout.Checkout
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.recipe.Recipe


// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
class CartActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    interface View: BaseContract.View {

        // fungsi fungsi response
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

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    interface Presenter: BaseContract.Presenter<View> {

        // fungsi fungsi request
        fun recommended(requestListModel: RequestListModel, enableLoading :Boolean)
        fun cart(requestListModel: RequestListModel, enableLoading :Boolean)
        fun updateCart(cart: Cart, enableLoading :Boolean)
        fun cartTotal(cart: Cart, enableLoading :Boolean)
        fun checkout(c : Checkout, enableLoading :Boolean)
    }
}
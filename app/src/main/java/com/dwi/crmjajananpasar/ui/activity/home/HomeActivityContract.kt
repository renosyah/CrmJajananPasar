package com.dwi.crmjajananpasar.ui.activity.home

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.cart.TotalCart
import com.dwi.crmjajananpasar.model.product.Product

// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
class HomeActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    interface View: BaseContract.View {


        // fungsi fungsi response
        fun onProductPromo(data : ArrayList<Product>)
        fun showProgressProductPromo(show: Boolean)
        fun showErrorProductPromo(e: String)

        fun onBanner(data : ArrayList<Product>)
        fun showProgressBanner(show: Boolean)
        fun showErrorBanner(e: String)

        fun onRecommended(data : ArrayList<Product>)
        fun showProgressRecommended(show: Boolean)
        fun showErrorRecommended(e: String)

        fun onProduct(data : ArrayList<Product>)
        fun showProgressProduct(show: Boolean)
        fun showErrorProduct(e: String)

        fun onCartTotal(totalCart : TotalCart)
        fun showProgressCartTotal(show: Boolean)
        fun showErrorCartTotal(e: String)
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    interface Presenter: BaseContract.Presenter<View> {

        // fungsi fungsi request
        fun productPromo(requestListModel: RequestListModel, enableLoading :Boolean)
        fun banner(requestListModel: RequestListModel, enableLoading :Boolean)
        fun recommended(requestListModel: RequestListModel, enableLoading :Boolean)
        fun product(requestListModel: RequestListModel, enableLoading :Boolean)
        fun cartTotal(cart: Cart, enableLoading :Boolean)
    }
}
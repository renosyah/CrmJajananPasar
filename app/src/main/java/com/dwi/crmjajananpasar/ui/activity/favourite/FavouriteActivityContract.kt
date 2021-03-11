package com.dwi.crmjajananpasar.ui.activity.favourite

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.product.Product

// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
class FavouriteActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    interface View: BaseContract.View {


        // fungsi fungsi response
        fun onFavourite(data : ArrayList<Product>)
        fun showProgressFavourite(show: Boolean)
        fun showErrorFavourite(e: String)
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    interface Presenter: BaseContract.Presenter<View> {

        // fungsi fungsi request
        fun favourite(requestListModel: RequestListModel, enableLoading :Boolean)
    }
}
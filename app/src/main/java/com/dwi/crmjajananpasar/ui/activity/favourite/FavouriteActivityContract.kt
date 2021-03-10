package com.dwi.crmjajananpasar.ui.activity.favourite

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.product.Product


class FavouriteActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onFavourite(data : ArrayList<Product>)
        fun showProgressFavourite(show: Boolean)
        fun showErrorFavourite(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun favourite(requestListModel: RequestListModel, enableLoading :Boolean)
    }
}
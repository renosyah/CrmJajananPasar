package com.dwi.crmjajananpasar.ui.activity.home

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.product.Product


class HomeActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onBanner(data : ArrayList<Product>)
        fun showProgressBanner(show: Boolean)
        fun showErrorBanner(e: String)

        fun onRecommended(data : ArrayList<Product>)
        fun showProgressRecommended(show: Boolean)
        fun showErrorRecommended(e: String)

        fun onProduct(data : ArrayList<Product>)
        fun showProgressProduct(show: Boolean)
        fun showErrorProduct(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun banner(requestListModel: RequestListModel, enableLoading :Boolean)
        fun recommended(requestListModel: RequestListModel, enableLoading :Boolean)
        fun product(requestListModel: RequestListModel, enableLoading :Boolean)
    }
}
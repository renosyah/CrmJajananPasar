package com.dwi.crmjajananpasar.ui.activity.recomended

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.product.Product


class RecommendedActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onRecommended(data : ArrayList<Product>)
        fun showProgressRecommended(show: Boolean)
        fun showErrorRecommended(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun recommended(requestListModel: RequestListModel, enableLoading :Boolean)
    }
}
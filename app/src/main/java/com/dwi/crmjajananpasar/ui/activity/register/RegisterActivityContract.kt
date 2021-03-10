package com.dwi.crmjajananpasar.ui.activity.register

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.customer.Customer


class RegisterActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onRegister(customer : Customer)
        fun showProgressRegister(show: Boolean)
        fun showErrorRegister(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun register(customer : Customer, enableLoading :Boolean)
    }
}
package com.dwi.crmjajananpasar.ui.activity.login

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.customer.Customer


class LoginActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onLogin(customer : Customer)
        fun showProgressLogin(show: Boolean)
        fun showErrorLogin(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun login(customer : Customer, enableLoading :Boolean)
    }
}
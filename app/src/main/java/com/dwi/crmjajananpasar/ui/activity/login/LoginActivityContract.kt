package com.dwi.crmjajananpasar.ui.activity.login

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.customer.Customer

// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
class LoginActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    interface View: BaseContract.View {


        // fungsi fungsi response
        fun onLogin(customer : Customer)
        fun showProgressLogin(show: Boolean)
        fun showErrorLogin(e: String)
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    interface Presenter: BaseContract.Presenter<View> {

        // fungsi fungsi request
        fun login(customer : Customer, enableLoading :Boolean)
    }
}
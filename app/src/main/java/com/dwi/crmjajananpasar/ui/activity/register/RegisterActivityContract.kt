package com.dwi.crmjajananpasar.ui.activity.register

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.customer.Customer

// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
class RegisterActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    interface View: BaseContract.View {


        // fungsi fungsi response
        fun onRegister(customer : Customer)
        fun showProgressRegister(show: Boolean)
        fun showErrorRegister(e: String)
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    interface Presenter: BaseContract.Presenter<View> {

        // fungsi fungsi request
        fun register(customer : Customer, enableLoading :Boolean)
    }
}
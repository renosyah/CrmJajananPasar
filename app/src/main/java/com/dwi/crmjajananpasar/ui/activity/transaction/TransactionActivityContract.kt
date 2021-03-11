package com.dwi.crmjajananpasar.ui.activity.transaction

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.payment.Payment
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.transaction.Transaction

// adalah class contract untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi apa saja yg dibutkan untuk
// komunikasi antar view dengan presenter
class TransactionActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    interface View: BaseContract.View {

        // fungsi fungsi response
        fun onTransactionByRef(transaction: Transaction)
        fun showProgressTransactionByRef(show: Boolean)
        fun showErrorTransactionByRef(e: String)

        fun onPayment(data : ArrayList<Payment>)
        fun showProgressPayment(show: Boolean)
        fun showErrorPayment(e: String)
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun transactionByRef(transaction: Transaction, enableLoading :Boolean)
        fun payment(requestListModel: RequestListModel, enableLoading :Boolean)
    }
}
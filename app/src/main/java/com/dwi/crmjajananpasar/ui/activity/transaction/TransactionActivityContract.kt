package com.dwi.crmjajananpasar.ui.activity.transaction

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.payment.Payment
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.transaction.Transaction


class TransactionActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onTransactionByRef(transaction: Transaction)
        fun showProgressTransactionByRef(show: Boolean)
        fun showErrorTransactionByRef(e: String)

        // add more for request
        fun onPayment(data : ArrayList<Payment>)
        fun showProgressPayment(show: Boolean)
        fun showErrorPayment(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun transactionByRef(transaction: Transaction, enableLoading :Boolean)
        fun payment(requestListModel: RequestListModel, enableLoading :Boolean)
    }
}
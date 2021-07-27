package com.dwi.crmjajananpasar.ui.activity.cod

import com.dwi.crmjajananpasar.base.BaseContract
import com.dwi.crmjajananpasar.model.transaction.Transaction
import com.dwi.crmjajananpasar.model.validateTransaction.ValidateTransaction

class CodActivityContract {

    // inteface view yg akan diimplement oleh
    // view seperti aktivity atau fragment
    interface View: BaseContract.View {

        // fungsi fungsi response
        fun onTransactionByRef(trans: Transaction)
        fun showProgressTransactionByRef(show: Boolean)
        fun showErrorTransactionByRef(e: String)

        fun onTransactionUpdated()
        fun showProgressTransactionUpdate(show: Boolean)
        fun showErrorTransactionUpdate(e: String)

        fun onValidated()
        fun showProgressValidate(show: Boolean)
        fun showErrorValidate(e: String)
    }

    // inteface presenter yg akan diimplement oleh
    // presenter seperti aktivity presenter atau fragment presenter
    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun transactionByRef(transaction: Transaction, enableLoading :Boolean)
        fun updateTransaction(transaction: Transaction, enableLoading :Boolean)
        fun addValidateTransaction(validateTransaction: ValidateTransaction, enableLoading :Boolean)
    }
}
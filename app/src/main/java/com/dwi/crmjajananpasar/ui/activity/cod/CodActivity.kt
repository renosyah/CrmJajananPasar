package com.dwi.crmjajananpasar.ui.activity.cod

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.di.component.DaggerActivityComponent
import com.dwi.crmjajananpasar.di.module.ActivityModule
import com.dwi.crmjajananpasar.model.transaction.Transaction
import com.dwi.crmjajananpasar.model.validateTransaction.ValidateTransaction
import com.dwi.crmjajananpasar.ui.activity.success.SuccessActivity
import com.dwi.crmjajananpasar.ui.util.ErrorLayout
import com.dwi.crmjajananpasar.ui.util.LoadingLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_c_o_d.*
import kotlinx.android.synthetic.main.activity_c_o_d.back_imageview
import kotlinx.android.synthetic.main.activity_c_o_d.error_layout
import kotlinx.android.synthetic.main.activity_c_o_d.loading_layout
import javax.inject.Inject

class CodActivity : AppCompatActivity(), CodActivityContract.View {

    // deklarasi variabel
    @Inject
    lateinit var presenter: CodActivityContract.Presenter
    lateinit var context: Context
    private var refId : String = ""
    private val validateTransaction = ValidateTransaction()
    private var transaction = Transaction()
    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_o_d)

        // panggil fungsi init widget
        initWidget()
    }


    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {

        this.context = this@CodActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (intent.hasExtra("ref_id")) {
            refId = intent.getStringExtra("ref_id")!!
        }

        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.process))
        loading.hide()

        error = ErrorLayout(context,error_layout) {
            cod_form_layout.visibility = View.VISIBLE
        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        back_imageview.setOnClickListener {
            onBackPressed()
        }

        send_image_button.setOnClickListener {
            if (input_address_edittext.text.toString().trim().isEmpty()) {
                return@setOnClickListener
            }
            transaction.address = input_address_edittext.text.toString()
            presenter.updateTransaction(transaction, true)
        }

        presenter.transactionByRef(Transaction(refId),true)
    }
    
    override fun onTransactionByRef(trans: Transaction) {
        transaction = trans
        Log.e("trans",Gson().toJson(trans))
        validateTransaction.transactionId = trans.id
    }

    override fun showProgressTransactionByRef(show: Boolean) {
        cod_form_layout.visibility = if (show) View.GONE else View.VISIBLE
        loading.setMessage(getString(R.string.loading_payment))
        loading.setVisibility(show)
    }

    override fun showErrorTransactionByRef(e: String) {
        cod_form_layout.visibility = View.GONE
        error.setMessage(e)
        error.show()
    }

    override fun onTransactionUpdated() {
        validateTransaction.imageUrl = "/img/VIA_COD.jpg"
        presenter.addValidateTransaction(validateTransaction,true)
    }

    override fun showProgressTransactionUpdate(show: Boolean) {
        cod_form_layout.visibility = if (show) View.GONE else View.VISIBLE
        loading.setMessage(getString(R.string.process))
        loading.setVisibility(show)
    }

    override fun showErrorTransactionUpdate(e: String) {
        cod_form_layout.visibility = View.GONE
        error.setMessage(e)
        error.show()
    }

    override fun onValidated() {
        startActivity(Intent(context, SuccessActivity::class.java))
        finish()
    }

    override fun showProgressValidate(show: Boolean) {
        cod_form_layout.visibility = if (show) View.GONE else View.VISIBLE
        loading.setMessage(getString(R.string.process))
        loading.setVisibility(show)
    }

    override fun showErrorValidate(e: String) {
        cod_form_layout.visibility = View.GONE
        error.setMessage(e)
        error.show()
    }
    
    
    // fungsi saat user
    // menekan tombol back
    override fun onBackPressed() {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.warning))
            .setMessage(getString(R.string.message_canceling_transaction))
            .setPositiveButton(getString(R.string.yes)){d, which ->
                super.onBackPressed()
                finish()
            }
            .setNegativeButton(getString(R.string.no),null)
            .setCancelable(false)
            .create()
            .show()
        return
    }

    // fungsi saat activity
    // dihancurkan
    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    // fungsi inject
    // dependensi agar
    // presenter activity dapat
    // digunakan
    private fun injectDependency(){
        val listcomponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        listcomponent.inject(this)
    }
}
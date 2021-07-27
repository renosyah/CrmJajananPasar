package com.dwi.crmjajananpasar.ui.activity.payment_method

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.ui.activity.cod.CodActivity
import com.dwi.crmjajananpasar.ui.activity.transaction.TransactionActivity
import kotlinx.android.synthetic.main.activity_payment_method.*

class PaymentMethod : AppCompatActivity() {

    lateinit var context: Context
    private var refId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)

        // panggil fungsi init widget
        initWidget()
    }


    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {

        this.context = this@PaymentMethod

        if (intent.hasExtra("ref_id")){
            refId = intent.getStringExtra("ref_id")!!
        }

        transfer_layout.setOnClickListener {
            val i = Intent(context, TransactionActivity::class.java)
            i.putExtra("ref_id", refId)
            startActivity(i)
            finish()
        }
        cod_layout.setOnClickListener {
            val i = Intent(context, CodActivity::class.java)
            i.putExtra("ref_id", refId)
            startActivity(i)
            finish()
        }
        back_imageview.setOnClickListener {
            onBackPressed()
        }
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
}
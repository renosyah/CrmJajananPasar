package com.dwi.crmjajananpasar.ui.activity.transaction

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.di.component.DaggerActivityComponent
import com.dwi.crmjajananpasar.di.module.ActivityModule
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.payment.Payment
import com.dwi.crmjajananpasar.model.transaction.Transaction
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivity
import com.dwi.crmjajananpasar.ui.activity.success.SuccessActivity
import com.dwi.crmjajananpasar.ui.activity.timeout.TimeoutActivity
import com.dwi.crmjajananpasar.ui.activity.upload.UploadActivity
import com.dwi.crmjajananpasar.ui.adapter.AdapterPayment
import com.dwi.crmjajananpasar.ui.dialog.ErrorDialog
import com.dwi.crmjajananpasar.ui.dialog.LoadingDialog
import kotlinx.android.synthetic.main.activity_recipe.back_imageview
import kotlinx.android.synthetic.main.activity_transaction.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TransactionActivity : AppCompatActivity(), TransactionActivityContract.View {

    // deklarasi variabel
    @Inject
    lateinit var presenter: TransactionActivityContract.Presenter
    lateinit var context: Context
    private var refId : String = ""
    lateinit var transaction: Transaction
    lateinit var timer : CountDownTimer
    private var minuteLeft : Int = 60
    private var enableShowTimeout = true
    val reqPayment : RequestListModel = RequestListModel()
    lateinit var adapterPayment : AdapterPayment
    val payments : ArrayList<Payment> = ArrayList()
    lateinit var loadingDialog : LoadingDialog
    lateinit var errorDialog: ErrorDialog

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        // panggil fungsi init widget
        initWidget()
    }

    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {
        this.context = this@TransactionActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        loadingDialog = LoadingDialog(context)
        errorDialog = ErrorDialog(context){
            finish()
            startActivity(intent)
        }

        if (intent.hasExtra("ref_id")){
            refId = intent.getStringExtra("ref_id")!!
        }

        back_imageview.setOnClickListener {

            onBackPressed()
        }

        upload_button.setOnClickListener {

            if (this::transaction.isInitialized){
                val i = Intent(context, UploadActivity::class.java)
                i.putExtra("transaction",transaction)
                startActivity(i)
                finish()
            }
        }
        upload_button.visibility = View.GONE

        setAdapters()
        requestAllData()
        setPaginationScroll()
        startCountDown(minuteLeft)
    }

    // mengisi nilai recycleview dengan adapter
    private fun setAdapters(){

        // mengisi nilai recycleview dengan adapter
        adapterPayment = AdapterPayment(context,payments){ p,i  ->

            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("rekening", p.detail)
            clipboardManager.setPrimaryClip(clipData)

            Toast.makeText(context,getString(R.string.copied),Toast.LENGTH_SHORT).show()
        }
        payment_recycleview.adapter = adapterPayment
        payment_recycleview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    // fungsi request data
    // dan mengisi variabel
    // untuk request data list
    private fun requestAllData(){

        reqPayment.categoryId = 1
        reqPayment.searchBy = "name"
        reqPayment.orderBy = "name"
        reqPayment.orderDir = "ASC"
        reqPayment.offset = 0
        reqPayment.limit = 10

        presenter.payment(reqPayment,true)
        presenter.transactionByRef(Transaction(refId),true)
    }

    // fungsi pagination
    // yg akan dipanggil saat scroll
    // mentok kebawah
    private fun setPaginationScroll(){
        transaction_nestedscrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                // pagination if user reach scroll to bottom on course
                reqPayment.offset += reqPayment.limit
                presenter.payment(reqPayment,false)
            }
        })
    }

    //fungsi untuk memulai hitungan mundu
    private fun startCountDown(minute : Int){
        timer = object : CountDownTimer(TimeUnit.MINUTES.toMillis(minute.toLong()), TimeUnit.SECONDS.toMillis(1L)) {
            override fun onTick(milis: Long) {
                val min = TimeUnit.MILLISECONDS.toMinutes(milis)
                val sec = TimeUnit.MILLISECONDS.toSeconds(milis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milis))
                time_textview.text = "${min} menit ${sec} detik"
            }

            override fun onFinish() {
                if (enableShowTimeout){
                    startActivity(Intent(context, TimeoutActivity::class.java))
                    finish()
                }
            }

        }
        timer.start()
    }


    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onPayment(data: ArrayList<Payment>) {
        payments.addAll(data)
        adapterPayment.notifyDataSetChanged()
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressPayment(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.loading_payment))
        loadingDialog.setVisibility(show)
    }

    override fun showErrorPayment(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }


    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onTransactionByRef(t: Transaction) {
        transaction = t
        upload_button.visibility = View.VISIBLE
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressTransactionByRef(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.loading_transaction))
        loadingDialog.setVisibility(show)
    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorTransactionByRef(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
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
        enableShowTimeout = false
        if (this::timer.isInitialized){
            timer.onFinish()
        }
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
package com.dwi.crmjajananpasar.ui.activity.favourite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.di.component.DaggerActivityComponent
import com.dwi.crmjajananpasar.di.module.ActivityModule
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.customer.Customer
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.ui.activity.detail_product.DetailProductActivity
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivity
import com.dwi.crmjajananpasar.ui.adapter.AdapterProduct
import com.dwi.crmjajananpasar.util.SerializableSave
import kotlinx.android.synthetic.main.activity_favourite.*
import javax.inject.Inject

class FavouriteActivity : AppCompatActivity() , FavouriteActivityContract.View {

    // deklarasi variabel
    @Inject
    lateinit var presenter: FavouriteActivityContract.Presenter
    lateinit var context: Context
    val reqfavourite : RequestListModel = RequestListModel()
    lateinit var adapterProduct : AdapterProduct
    val products : ArrayList<Product> = ArrayList()

    // fungsi utama yg akan
    // dipanggil saat activity dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        // panggil fungsi init widget
        initWidget()
    }

    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {
        this.context = this@FavouriteActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        back_imageview.setOnClickListener {
            startActivity(Intent(context, HomeActivity::class.java))
            finish()
        }

        setAdapters()
        requestAllData()
        setPaginationScroll()
    }

    // mengisi nilai recycleview dengan adapter
    private fun setAdapters(){

        // mengisi nilai recycleview dengan adapter
        adapterProduct = AdapterProduct(context,products){ product, i ->

            val intent = Intent(context,DetailProductActivity::class.java)
            intent.putExtra("product",product)
            startActivity(intent)
        }
        favourite_recycleview.adapter = adapterProduct
        favourite_recycleview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    // fungsi request data
    // dan mengisi variabel
    // untuk request data list
    private fun requestAllData(){

        if (SerializableSave(context, SerializableSave.userDataFileSessionName).load() != null){
            val customer = SerializableSave(context, SerializableSave.userDataFileSessionName).load() as Customer
            reqfavourite.categoryId = 1
            reqfavourite.customerId = customer.id
            reqfavourite.favouriteValue = 2
            reqfavourite.offset = 0
            reqfavourite.limit = 10

            presenter.favourite(reqfavourite,true)
        }
    }

    // fungsi pagination
    // yg akan dipanggil saat scroll
    // mentok kebawah
    private fun setPaginationScroll(){
        favourite_nestedscrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                // pagination if user reach scroll to bottom on course
                reqfavourite.offset += reqfavourite.limit
                presenter.favourite(reqfavourite,false)
            }
        })
    }


    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onFavourite(data: ArrayList<Product>) {
        products.addAll(data)
        adapterProduct.notifyDataSetChanged()
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressFavourite(show: Boolean) {

    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorFavourite(e: String) {
        Toast.makeText(context,e, Toast.LENGTH_SHORT).show()
    }

    // fungsi saat user
    // menekan tombol back
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(context, HomeActivity::class.java))
        finish()
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
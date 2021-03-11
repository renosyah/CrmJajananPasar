package com.dwi.crmjajananpasar.ui.activity.recomended

import android.app.Activity
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
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.ui.activity.detail_product.DetailProductActivity
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivity
import com.dwi.crmjajananpasar.ui.adapter.AdapterProduct
import kotlinx.android.synthetic.main.activity_favourite.back_imageview
import kotlinx.android.synthetic.main.activity_recommended.*
import javax.inject.Inject

class RecommendedActivity : AppCompatActivity(),RecommendedActivityContract.View {

    // deklarasi variabel
    @Inject
    lateinit var presenter: RecommendedActivityContract.Presenter
    lateinit var context: Context
    val reqfavourite : RequestListModel = RequestListModel()
    lateinit var adapterProduct : AdapterProduct
    val products : ArrayList<Product> = ArrayList()

    // fungsi utama yg akan
    // dipanggil saat activity dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommended)

        // panggil fungsi init widget
        initWidget()
    }

    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {
        this.context = this@RecommendedActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        back_imageview.setOnClickListener {
            setResult(Activity.RESULT_OK)
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
        recommended_recycleview.adapter = adapterProduct
        recommended_recycleview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    // fungsi request data
    // dan mengisi variabel
    // untuk request data list
    private fun requestAllData(){

        reqfavourite.categoryId = 1
        reqfavourite.recomendedValue = 2
        reqfavourite.offset = 0
        reqfavourite.limit = 10

        presenter.recommended(reqfavourite,true)
    }

    // fungsi pagination
    // yg akan dipanggil saat scroll
    // mentok kebawah
    private fun setPaginationScroll(){
        recommended_nestedscrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                // pagination if user reach scroll to bottom on course
                reqfavourite.offset += reqfavourite.limit
                presenter.recommended(reqfavourite,false)
            }
        })
    }


    override fun onRecommended(data: ArrayList<Product>) {
        products.addAll(data)
        adapterProduct.notifyDataSetChanged()
    }

    override fun showProgressRecommended(show: Boolean) {

    }

    override fun showErrorRecommended(e: String) {
        Toast.makeText(context,e, Toast.LENGTH_SHORT).show()
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
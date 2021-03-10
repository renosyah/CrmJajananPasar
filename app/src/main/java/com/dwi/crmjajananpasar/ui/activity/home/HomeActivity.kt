package com.dwi.crmjajananpasar.ui.activity.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.di.component.DaggerActivityComponent
import com.dwi.crmjajananpasar.di.module.ActivityModule
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.cart.TotalCart
import com.dwi.crmjajananpasar.model.customer.Customer
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.ui.activity.detail_product.DetailProductActivity
import com.dwi.crmjajananpasar.ui.activity.favourite.FavouriteActivity
import com.dwi.crmjajananpasar.ui.activity.profile.ProfileActivity
import com.dwi.crmjajananpasar.ui.activity.recipe.RecipeActivity
import com.dwi.crmjajananpasar.ui.activity.recomended.RecommendedActivity
import com.dwi.crmjajananpasar.ui.adapter.AdapterBanner
import com.dwi.crmjajananpasar.ui.adapter.AdapterProduct
import com.dwi.crmjajananpasar.ui.adapter.AdapterProductRecommended
import com.dwi.crmjajananpasar.util.Formatter.Companion.decimalFormat
import com.dwi.crmjajananpasar.util.SerializableSave
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(),HomeActivityContract.View {

    @Inject
    lateinit var presenter: HomeActivityContract.Presenter

    // konteks yang dipakai
    lateinit var context: Context
    val FROM_DETAIL_PRODUCT =102

    lateinit var customer : Customer
    val reqBanner : RequestListModel = RequestListModel()
    val reqRecommended : RequestListModel = RequestListModel()
    val reqProduct : RequestListModel = RequestListModel()
    val reqCartTotal :Cart = Cart()

    lateinit var adapterBanner : AdapterBanner
    val banners : ArrayList<Product> = ArrayList()

    lateinit var adapterRecommended : AdapterProductRecommended
    val productsRecommended : ArrayList<Product> = ArrayList()

    lateinit var adapterProduct : AdapterProduct
    val products : ArrayList<Product> = ArrayList()

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // panggil fungsi init widget
        initWidget();
    }

    private fun initWidget() {
        this.context = this@HomeActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (SerializableSave(context, SerializableSave.userDataFileSessionName).load() != null){
            customer = SerializableSave(context, SerializableSave.userDataFileSessionName).load() as Customer

        }
        profile_imageview.setOnClickListener {
            startActivity(Intent(context, ProfileActivity::class.java))
            finish()
        }
        favourite_imageview.setOnClickListener {
            startActivity(Intent(context, FavouriteActivity::class.java))
            finish()
        }
        receipt_button.setOnClickListener {
            startActivity(Intent(context, RecipeActivity::class.java))
            finish()
        }
        see_more_textview.setOnClickListener {
            startActivity(Intent(context, RecommendedActivity::class.java))
            finish()
        }
        cart_imageview.setOnClickListener {

        }
        cart_button.setOnClickListener {
            cart_imageview.performClick()
        }

        setAdapters()
        setAutoSlideBanner()
        setPaginationScroll()
        requestAllData()
    }

    // mengisi nilai recycleview dengan adapter
    private fun setAdapters(){

        // mengisi nilai recycleview banner dengan adapter
        adapterBanner = AdapterBanner(context,banners) { product, i ->

            val intent = Intent(context,DetailProductActivity::class.java)
            intent.putExtra("product",product)
            startActivityForResult(intent,FROM_DETAIL_PRODUCT)
        }
        banner_recycleview.adapter = adapterBanner
        banner_recycleview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(banner_recycleview)


        // mengisi nilai recycleview produk rekomendasi dengan adapter
        adapterRecommended = AdapterProductRecommended(context,productsRecommended){ product, i ->

            val intent = Intent(context,DetailProductActivity::class.java)
            intent.putExtra("product",product)
            startActivityForResult(intent,FROM_DETAIL_PRODUCT)
        }
        recomended_recycleview.adapter = adapterRecommended
        recomended_recycleview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)


        // mengisi nilai recycleview dengan adapter
        adapterProduct = AdapterProduct(context,products){ product, i ->

            val intent = Intent(context,DetailProductActivity::class.java)
            intent.putExtra("product",product)
            startActivityForResult(intent,FROM_DETAIL_PRODUCT)
        }
        product_recycleview.adapter = adapterProduct
        product_recycleview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
    }

    private fun setAutoSlideBanner(){
        var bannerPos = 0
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                if (banner_recycleview == null)
                    return

                bannerPos = if (bannerPos == banners.size) 0 else bannerPos
                banner_recycleview.smoothScrollToPosition(bannerPos)
                bannerPos++
                mainHandler.postDelayed(this, 5000)
            }
        })
    }

    private fun setPaginationScroll(){
        home_nestedscrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                // pagination if user reach scroll to bottom on course
                reqProduct.offset += reqProduct.limit
                presenter.product(reqProduct,false)
            }
        })
    }

    private fun requestAllData(){
        reqBanner.categoryId = 1
        reqBanner.recomendedValue = 2
        reqBanner.offset = 0
        reqBanner.limit = 10

        reqRecommended.categoryId = 1
        reqRecommended.recomendedValue = 2
        reqRecommended.offset = 0
        reqRecommended.limit = 10

        reqProduct.categoryId = 1
        reqProduct.searchBy = "name"
        reqProduct.orderBy = "name"
        reqProduct.orderDir = "ASC"
        reqProduct.offset = 0
        reqProduct.limit = 10

        reqCartTotal.customerId = customer.id
        presenter.cartTotal(reqCartTotal,false)

        presenter.banner(reqBanner,true)
        presenter.recommended(reqRecommended,true)
        presenter.product(reqProduct,true)
    }

    override fun onBanner(data: ArrayList<Product>) {
        banners.addAll(data)
        adapterBanner.notifyDataSetChanged()
    }

    override fun showProgressBanner(show: Boolean) {

    }

    override fun showErrorBanner(e: String) {
        Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
    }

    //
    override fun onRecommended(data: ArrayList<Product>) {
        productsRecommended.addAll(data)
        adapterRecommended.notifyDataSetChanged()
    }

    override fun showProgressRecommended(show: Boolean) {

    }

    override fun showErrorRecommended(e: String) {
        Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
    }

    //
    override fun onProduct(data: ArrayList<Product>) {
        products.addAll(data)
        adapterProduct.notifyDataSetChanged()
    }

    override fun showProgressProduct(show: Boolean) {

    }

    override fun showErrorProduct(e: String) {
        Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
    }

    //

    override fun onCartTotal(totalCart : TotalCart) {
        cart_button.visibility = if (totalCart.item > 0) View.VISIBLE else View.GONE
        cart_button.text = "${getString(R.string.check_cart)} ${totalCart.item} ${getString(R.string.item)} ${decimalFormat(totalCart.total)}"
    }

    override fun showProgressCartTotal(show: Boolean) {

    }

    override fun showErrorCartTotal(e: String) {
        Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FROM_DETAIL_PRODUCT && resultCode == Activity.RESULT_OK){
            presenter.cartTotal(reqCartTotal,false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    private fun injectDependency(){
        val listcomponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        listcomponent.inject(this)
    }
}
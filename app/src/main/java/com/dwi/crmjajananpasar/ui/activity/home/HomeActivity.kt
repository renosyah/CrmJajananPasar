package com.dwi.crmjajananpasar.ui.activity.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.dwi.crmjajananpasar.ui.activity.ContactUsActivity
import com.dwi.crmjajananpasar.ui.activity.cart.CartActivity
import com.dwi.crmjajananpasar.ui.activity.detail_product.DetailProductActivity
import com.dwi.crmjajananpasar.ui.activity.favourite.FavouriteActivity
import com.dwi.crmjajananpasar.ui.activity.profile.ProfileActivity
import com.dwi.crmjajananpasar.ui.activity.recipe.RecipeActivity
import com.dwi.crmjajananpasar.ui.activity.recomended.RecommendedActivity
import com.dwi.crmjajananpasar.ui.adapter.AdapterBanner
import com.dwi.crmjajananpasar.ui.adapter.AdapterProduct
import com.dwi.crmjajananpasar.ui.adapter.AdapterProductRecommended
import com.dwi.crmjajananpasar.ui.dialog.ErrorDialog
import com.dwi.crmjajananpasar.ui.dialog.LoadingDialog
import com.dwi.crmjajananpasar.util.Formatter.Companion.decimalFormat
import com.dwi.crmjajananpasar.util.SerializableSave
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity(),HomeActivityContract.View {

    // variabel static
    companion object {
        val FROM_BASE = 102
        val MY_PERMISSIONS_REQUEST = 121
    }

    // deklarasi variabel
    @Inject
    lateinit var presenter: HomeActivityContract.Presenter
    lateinit var context: Context
    lateinit var customer : Customer
    val reqProductPromo : RequestListModel = RequestListModel()
    val reqBanner : RequestListModel = RequestListModel()
    val reqRecommended : RequestListModel = RequestListModel()
    val reqProduct : RequestListModel = RequestListModel()
    val reqCartTotal :Cart = Cart()
    val productPromo : ArrayList<Product> = ArrayList()
    lateinit var adapterBanner : AdapterBanner
    val banners : ArrayList<Product> = ArrayList()
    lateinit var adapterRecommended : AdapterProductRecommended
    val productsRecommended : ArrayList<Product> = ArrayList()
    lateinit var adapterProduct : AdapterProduct
    val products : ArrayList<Product> = ArrayList()
    lateinit var loadingDialog : LoadingDialog
    lateinit var errorDialog: ErrorDialog

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // panggil fungsi init widget
        initWidget();
    }

    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {
        this.context = this@HomeActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (SerializableSave(context, SerializableSave.userDataFileSessionName).load() != null){
            customer = SerializableSave(context, SerializableSave.userDataFileSessionName).load() as Customer
        }

        loadingDialog = LoadingDialog(context)
        errorDialog = ErrorDialog(context){
            finish()
            startActivity(intent)
        }

        contact_us_textview.setOnClickListener {
            startActivity(Intent(context, ContactUsActivity::class.java))
        }

        profile_imageview.setOnClickListener {
            startActivity(Intent(context, ProfileActivity::class.java))
            finish()
        }
        favourite_imageview.setOnClickListener {
            startActivity(Intent(context, FavouriteActivity::class.java))
            finish()
        }
        recipe_button.setOnClickListener {
            startActivity(Intent(context, RecipeActivity::class.java))
            finish()
        }
        see_more_textview.setOnClickListener {
            startActivityForResult(Intent(context, RecommendedActivity::class.java), FROM_BASE)
        }
        cart_imageview.setOnClickListener {
            startActivityForResult(Intent(context, CartActivity::class.java), FROM_BASE)
        }
        cart_button.setOnClickListener {
            cart_imageview.performClick()
        }
        cart_button.visibility = View.GONE

        requestPermission{

            setAdapters()
            setAutoSlideBanner()
            setPaginationScroll()
            requestAllData()
        }
    }

    // mengisi nilai recycleview dengan adapter
    private fun setAdapters(){

        // mengisi nilai recycleview banner dengan adapter
        adapterBanner = AdapterBanner(context,banners) { product, i ->

            val intent = Intent(context,DetailProductActivity::class.java)
            intent.putExtra("product",product)
            startActivityForResult(intent,FROM_BASE)
        }
        banner_recycleview.adapter = adapterBanner
        banner_recycleview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(banner_recycleview)


        // mengisi nilai recycleview produk rekomendasi dengan adapter
        adapterRecommended = AdapterProductRecommended(context,productsRecommended){ product, i ->

            val intent = Intent(context,DetailProductActivity::class.java)
            intent.putExtra("product",product)
            startActivityForResult(intent,FROM_BASE)
        }
        recomended_recycleview.adapter = adapterRecommended
        recomended_recycleview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)


        // mengisi nilai recycleview dengan adapter
        adapterProduct = AdapterProduct(context,products){ product, i ->

            val intent = Intent(context,DetailProductActivity::class.java)
            intent.putExtra("product",product)
            startActivityForResult(intent,FROM_BASE)
        }
        product_recycleview.adapter = adapterProduct
        product_recycleview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
    }

    // fungsi untuk membuat banner
    // slide secara otomatis setiap
    // 5 detik
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

    // fungsi pagination
    // yg akan dipanggil saat scroll
    // mentok kebawah
    private fun setPaginationScroll(){
        home_nestedscrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                // pagination if user reach scroll to bottom on course
                reqProduct.offset += reqProduct.limit
                presenter.product(reqProduct,false)
            }
        })
    }

    // fungsi request data
    // dan mengisi variabel
    // untuk request data list
    private fun requestAllData(){

        val date = Calendar.getInstance()
        reqProductPromo.currentDate = "${date.get(Calendar.YEAR)}-${date.get(Calendar.MONTH) + 1}-${date.get(Calendar.DAY_OF_MONTH)}"
        reqProductPromo.customerId = customer.id
        reqProductPromo.offset = 0
        reqProductPromo.limit = 10

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

        presenter.productPromo(reqProductPromo,true)
        presenter.product(reqProduct,true)
    }

    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onProductPromo(data: ArrayList<Product>) {
        banners.addAll(data)
        productsRecommended.addAll(data)

        presenter.banner(reqBanner,true)
        presenter.recommended(reqRecommended,true)
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressProductPromo(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.loading_promo))
        loadingDialog.setVisibility(show)
    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorProductPromo(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }

    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onBanner(data: ArrayList<Product>) {
        banners.addAll(data)
        adapterBanner.notifyDataSetChanged()
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressBanner(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.loading_banner))
        loadingDialog.setVisibility(show)
    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorBanner(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }


    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onRecommended(data: ArrayList<Product>) {
        productsRecommended.addAll(data)
        adapterRecommended.notifyDataSetChanged()
        recomended_layout.visibility = if (productsRecommended.isNotEmpty()) View.VISIBLE else View.GONE
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressRecommended(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.loading_recommended))
        loadingDialog.setVisibility(show)
    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorRecommended(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }


    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onProduct(data: ArrayList<Product>) {
        products.addAll(data)
        adapterProduct.notifyDataSetChanged()
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressProduct(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.loading_product))
        loadingDialog.setVisibility(show)
    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorProduct(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }


    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onCartTotal(totalCart : TotalCart) {
        cart_button.visibility = if (totalCart.item > 0) View.VISIBLE else View.GONE
        cart_button_item.text = "${totalCart.item} ${getString(R.string.item)}"
        cart_button_total.text = decimalFormat(totalCart.total)
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressCartTotal(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.loading_cart_total))
        loadingDialog.setVisibility(show)
    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorCartTotal(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }

    // fungsi untuk menangkap hasil
    // yang akan merefresh
    // total keranjang
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FROM_BASE && resultCode == Activity.RESULT_OK){
            presenter.cartTotal(reqCartTotal,false)

            productsRecommended.clear()
            banners.clear()
            productPromo.clear()

            presenter.productPromo(reqProductPromo,false)
        }
    }

    // fungsi untuk menangkap hasil
    // apakah permission di setujui
    // atau tidak
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            startActivity(Intent(context, HomeActivity::class.java))
            finish()
        }
    }

    // fungsi untuk request permission
    private fun requestPermission(next: (Boolean)->Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((context as Activity),arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST)

        } else {
            next.invoke(true)
        }
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
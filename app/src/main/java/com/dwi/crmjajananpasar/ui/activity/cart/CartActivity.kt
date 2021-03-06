package com.dwi.crmjajananpasar.ui.activity.cart

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.di.component.DaggerActivityComponent
import com.dwi.crmjajananpasar.di.module.ActivityModule
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.cart.TotalCart
import com.dwi.crmjajananpasar.model.checkout.Checkout
import com.dwi.crmjajananpasar.model.customer.Customer
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.ui.activity.detail_product.DetailProductActivity
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivity
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivity.Companion.FROM_BASE
import com.dwi.crmjajananpasar.ui.activity.payment_method.PaymentMethod
import com.dwi.crmjajananpasar.ui.activity.profile.ProfileActivity
import com.dwi.crmjajananpasar.ui.activity.recomended.RecommendedActivity
import com.dwi.crmjajananpasar.ui.activity.transaction.TransactionActivity
import com.dwi.crmjajananpasar.ui.adapter.AdapterCart
import com.dwi.crmjajananpasar.ui.adapter.AdapterProductRecommended
import com.dwi.crmjajananpasar.ui.dialog.ErrorDialog
import com.dwi.crmjajananpasar.ui.dialog.LoadingDialog
import com.dwi.crmjajananpasar.util.Formatter.Companion.decimalFormat
import com.dwi.crmjajananpasar.util.SerializableSave
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.recomended_layout
import kotlinx.android.synthetic.main.activity_cart.recomended_recycleview
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CartActivity : AppCompatActivity(), CartActivityContract.View {

    // deklarasi variabel
    @Inject
    lateinit var presenter: CartActivityContract.Presenter
    lateinit var context: Context
    lateinit var customer : Customer
    val reqProductPromo : RequestListModel = RequestListModel()
    val reqRecommended : RequestListModel = RequestListModel()
    val reqCart : RequestListModel = RequestListModel()
    val reqCartTotal :Cart = Cart()
    lateinit var adapterCart : AdapterCart
    val carts : ArrayList<Cart> = ArrayList()
    lateinit var adapterRecommended : AdapterProductRecommended
    val productsRecommended : ArrayList<Product> = ArrayList()
    val reqCheckout : Checkout = Checkout()
    lateinit var loadingDialog : LoadingDialog
    lateinit var errorDialog: ErrorDialog

    // fungsi utama yg akan
    // dipanggil saat activity dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // panggil fungsi init widget
        initWidget()
    }

    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {
        this.context = this@CartActivity

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


        back_imageview.setOnClickListener {

            setResult(Activity.RESULT_OK)
            finish()
        }

        add_item_textview.setOnClickListener {
            finish()
        }
        see_more_textview.setOnClickListener {
            startActivity(Intent(context, RecommendedActivity::class.java))
        }
        buy_button.setOnClickListener {
            presenter.checkout(reqCheckout,true)
        }

        setAdapters()
        setPaginationScroll()
        requestAllData()
    }

    // mengisi nilai recycleview dengan adapter
    private fun setAdapters(){

        // mengisi nilai recycleview produk rekomendasi dengan adapter
        adapterRecommended = AdapterProductRecommended(context,productsRecommended){ product, i ->

            val intent = Intent(context, DetailProductActivity::class.java)
            intent.putExtra("product",product)
            startActivityForResult(intent,FROM_BASE)
        }
        recomended_recycleview.adapter = adapterRecommended
        recomended_recycleview.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL,false)

        // mengisi nilai recycleview keranjang dengan adapter
        adapterCart = AdapterCart(context,carts){cart, qty_flag, i ->
            presenter.oneProduct(cart.product, qty_flag, true)
            presenter.updateCart(cart,false)
        }
        cart_recycleview.adapter = adapterCart
        cart_recycleview.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
    }


    // fungsi pagination
    // yg akan dipanggil saat scroll
    // mentok kebawah
    private fun setPaginationScroll(){
        cart_nestedscrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                // pagination if user reach scroll to bottom on course
                reqCart.offset += reqCart.limit
                presenter.cart(reqCart,false)
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

        reqRecommended.categoryId = 1
        reqRecommended.recomendedValue = 2
        reqRecommended.offset = 0
        reqRecommended.limit = 10

        reqCart.customerId = customer.id
        reqCart.searchBy = "product_id"
        reqCart.orderBy = "product_id"
        reqCart.orderDir = "ASC"
        reqCart.offset = 0
        reqCart.limit = 10

        reqCheckout.customerId = customer.id
        reqCheckout.address = ""
        reqCheckout.transactionDate = "${date.get(Calendar.YEAR)}-${date.get(Calendar.MONTH) + 1}-${date.get( Calendar.DAY_OF_MONTH)}"

        reqCartTotal.customerId = customer.id
        presenter.cartTotal(reqCartTotal,false)
        presenter.cart(reqCart,true)
        presenter.productPromo(reqProductPromo,true)
    }

    override fun onOneProduct(product: Product, stockToRemove : Int) {
        if (stockToRemove == AdapterCart.QTY_ADD){
            product.stock -= 1
        } else {
            product.stock += 1
        }
        presenter.updateProduct(product,true)
    }

    override fun showProgressOneProduct(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.updating_cart))
        loadingDialog.setVisibility(show)
    }

    override fun showErrorOneProduct(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }




    override fun onUpdateProduct() {

    }

    override fun showProgressUpdateProduct(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.updating_cart))
        loadingDialog.setVisibility(show)
    }

    override fun showErrorUpdateProduct(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }


    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onProductPromo(data: ArrayList<Product>) {
        productsRecommended.addAll(data)
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
    override fun onCart(data: ArrayList<Cart>) {
        if (reqCart.offset == 0){
            carts.clear()
        }
        carts.addAll(data)
        adapterCart.notifyDataSetChanged()
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressCart(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.loading_cart))
        loadingDialog.setVisibility(show)
    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorCart(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }

    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onUpdateCart() {
        presenter.cartTotal(reqCartTotal,false)
        adapterCart.notifyDataSetChanged()
    }


    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressUpdateCart(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.updating_cart))
        loadingDialog.setVisibility(show)
    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorUpdateCart(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }

    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onCartTotal(totalCart: TotalCart) {
        total_textview.text = decimalFormat(totalCart.total)
        reqCheckout.total = totalCart.total
        buy_button.visibility = if (totalCart.total > 0) View.VISIBLE else View.GONE
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

    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onCheckout(refId: String) {
        if (refId.isNotEmpty()){

            val i = Intent(context, PaymentMethod::class.java)
            i.putExtra("ref_id",refId)
            startActivity(i)

            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressCheckout(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.checkout))
        loadingDialog.setVisibility(show)
    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorCheckout(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }

    // fungsi untuk menangkap hasil
    // yang akan merefresh
    // list keranjang
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FROM_BASE && resultCode == Activity.RESULT_OK){

            productsRecommended.clear()

            reqProductPromo.offset = 0
            reqCart.offset = 0

            presenter.recommended(reqRecommended,false)
            presenter.cart(reqCart,false)
            presenter.cartTotal(reqCartTotal,false)
        }
    }

    // fungsi saat user
    // menekan tombol back
    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
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
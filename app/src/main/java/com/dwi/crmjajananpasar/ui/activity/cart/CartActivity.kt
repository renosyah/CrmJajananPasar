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
import com.dwi.crmjajananpasar.ui.activity.profile.ProfileActivity
import com.dwi.crmjajananpasar.ui.activity.recomended.RecommendedActivity
import com.dwi.crmjajananpasar.ui.activity.transaction.TransactionActivity
import com.dwi.crmjajananpasar.ui.adapter.AdapterCart
import com.dwi.crmjajananpasar.ui.adapter.AdapterProductRecommended
import com.dwi.crmjajananpasar.util.Formatter.Companion.decimalFormat
import com.dwi.crmjajananpasar.util.SerializableSave
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.recomended_layout
import kotlinx.android.synthetic.main.activity_cart.recomended_recycleview
import javax.inject.Inject

class CartActivity : AppCompatActivity(), CartActivityContract.View {

    // deklarasi variabel
    @Inject
    lateinit var presenter: CartActivityContract.Presenter
    lateinit var context: Context
    lateinit var customer : Customer
    val reqRecommended : RequestListModel = RequestListModel()
    val reqCart : RequestListModel = RequestListModel()
    val reqCartTotal :Cart = Cart()
    lateinit var adapterCart : AdapterCart
    val carts : ArrayList<Cart> = ArrayList()
    lateinit var adapterRecommended : AdapterProductRecommended
    val productsRecommended : ArrayList<Product> = ArrayList()
    val reqCheckout : Checkout = Checkout()

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

        back_imageview.setOnClickListener {

            setResult(Activity.RESULT_OK)
            finish()
        }

        add_item_textview.setOnClickListener {
            startActivity(Intent(context,HomeActivity::class.java))
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
        adapterCart = AdapterCart(context,carts){cart, i ->

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

        reqCartTotal.customerId = customer.id
        presenter.cartTotal(reqCartTotal,false)
        presenter.cart(reqCart,true)
        presenter.recommended(reqRecommended,true)
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

    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorRecommended(e: String) {
        Toast.makeText(context,e, Toast.LENGTH_SHORT).show()
    }

    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onCart(data: ArrayList<Cart>) {
        carts.addAll(data)
        adapterCart.notifyDataSetChanged()
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressCart(show: Boolean) {

    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorCart(e: String) {
        Toast.makeText(context,e, Toast.LENGTH_SHORT).show()
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

    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorUpdateCart(e: String) {
        Toast.makeText(context,e, Toast.LENGTH_SHORT).show()
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

    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorCartTotal(e: String) {
        Toast.makeText(context,e, Toast.LENGTH_SHORT).show()
    }

    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onCheckout(refId: String) {
        if (refId.isNotEmpty()){

            val i = Intent(context, TransactionActivity::class.java)
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

    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorCheckout(e: String) {
        Toast.makeText(context,e, Toast.LENGTH_SHORT).show()
    }

    // fungsi untuk menangkap hasil
    // yang akan merefresh
    // list keranjang
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FROM_BASE && resultCode == Activity.RESULT_OK){
            reqCart.offset = 0
            presenter.cart(reqCart,false)
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
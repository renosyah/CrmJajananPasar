package com.dwi.crmjajananpasar.ui.activity.detail_product

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.di.component.DaggerActivityComponent
import com.dwi.crmjajananpasar.di.module.ActivityModule
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.customer.Customer
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.recipe.Recipe
import com.dwi.crmjajananpasar.ui.activity.recipe_detail.RecipeDetailActivity
import com.dwi.crmjajananpasar.ui.dialog.ErrorDialog
import com.dwi.crmjajananpasar.ui.dialog.LoadingDialog
import com.dwi.crmjajananpasar.util.Formatter.Companion.decimalFormat
import com.dwi.crmjajananpasar.util.SerializableSave
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class DetailProductActivity : AppCompatActivity(),DetailProductActivityContract.View {

    // deklarasi variabel
    @Inject
    lateinit var presenter: DetailProductActivityContract.Presenter
    lateinit var context: Context
    lateinit var product : Product
    val reqRecipe : RequestListModel = RequestListModel()
    private val cart : Cart = Cart()
    lateinit var loadingDialog : LoadingDialog
    lateinit var errorDialog: ErrorDialog

    // fungsi utama yg akan
    // dipanggil saat activity dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        // panggil fungsi init widget
        initWidget()
    }

    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {
        this.context = this@DetailProductActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (intent.hasExtra("product") && SerializableSave(context, SerializableSave.userDataFileSessionName).load() != null){
            val customer = SerializableSave(context, SerializableSave.userDataFileSessionName).load() as Customer
            product = intent.getSerializableExtra("product") as Product

            name_product_textview.text = product.name
            price_product_textview.text = decimalFormat(product.price)
            if (product.imageUrl != "") {
                Picasso.get()
                    .load("${product.imageUrl}")
                    .into(image_product_imageview)
            }

            cart.id = 0
            cart.customerId = customer.id
            cart.productId = product.id
            cart.product = product.clone()

            // jika produk speasial tipe promo maka
            if (product.productType == 1) {

                cart.quantity = product.defaultQty
                cart.price = product.price
                cart.subTotal = product.price
            } else {

                cart.quantity = 1
                cart.price = product.price
                cart.subTotal = cart.price * cart.quantity
            }

            add_to_cart_button.text = "${getString(R.string.add_to_cart)} ${decimalFormat(cart.subTotal)}"
        }

        loadingDialog = LoadingDialog(context)
        errorDialog = ErrorDialog(context){
            finish()
            startActivity(intent)
        }


        // jika produk speasial tipe promo maka
        remove_qty_textview.isEnabled = (product.productType == 0)
        remove_qty_textview.setOnClickListener {
            if (cart.quantity == 1) return@setOnClickListener
            cart.quantity--
            cart.subTotal = cart.price * cart.quantity
            qty_textview.text = cart.quantity.toString()
            add_to_cart_button.text = "${getString(R.string.add_to_cart)} ${decimalFormat(cart.subTotal)}"
        }

        // jika produk speasial tipe promo maka
        qty_textview.text = cart.quantity.toString()

        // jika produk speasial tipe promo maka
        add_qty_textview.isEnabled = (product.productType == 0)
        add_qty_textview.setOnClickListener {
            if (product.stock - (cart.quantity + 1) <= 0){
                Toast.makeText(context,getString(R.string.stock_is_not_enough),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            cart.quantity++
            cart.subTotal = cart.price * cart.quantity
            qty_textview.text = cart.quantity.toString()
            add_to_cart_button.text = "${getString(R.string.add_to_cart)} ${decimalFormat(cart.subTotal)}"
        }

        add_to_cart_button.setOnClickListener {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val strDate: Date = sdf.parse(product.expDate)
            if (Date().after(strDate)) {
                Toast.makeText(context,getString(R.string.product_is_expired),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            presenter.addCart(cart,true, cart.quantity)
        }

        requestAllData()
    }

    // fungsi request data
    // dan mengisi variabel
    // untuk request data list
    private fun requestAllData(){

        reqRecipe.categoryId = 1
        reqRecipe.searchBy = "product_id"
        reqRecipe.searchValue = product.id.toString()
        reqRecipe.orderBy = "id"
        reqRecipe.orderDir = "ASC"
        reqRecipe.offset = 0
        reqRecipe.limit = 10

        presenter.recipe(reqRecipe,true)
    }


    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onRecipe(data: ArrayList<Recipe>) {
        if (data.isNotEmpty()){
            var recipe : Recipe? = null
            for (i in data){
                recipe = i
            }

            var detail = product.detail
            if (product.detail.isNotEmpty() && product.detail.length > 100){
                detail = product.detail.substring(0,100)
            }

            val span1 = SpannableString("${detail}...")
            val span2 = SpannableString(getString(R.string.see_recipes))
            span2.setSpan(object : ClickableSpan(){
                override fun onClick(widget: View) {

                    val intent = Intent(context, RecipeDetailActivity::class.java)
                    intent.putExtra("recipe",recipe)
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {

                    ds.color = getColor(R.color.colorPrimaryDark)
                    ds.isUnderlineText = false

                }
            },0, span2.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            detail_product_textview.movementMethod =  LinkMovementMethod.getInstance()
            detail_product_textview.text = span1
            detail_product_textview.append(span2)
        }
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressRecipe(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.loading_recipe))
        loadingDialog.setVisibility(show)
    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorRecipe(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }

    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onAddCart(stockRemove : Int) {
        product.stock -= stockRemove
        presenter.updateProduct(product, true)
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressAddCart(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.adding_to_cart))
        loadingDialog.setVisibility(show)
    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorAddCart(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
    }

    override fun onUpdateProduct() {
        Toast.makeText(context,"${product.name} ${getString(R.string.added_to_cart)}", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun showProgressUpdateProduct(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.adding_to_cart))
        loadingDialog.setVisibility(show)
    }

    override fun showErrorUpdateProduct(e: String) {
        errorDialog.setMessage(e)
        errorDialog.setVisibility(true)
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
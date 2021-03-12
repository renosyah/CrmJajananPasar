package com.dwi.crmjajananpasar.ui.activity.recipe

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
import com.dwi.crmjajananpasar.model.recipe.Recipe
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivity
import com.dwi.crmjajananpasar.ui.activity.recipe_detail.RecipeDetailActivity
import com.dwi.crmjajananpasar.ui.adapter.AdapterRecipe
import kotlinx.android.synthetic.main.activity_recipe.*
import javax.inject.Inject

class RecipeActivity : AppCompatActivity(),RecipeActivityContract.View {

    // deklarasi variabel
    @Inject
    lateinit var presenter: RecipeActivityContract.Presenter
    lateinit var context: Context
    val reqRecipe : RequestListModel = RequestListModel()
    lateinit var adapterRecipe : AdapterRecipe
    val recipes : ArrayList<Recipe> = ArrayList()

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        // panggil fungsi init widget
        initWidget()
    }

    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {
        this.context = this@RecipeActivity

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
        adapterRecipe = AdapterRecipe(context){ recipe ->

            val intent = Intent(context,RecipeDetailActivity::class.java)
            intent.putExtra("recipe",recipe)
            startActivity(intent)
        }
        recipe_recycleview.adapter = adapterRecipe
        recipe_recycleview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    // fungsi request data
    // dan mengisi variabel
    // untuk request data list
    private fun requestAllData(){

        reqRecipe.categoryId = 1
        reqRecipe.searchBy = "product_id"
        reqRecipe.orderBy = "id"
        reqRecipe.orderDir = "ASC"
        reqRecipe.offset = 0
        reqRecipe.limit = 10

        presenter.recipe(reqRecipe,true)
    }

    // fungsi pagination
    // yg akan dipanggil saat scroll
    // mentok kebawah
    private fun setPaginationScroll(){
        recipe_nestedscrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                // pagination if user reach scroll to bottom on course
                reqRecipe.offset += reqRecipe.limit
                presenter.recipe(reqRecipe,false)
            }
        })
    }

    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onRecipe(data: ArrayList<Recipe>) {
        recipes.addAll(data)
        adapterRecipe.setItems(AdapterRecipe.toTwoProducts(recipes))
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressRecipe(show: Boolean) {

    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorRecipe(e: String) {
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
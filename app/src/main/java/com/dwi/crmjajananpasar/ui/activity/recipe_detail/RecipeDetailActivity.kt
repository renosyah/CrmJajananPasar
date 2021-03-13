package com.dwi.crmjajananpasar.ui.activity.recipe_detail

import android.content.Context
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
import com.dwi.crmjajananpasar.model.recipe_detail.RecipeDetail
import com.dwi.crmjajananpasar.ui.adapter.AdapterRecipeDetail
import com.dwi.crmjajananpasar.ui.dialog.ErrorDialog
import com.dwi.crmjajananpasar.ui.dialog.LoadingDialog
import kotlinx.android.synthetic.main.activity_recipe.back_imageview
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import javax.inject.Inject

class RecipeDetailActivity : AppCompatActivity(),RecipeDetailActivityContract.View {

    // deklarasi variabel
    @Inject
    lateinit var presenter: RecipeDetailActivityContract.Presenter
    lateinit var context: Context
    lateinit var recipe : Recipe
    val reqRecipeDetail : RequestListModel = RequestListModel()
    lateinit var adapterRecipeDetail : AdapterRecipeDetail
    val recipeDetails : ArrayList<RecipeDetail> = ArrayList()
    lateinit var loadingDialog : LoadingDialog
    lateinit var errorDialog: ErrorDialog

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        // panggil fungsi init widget
        initWidget()
    }

    // fungsi utama yg akan
    // dipanggil saat inisialisasi
    // variabel yang dideklarasi
    private fun initWidget() {
        this.context = this@RecipeDetailActivity

        if (intent.hasExtra("recipe")){
            this.recipe = intent.getSerializableExtra("recipe") as Recipe
        }

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        loadingDialog = LoadingDialog(context)
        errorDialog = ErrorDialog(context){
            finish()
            startActivity(intent)
        }
        product_name.text = recipe.product.name

        back_imageview.setOnClickListener {
            finish()
        }

        setAdapters()
        requestAllData()
        setPaginationScroll()
    }

    // mengisi nilai recycleview dengan adapter
    private fun setAdapters(){

        // mengisi nilai recycleview dengan adapter
        adapterRecipeDetail = AdapterRecipeDetail(context,recipeDetails){ recipeDetail,i  ->

        }
        recipe_detail_recycleview.adapter = adapterRecipeDetail
        recipe_detail_recycleview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }

    // fungsi request data
    // dan mengisi variabel
    // untuk request data list
    private fun requestAllData(){

        reqRecipeDetail.categoryId = 1
        reqRecipeDetail.searchBy = "recipe_id"
        reqRecipeDetail.searchValue = recipe.id.toString()
        reqRecipeDetail.orderBy = "step"
        reqRecipeDetail.orderDir = "ASC"
        reqRecipeDetail.offset = 0
        reqRecipeDetail.limit = 10

        presenter.recipeDetail(reqRecipeDetail,true)
    }

    // fungsi pagination
    // yg akan dipanggil saat scroll
    // mentok kebawah
    private fun setPaginationScroll(){
        recipe_detail_nestedscrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                // pagination if user reach scroll to bottom on course
                reqRecipeDetail.offset += reqRecipeDetail.limit
                presenter.recipeDetail(reqRecipeDetail,false)
            }
        })
    }

    // fungsi response yang nantinya akan
    // memberikan data yange berhasil diambil
    // saat request
    override fun onRecipeDetail(data: ArrayList<RecipeDetail>) {
        recipeDetails.addAll(data)
        adapterRecipeDetail.notifyDataSetChanged()
    }

    // fungsi untuk menampilkan
    // tampilan loading saat
    // nilai show bernilai true
    override fun showProgressRecipeDetail(show: Boolean) {
        loadingDialog.setMessage(getString(R.string.loading_recipe))
        loadingDialog.setVisibility(show)
    }

    // fungsi untuk menampilkan
    // tampilan error dan akan
    // memberikan variabel dengan
    // pesan yg dapat di tampilkan
    override fun showErrorRecipeDetail(e: String) {
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
package com.dwi.crmjajananpasar.ui.activity.detail_product

import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.recipe.Recipe
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

// adalah class presenter untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi yg berkaitan dengan proses request data
class DetailProductActivityPresenter : DetailProductActivityContract.Presenter {

    // deklarasi variabel
    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: DetailProductActivityContract.View
    
    override fun updateProduct(product: Product, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressUpdateProduct(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.updateProduct(product.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressUpdateProduct(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorUpdateProduct(result.Error!!)
                    }
                    if (result.Data != "") {
                        view.onUpdateProduct()
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressUpdateProduct(false)
                }
                view.showErrorUpdateProduct(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    // fungsi request yg akan dipanggil oleh view
    override fun recipe(requestListModel: RequestListModel, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressRecipe(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.allRecipe(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Recipe>>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressRecipe(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorRecipe(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onRecipe(result.Data!!)
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressRecipe(false)
                }
                view.showErrorRecipe(t.message!!)
            })

        subscriptions.add(subscribe)
    }


    // fungsi request yg akan dipanggil oleh view
    override fun addCart(cart: Cart, enableLoading: Boolean, stockRemove : Int) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressAddCart(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.addCart(cart.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressAddCart(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorAddCart(result.Error!!)
                    }
                    if (result.Data != "") {
                        view.onAddCart(stockRemove)
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressAddCart(false)
                }
                view.showErrorAddCart(t.message!!)
            })

        subscriptions.add(subscribe)
    }
    // untuk saat ini kosong
    // belum dibutuhkan
    override fun subscribe() {

    }
    // fungsi untuk membersihkan subscipsi request
    override fun unsubscribe() {
        subscriptions.clear()
    }

    // fungsi inisialisasi view
    override fun attach(view: DetailProductActivityContract.View) {
        this.view = view
    }

}
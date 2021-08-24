package com.dwi.crmjajananpasar.ui.activity.cart

import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.cart.TotalCart
import com.dwi.crmjajananpasar.model.checkout.Checkout
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.recipe.Recipe
import com.dwi.crmjajananpasar.model.transaction.Transaction
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

// adalah class presenter untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi yg berkaitan dengan proses request data
class CartActivityPresenter : CartActivityContract.Presenter {

    // deklarasi variabel
    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: CartActivityContract.View


    override fun oneProduct(product: Product,stockToRemove : Int, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressOneProduct(true)
        }
        val subscribe = api.oneProduct(product.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<Product>? ->
                if (enableLoading) {
                    view.showProgressOneProduct(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorOneProduct(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onOneProduct(result.Data!!,stockToRemove)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressOneProduct(false)
                }
                view.showErrorOneProduct(t.message!!)
            })

        subscriptions.add(subscribe)
    }

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
    override fun productPromo(requestListModel: RequestListModel, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressProductPromo(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.allProductPromo(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Product>>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressProductPromo(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorProductPromo(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onProductPromo(result.Data!!)
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressProductPromo(false)
                }
                view.showErrorProductPromo(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    // fungsi request yg akan dipanggil oleh view
    override fun recommended(requestListModel: RequestListModel, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressRecommended(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.allProductRecommended(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Product>>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressRecommended(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorRecommended(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onRecommended(result.Data!!)
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressRecommended(false)
                }
                view.showErrorRecommended(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    // fungsi request yg akan dipanggil oleh view
    override fun cart(requestListModel: RequestListModel, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressCart(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.allCart(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Cart>>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressCart(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorCart(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onCart(result.Data!!)
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressCart(false)
                }
                view.showErrorCart(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    // fungsi request yg akan dipanggil oleh view
    override fun updateCart(cart: Cart, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressUpdateCart(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.updateCart(cart.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressUpdateCart(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorUpdateCart(result.Error!!)
                    }
                    if (result.Data != "") {
                        view.onUpdateCart()
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressUpdateCart(false)
                }
                view.showErrorUpdateCart(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    // fungsi request yg akan dipanggil oleh view
    override fun cartTotal(cart: Cart, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressCartTotal(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.getTotal(cart.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<TotalCart>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressCartTotal(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorCartTotal(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onCartTotal(result.Data!!)
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressCartTotal(false)
                }
                view.showErrorCartTotal(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    // fungsi request yg akan dipanggil oleh view
    override fun checkout(c: Checkout, enableLoading :Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressCheckout(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.checkout(c.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressCheckout(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorCheckout(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onCheckout(result.Data!!)
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressCheckout(false)
                }
                view.showErrorCheckout(t.message!!)
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
    override fun attach(view: CartActivityContract.View) {
        this.view = view
    }

}
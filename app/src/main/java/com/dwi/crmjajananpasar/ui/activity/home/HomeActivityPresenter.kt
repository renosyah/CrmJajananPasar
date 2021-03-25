package com.dwi.crmjajananpasar.ui.activity.home

import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.cart.TotalCart
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

// adalah class presenter untuk activity ini
// yg mana class ini akan menghandle
// fungsi-fungsi yg berkaitan dengan proses request data
class HomeActivityPresenter : HomeActivityContract.Presenter {

    // deklarasi variabel
    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: HomeActivityContract.View


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
    override fun banner(requestListModel: RequestListModel, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressBanner(true)
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
                    view.showProgressBanner(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorBanner(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onBanner(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressBanner(false)
                }
                view.showErrorBanner(t.message!!)
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
    override fun product(requestListModel: RequestListModel, enableLoading: Boolean) {

        // check apakah loading dibutuhkan
        // jika iya tampilkan
        if (enableLoading) {
            view.showProgressProduct(true)
        }

        // membuat instance subscription
        // yg nantinya akan memanggil fungsi
        // untuk merequest data
        val subscribe = api.allProduct(requestListModel.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Product>>? ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressProduct(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorProduct(result.Error!!)
                        return@subscribe
                    }
                    if (result.Data != null) {
                        view.onProduct(result.Data!!)
                    }
                }

            }, { t: Throwable ->

                // check apakah loading dibutuhkan
                // jika iya tampilkan
                if (enableLoading) {
                    view.showProgressProduct(false)
                }
                view.showErrorProduct(t.message!!)
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
    // untuk saat ini kosong
    // belum dibutuhkan
    override fun subscribe() {

    }
    // fungsi untuk membersihkan subscipsi request
    override fun unsubscribe() {
        subscriptions.clear()
    }

    // fungsi inisialisasi view
    override fun attach(view: HomeActivityContract.View) {
        this.view = view
    }

}
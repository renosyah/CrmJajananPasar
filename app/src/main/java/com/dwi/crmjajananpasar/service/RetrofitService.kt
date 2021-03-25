package com.dwi.crmjajananpasar.service

import com.dwi.crmjajananpasar.BuildConfig
import com.dwi.crmjajananpasar.model.RequestListModel
import com.dwi.crmjajananpasar.model.ResponseModel
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.cart.TotalCart
import com.dwi.crmjajananpasar.model.checkout.Checkout
import com.dwi.crmjajananpasar.model.customer.Customer
import com.dwi.crmjajananpasar.model.payment.Payment
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.model.recipe.Recipe
import com.dwi.crmjajananpasar.model.recipe_detail.RecipeDetail
import com.dwi.crmjajananpasar.model.transaction.Transaction
import com.dwi.crmjajananpasar.model.uploadResponse.UploadResponse
import com.dwi.crmjajananpasar.model.validateTransaction.ValidateTransaction
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.*

interface RetrofitService {

    // add more end point to access
    @POST("api/customer/login.php")
    fun login(@Body customer: Customer): Observable<ResponseModel<Customer>>

    @POST("api/customer/add.php")
    fun register(@Body customer: Customer): Observable<ResponseModel<Customer>>

    @POST("api/product/list.php")
    fun allProduct(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Product>>>

    @POST("api/product/list_promo.php")
    fun allProductPromo(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Product>>>

    @POST("api/product/list_recomended.php")
    fun allProductRecommended(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Product>>>

    @POST("api/product/list_favourite.php")
    fun allProductFavourite(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Product>>>

    @POST("api/recipe/list.php")
    fun allRecipe(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Recipe>>>

    @POST("api/recipe_detail/list.php")
    fun allRecipeDetail(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<RecipeDetail>>>

    @POST("api/payment/list.php")
    fun allPayment(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Payment>>>

    @POST("api/payment/one.php")
    fun onePayment(@Body payment: Payment): Observable<ResponseModel<Payment>>

    @POST("api/cart/add.php")
    fun addCart(@Body cart : Cart): Observable<ResponseModel<String>>

    @POST("api/cart/list_detail.php")
    fun allCart(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Cart>>>

    @POST("api/cart/update.php")
    fun updateCart(@Body cart : Cart): Observable<ResponseModel<String>>

    @POST("api/cart/delete.php")
    fun deleteCart(@Body cart : Cart): Observable<ResponseModel<String>>

    @POST("api/cart/total.php")
    fun getTotal(@Body cart : Cart): Observable<ResponseModel<TotalCart>>

    @POST("api/transaction/one_by_ref.php")
    fun oneTransactionByRef(@Body transaction: Transaction): Observable<ResponseModel<Transaction>>

    @POST("api/checkout/add.php")
    fun checkout(@Body checkout: Checkout): Observable<ResponseModel<String>>

    @POST("api/validate_transaction/add.php")
    fun addValidateTransaction(@Body validateTransaction: ValidateTransaction): Observable<ResponseModel<String>>

    @Multipart
    @POST("/api/upload_file.php")
    fun upload(@Part file: MultipartBody.Part): Observable<ResponseModel<UploadResponse>>

    companion object {

        fun create() : RetrofitService {

            // membuat instance retrofit
            // yg nantinya ini yg akan digunakan untuk
            // melakukan request ke api
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.SERVER_URL)
                .build()

            // balikan instance
            // sebagai nilai balik
            return retrofit.create(RetrofitService::class.java)
        }
    }
}
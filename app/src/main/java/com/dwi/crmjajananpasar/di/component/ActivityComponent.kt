package com.dwi.crmjajananpasar.di.component

import com.dwi.crmjajananpasar.di.module.ActivityModule
import com.dwi.crmjajananpasar.model.recipe_detail.RecipeDetail
import com.dwi.crmjajananpasar.ui.activity.cart.CartActivity
import com.dwi.crmjajananpasar.ui.activity.detail_product.DetailProductActivity
import com.dwi.crmjajananpasar.ui.activity.favourite.FavouriteActivity
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivity
import com.dwi.crmjajananpasar.ui.activity.login.LoginActivity
import com.dwi.crmjajananpasar.ui.activity.recipe.RecipeActivity
import com.dwi.crmjajananpasar.ui.activity.recipe_detail.RecipeDetailActivity
import com.dwi.crmjajananpasar.ui.activity.recomended.RecommendedActivity
import com.dwi.crmjajananpasar.ui.activity.register.RegisterActivity
import com.dwi.crmjajananpasar.ui.activity.transaction.TransactionActivity
import com.dwi.crmjajananpasar.ui.activity.upload.UploadActivity
import com.dwi.crmjajananpasar.ui.adapter.AdapterProductRecommended
import dagger.Component

// ini adalah interface komponen aktivity
// agar fungsi inject dapat dipanggil
// maka fungsi tersebut sebelumnya harus didelarasi
// di interface ini
@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    // fungsi yg akan digunakan untuk diinject di activity login
    fun inject(loginActivity: LoginActivity)

    // fungsi yg akan digunakan untuk diinject di activity register
    fun inject(registerActivity: RegisterActivity)

    // fungsi yg akan digunakan untuk diinject di activity home
    fun inject(homeActivity: HomeActivity)

    // fungsi yg akan digunakan untuk diinject di activity favourite
    fun inject(favouriteActivity: FavouriteActivity)

    // fungsi yg akan digunakan untuk diinject di activity recipe
    fun inject(recipeActivity: RecipeActivity)

    // fungsi yg akan digunakan untuk diinject di activity recipeDetailActivity
    fun inject(recipeDetailActivity : RecipeDetailActivity)

    // fungsi yg akan digunakan untuk diinject di activity RecommendedActivity
    fun inject(recommendedActivity: RecommendedActivity)

    // fungsi yg akan digunakan untuk diinject di activity RecommendedActivity
    fun inject(detailProductActivity: DetailProductActivity)

    // fungsi yg akan digunakan untuk diinject di activity CartActivity
    fun inject(cartActivity: CartActivity)

    // fungsi yg akan digunakan untuk diinject di activity TransactionActivity
    fun inject(transactionActivity: TransactionActivity)

    // fungsi yg akan digunakan untuk diinject di activity  UploadActivity
    fun inject(uploadActivity: UploadActivity)
}
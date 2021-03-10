package com.dwi.crmjajananpasar.di.module

import android.app.Activity
import com.dwi.crmjajananpasar.service.RetrofitService
import com.dwi.crmjajananpasar.ui.activity.cart.CartActivityContract
import com.dwi.crmjajananpasar.ui.activity.cart.CartActivityPresenter
import com.dwi.crmjajananpasar.ui.activity.detail_product.DetailProductActivity
import com.dwi.crmjajananpasar.ui.activity.detail_product.DetailProductActivityContract
import com.dwi.crmjajananpasar.ui.activity.detail_product.DetailProductActivityPresenter
import com.dwi.crmjajananpasar.ui.activity.favourite.FavouriteActivityContract
import com.dwi.crmjajananpasar.ui.activity.favourite.FavouriteActivityPresenter
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivityContract
import com.dwi.crmjajananpasar.ui.activity.home.HomeActivityPresenter
import com.dwi.crmjajananpasar.ui.activity.login.LoginActivityContract
import com.dwi.crmjajananpasar.ui.activity.login.LoginActivityPresenter
import com.dwi.crmjajananpasar.ui.activity.recipe.RecipeActivityContract
import com.dwi.crmjajananpasar.ui.activity.recipe.RecipeActivityPresenter
import com.dwi.crmjajananpasar.ui.activity.recipe_detail.RecipeDetailActivityContract
import com.dwi.crmjajananpasar.ui.activity.recipe_detail.RecipeDetailActivityPresenter
import com.dwi.crmjajananpasar.ui.activity.recomended.RecommendedActivityContract
import com.dwi.crmjajananpasar.ui.activity.recomended.RecommendedActivityPresenter
import com.dwi.crmjajananpasar.ui.activity.register.RegisterActivityContract
import com.dwi.crmjajananpasar.ui.activity.register.RegisterActivityPresenter
import com.dwi.crmjajananpasar.ui.activity.transaction.TransactionActivityContract
import com.dwi.crmjajananpasar.ui.activity.transaction.TransactionActivityPresenter
import com.dwi.crmjajananpasar.ui.activity.upload.UploadActivityContract
import com.dwi.crmjajananpasar.ui.activity.upload.UploadActivityPresenter
import dagger.Module
import dagger.Provides

// ini adalah class dimana
// setiap melakukan injecksi
// presenter ke activity
// maka akan di provide presenter
// untuk aktivity yg bersangkutan
@Module
class ActivityModule(private var activity : Activity) {

    // fungsi untuk provide activity
    // dengan nilai balik adalah variabel activity
    // yg telah diinisialisasi
    @Provides
    fun provideActivity() : Activity {
        return activity
    }

    @Provides
    fun provideApiService(): RetrofitService {
        return RetrofitService.create()
    }

    // fungsi untuk provide presenter pada activity login
    @Provides
    fun loginActivityPresenter(): LoginActivityContract.Presenter {
        return LoginActivityPresenter()
    }

    // fungsi untuk provide presenter pada activity register
    @Provides
    fun registerActivityPresenter(): RegisterActivityContract.Presenter {
        return RegisterActivityPresenter()
    }

    // fungsi untuk provide presenter pada activity home
    @Provides
    fun homeActivityPresenter(): HomeActivityContract.Presenter {
        return HomeActivityPresenter()
    }

    // fungsi untuk provide presenter pada activity favourite
    @Provides
    fun favouriteActivityPresenter(): FavouriteActivityContract.Presenter {
        return FavouriteActivityPresenter()
    }

    // fungsi untuk provide presenter pada activity Recipe
    @Provides
    fun recipeActivityPresenter(): RecipeActivityContract.Presenter {
        return RecipeActivityPresenter()
    }

    // fungsi untuk provide presenter pada activity Recipe detail
    @Provides
    fun recipeDetailActivityPresenter(): RecipeDetailActivityContract.Presenter {
        return RecipeDetailActivityPresenter()
    }

    // fungsi untuk provide presenter pada activity Recipe detail
    @Provides
    fun recommendedActivityPresenter(): RecommendedActivityContract.Presenter {
        return RecommendedActivityPresenter()
    }

    // fungsi untuk provide presenter pada activity detail product
    @Provides
    fun detailProductActivityPresenter(): DetailProductActivityContract.Presenter {
        return DetailProductActivityPresenter()
    }

    // fungsi untuk provide presenter pada activity cart
    @Provides
    fun cartActivityPresenter(): CartActivityContract.Presenter {
        return CartActivityPresenter()
    }

    // fungsi untuk provide presenter pada activity Transaction
    @Provides
    fun transactionActivityPresenter(): TransactionActivityContract.Presenter {
        return TransactionActivityPresenter()
    }

    // fungsi untuk provide presenter pada activity Transaction
    @Provides
    fun uploadActivityPresenter(): UploadActivityContract.Presenter {
        return UploadActivityPresenter()
    }
}
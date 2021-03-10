package com.dwi.crmjajananpasar.di.module

import android.app.Application
import com.dwi.crmjajananpasar.BaseApp
import com.dwi.crmjajananpasar.di.scope.PerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

// ini adalah class dimana
// setiap melakukan injecksi
// presenter ke activity
// maka akan di provide presenter
// untuk aktivity yg bersangkutan
// namun kali ini disederhanakan karna
// yg diinject adalah base
@Module
class ApplicationModule(private val baseApp: BaseApp) {

    // fungsi untuk provide activity
    // dengan nilai balik adalah variabel base app
    // yg telah diinisialisasi
    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application {
        return baseApp
    }
}
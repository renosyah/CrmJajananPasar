package com.dwi.crmjajananpasar.di.component

import com.dwi.crmjajananpasar.BaseApp
import com.dwi.crmjajananpasar.di.module.ApplicationModule
import dagger.Component

// ini adalah interface komponen base
// agar fungsi inject dapat dipanggil
// maka fungsi tersebut sebelumnya harus didelarasi
// di interface ini
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: BaseApp)
}
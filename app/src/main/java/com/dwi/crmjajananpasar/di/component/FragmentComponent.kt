package com.dwi.crmjajananpasar.di.component

import com.dwi.crmjajananpasar.di.module.FragmentModule
import dagger.Component

// ini adalah interface komponen fragment
// agar fungsi inject dapat dipanggil
// maka fungsi tersebut sebelumnya harus didelarasi
// di interface ini
@Component(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {
    // add for each new fragment
}
package com.dwi.crmjajananpasar

import android.app.Application
import com.dwi.crmjajananpasar.di.component.ApplicationComponent
import com.dwi.crmjajananpasar.di.component.DaggerApplicationComponent
import com.dwi.crmjajananpasar.di.module.ApplicationModule
import com.squareup.picasso.BuildConfig


class BaseApp : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        setup()

        if (BuildConfig.DEBUG) {
        }
    }

    fun setup() {
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this)).build()
        component.inject(this)
    }

    companion object {
        lateinit var instance: BaseApp private set
    }
}
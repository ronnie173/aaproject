package com.appsian.aaproject

import android.app.Application
import com.appsian.aaproject.di.daoModules
import com.appsian.aaproject.di.reposModule
import com.appsian.aaproject.di.setupModule
import com.appsian.aaproject.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
class ApplicationController:Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ApplicationController)
            modules(listOf(setupModule, reposModule ,viewModelModule,daoModules))
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
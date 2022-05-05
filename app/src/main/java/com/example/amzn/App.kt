package com.example.amzn

import android.os.Handler
import android.os.Looper
import androidx.multidex.MultiDexApplication
import com.example.amzn.BuildConfig
import co.early.fore.kt.core.delegate.DebugDelegateDefault
import co.early.fore.kt.core.delegate.Fore
import com.example.amzn.data.iap.AmazonIapListener
import com.example.amzn.di.dataModule
import com.example.amzn.di.domainModule
import com.example.amzn.di.uiModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Copyright © 2019 early.co. All rights reserved.
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        inst = this

        if (BuildConfig.DEBUG) {
            Fore.setDelegate(DebugDelegateDefault(tagPrefix = "amzn_"))
        }

        startKoin {
            if (BuildConfig.DEBUG) {
                // Use Koin Android Logger
                androidLogger()
            }
            // declare Android context
            androidContext(this@App)
            // declare modules to use
            modules(
                listOf(
                    dataModule,
                    domainModule,
                    uiModule
                )
            )
        }

        init()
    }

    companion object {
        lateinit var inst: App private set

        fun init() {
            // run any initialisation code here

            // simulate a bit of a delay
            Handler(Looper.getMainLooper()).postDelayed({

                Fore.getLogger().i("registering with amazon purchasing service")

                val purchasingService = inst.get() as com.example.amzn.domain.iap.PurchasingService
                purchasingService.registerListener(inst, inst.get() as AmazonIapListener)
                purchasingService.getProductData(setOf("com.amazon.sample.iap.consumable.orange"))
            },1000)
        }
    }
}

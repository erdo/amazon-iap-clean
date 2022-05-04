package com.example.amzn

import androidx.multidex.MultiDexApplication
import amzn.BuildConfig
import co.early.fore.kt.core.delegate.DebugDelegateDefault
import co.early.fore.kt.core.delegate.Fore
import com.example.amzn.di.dataModule
import com.example.amzn.di.domainModule
import com.example.amzn.di.uiModule
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

//            val persista: PerSista = inst.get()
//            persista.wipeEverything {}
        }
    }
}

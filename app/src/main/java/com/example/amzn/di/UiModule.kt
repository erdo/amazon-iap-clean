package com.example.amzn.di

import com.example.amzn.App
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val uiModule = module(override = true) {

    /**
     * Misc
     */

    single { androidContext() as App }

    /**
     * ViewModel
     */

}

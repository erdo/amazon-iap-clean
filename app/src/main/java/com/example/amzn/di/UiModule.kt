package com.example.amzn.di

import com.example.amzn.App
import com.example.amzn.ui.dashboard.DashboardViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module(override = true) {

    /**
     * Misc
     */

    single { androidContext() as App }

    /**
     * ViewModel
     */

    viewModel {
        DashboardViewModel(
            weatherModel = get(),
            refreshModel = get()
        )
    }
}

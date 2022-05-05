package com.example.amzn.di

import co.early.fore.core.time.SystemTimeWrapper
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.net.InterceptorLogging
import co.early.fore.kt.net.ktor.CallProcessorKtor
import co.early.persista.PerSista
import com.example.amzn.App
import com.example.amzn.data.api.ktor.CustomKtorBuilder
import com.example.amzn.data.api.ktor.ErrorHandler
import com.example.amzn.data.api.ktor.GlobalRequestInterceptor
import com.example.amzn.data.iap.AmazonFulfillment
import com.example.amzn.data.iap.AmazonIapListener
import com.example.amzn.data.iap.AmazonPurchasingServiceImp
import com.example.amzn.domain.iap.BoughtItems
import com.example.amzn.domain.iap.PurchasingService
import org.koin.dsl.module

val dataModule = module(override = true) {

    /**
     * Amazon IAP
     */

    single {
        AmazonIapListener(
            productCatalogue = get(),
            amazonFulfillment = get(),
            userOwned = (get() as BoughtItems),
        )
    }

    single {
        AmazonFulfillment(
            boughtItems = get(),
            productCatalogue = get(),
            purchasingService = get()
        )
    }

    single<PurchasingService> {
        AmazonPurchasingServiceImp()
    }

    /**
     * Ktor
     */

    single {
        CustomKtorBuilder.create(
            GlobalRequestInterceptor(),
            InterceptorLogging()
        )//logging interceptor should be the last one
    }

    single {
        CallProcessorKtor(
            ErrorHandler(get())
        )
    }

    /**
     * Persistence
     */

    single {
        PerSista(
            dataDirectory = (get() as App).filesDir,
            logger = get()
        )
    }

    /**
     * Network Services
     */

    /**
     * Misc Data
     */

    single {
        SystemTimeWrapper()
    }

    single {
        Fore.getLogger()
    }
}

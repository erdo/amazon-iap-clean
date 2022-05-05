package com.example.amzn.di

import com.example.amzn.domain.iap.BoughtItems
import com.example.amzn.domain.iap.ProductCatalogue
import org.koin.dsl.module

val domainModule = module(override = true) {

    /**
     * Amazon IAP
     */

    single {
        BoughtItems()
    }

    single {
        ProductCatalogue()
    }

    /**
     * Models and Mediators
     */

}

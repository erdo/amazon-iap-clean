package com.example.amzn.di

import com.example.amzn.domain.iap.BoughtItems
import com.example.amzn.domain.iap.ProductCatalogue
import com.example.amzn.domain.refresher.OnRefreshMediator
import com.example.amzn.domain.refresher.RefreshModel
import com.example.amzn.domain.weather.WeatherModel
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

    single {
        RefreshModel(
            onRefreshMediator = get(),
            refreshIntervalMilliSeconds = 10000,
            systemTimeWrapper = get(),
            logger = get()
        )
    }

    single {
        OnRefreshMediator(get())
    }

    single {
        WeatherModel(
            pollenService = get(),
            temperatureService = get(),
            windSpeedService = get(),
            perSista = get(),
            logger = get()
        )
    }
}

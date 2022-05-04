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
import com.example.amzn.data.api.ktor.services.pollen.PollenApi
import com.example.amzn.data.api.ktor.services.pollen.PollenServiceImp
import com.example.amzn.data.api.ktor.services.temperature.TemperatureApi
import com.example.amzn.data.api.ktor.services.temperature.TemperatureServiceImp
import com.example.amzn.data.api.ktor.services.windspeed.WindSpeedApi
import com.example.amzn.data.api.ktor.services.windspeed.WindSpeedServiceImp
import com.example.amzn.domain.weather.PollenService
import com.example.amzn.domain.weather.TemperatureService
import com.example.amzn.domain.weather.WindSpeedService
import org.koin.dsl.module

@OptIn(ExperimentalStdlibApi::class)
val dataModule = module(override = true) {

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

    single<TemperatureService> {
        TemperatureServiceImp(
            client = TemperatureApi.create(get()),
            processor = get(),
            logger = get()
        )
    }

    single<WindSpeedService> {
        WindSpeedServiceImp(
            client = WindSpeedApi.create(get()),
            processor = get(),
            logger = get()
        )
    }

    single<PollenService> {
        PollenServiceImp(
            client = PollenApi.create(get()),
            processor = get(),
            logger = get()
        )
    }

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

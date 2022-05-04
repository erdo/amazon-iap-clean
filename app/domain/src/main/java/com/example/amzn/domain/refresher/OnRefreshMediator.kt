package com.example.amzn.domain.refresher

import com.example.amzn.domain.weather.WeatherModel


class OnRefreshMediator(
    private val weatherModel: WeatherModel,
) {
    fun refreshNow() {
        weatherModel.fetchWeatherReport()
    }
}

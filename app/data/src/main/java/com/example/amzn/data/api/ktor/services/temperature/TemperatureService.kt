package com.example.amzn.data.api.ktor.services.temperature

import co.early.fore.kt.core.Either
import co.early.fore.kt.core.logging.Logger
import co.early.fore.kt.net.ktor.CallProcessorKtor
import com.example.amzn.data.api.DataError
import com.example.amzn.data.api.toDomain
import com.example.amzn.domain.DomainError
import com.example.amzn.domain.weather.Temperature
import com.example.amzn.domain.weather.TemperatureService

class TemperatureServiceImp(
    private val client: TemperatureApi,
    private val processor: CallProcessorKtor<DataError>,
    private val logger: Logger,
) : TemperatureService {

    override suspend fun getTemperatures(): Either<DomainError, List<Temperature>> {

        val dataResult = processor.processCallAwait {
            logger.i("processing call t:" + Thread.currentThread())
            client.getTemperatureReadings()
        }

        val domainResult = toDomain(dataResult){
            it.map { temperaturePojo ->
                temperaturePojo.toDomain()
            }
        }

        return domainResult
    }
}

fun TemperaturePojo.toDomain(): Temperature {
    return Temperature(
        maxTempC = this.maxTempC,
        minTempC = this.minTempC,
    )
}

package com.example.amzn.data.api.ktor.services.pollen

import co.early.fore.kt.core.Either
import co.early.fore.kt.core.logging.Logger
import co.early.fore.kt.net.ktor.CallProcessorKtor
import com.example.amzn.data.api.DataError
import com.example.amzn.data.api.toDomain
import com.example.amzn.domain.DomainError
import com.example.amzn.domain.weather.PollenCount
import com.example.amzn.domain.weather.PollenService

class PollenServiceImp(
    private val client: PollenApi,
    private val processor: CallProcessorKtor<DataError>,
    private val logger: Logger,
) : PollenService {

    override suspend fun getPollenCounts(): Either<DomainError, List<PollenCount>> {

        val dataResult = processor.processCallAwait {
            logger.i("processing call t:" + Thread.currentThread())
            client.getPollenCountReadings()
        }

        val domainResult = toDomain(dataResult){
            it.map { pollenPojo ->
                pollenPojo.toDomain()
            }
        }

        return domainResult
    }
}

fun PollenPojo.toDomain(): PollenCount {
    return PollenCount(this.level.domainPollenLevel)
}

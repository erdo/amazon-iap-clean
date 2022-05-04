package com.example.amzn.data.api

import com.example.amzn.domain.DomainError
import com.example.amzn.domain.DomainError.*

sealed class DataError(val resolution: DomainError) {
    object Misc: DataError(RETRY_LATER)
    object Network: DataError(CHECK_NETWORK_THEN_RETRY)
    object SecurityUnknown: DataError(CHECK_NETWORK_THEN_RETRY)
    object Server: DataError(RETRY_LATER)
    object AlreadyExecuted: DataError(RETRY_LATER)
    object Client: DataError(RETRY_LATER)
    object RateLimited: DataError(RETRY_LATER)
    object SessionTimedOut: DataError(LOGIN_THEN_RETRY)
    object Busy: DataError(RETRY_LATER)
    object LaunchServiceSaysNo: DataError(RETRY_LATER)
    object WeatherUserLocked: DataError(RETRY_LATER)
    object WeatherLoginIncorrect: DataError(LOGIN_THEN_RETRY)
}

package com.example.amzn.domain

import kotlinx.serialization.Serializable

@Serializable
enum class DomainError {
    RETRY_LATER,
    CHECK_NETWORK_THEN_RETRY,
    LOGIN_THEN_RETRY,
}

package com.example.amzn.data.api

import co.early.fore.kt.core.Either
import com.example.amzn.domain.DomainError

fun <Data, Domain> toDomain(
    dataEither: Either<DataError, Data>,
    toAppBlock: (Data) -> Domain) : Either<DomainError, Domain> {
    return when (dataEither) {
        is Either.Right -> Either.right(toAppBlock(dataEither.b))
        is Either.Left -> Either.left(dataEither.a.resolution)
    }
}

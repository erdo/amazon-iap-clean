package com.example.amzn.data.iap

import com.amazon.device.iap.model.FulfillmentResult
import com.amazon.device.iap.model.Product
import com.amazon.device.iap.model.ProductType
import com.example.amzn.domain.iap.FulfillmentRes

fun Product.toDomain(): com.example.amzn.domain.iap.Product {
    return com.example.amzn.domain.iap.Product(
        sku = sku,
        productType = productType.toDomain(),
        description = description,
        price = price,
        smallIconUrl = smallIconUrl,
        title = title,
    )
}

fun ProductType.toDomain(): com.example.amzn.domain.iap.ProductType {
    return when (this) {
        ProductType.CONSUMABLE -> com.example.amzn.domain.iap.ProductType.CONSUMABLE
        ProductType.ENTITLED -> com.example.amzn.domain.iap.ProductType.ENTITLED
        ProductType.SUBSCRIPTION -> com.example.amzn.domain.iap.ProductType.SUBSCRIPTION
    }
}

fun Map<String, Product>.toDomain(): Map<String, com.example.amzn.domain.iap.Product> {
    return mapValues { it.value.toDomain() }
}

fun FulfillmentRes.toData(): FulfillmentResult{
    return when (this){
        FulfillmentRes.FULFILLED -> FulfillmentResult.FULFILLED
        FulfillmentRes.UNAVAILABLE -> FulfillmentResult.UNAVAILABLE
    }
}
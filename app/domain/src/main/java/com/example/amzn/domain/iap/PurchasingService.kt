package com.example.amzn.domain.iap

interface PurchasingService {
    fun registerListener(application: Any, purchasingListener: Any)
    fun getUserData()
    fun getPurchaseUpdates(boolean: Boolean) // TODO what is this boolean for?
    fun getProductData(skus: Set<String>)
    fun purchase(sku: String)
    fun notifyFulfillment(receiptId: String, result: FulfillmentRes)
}
package com.example.amzn.data.iap

import android.app.Application
import co.early.fore.kt.core.delegate.Fore
import com.amazon.device.iap.PurchasingService
import com.example.amzn.domain.iap.FulfillmentRes

class AmazonPurchasingServiceImp: com.example.amzn.domain.iap.PurchasingService {

    private var listenerRegistered = false

    override fun registerListener(application: Any, purchasingListener: Any) {
        Fore.getLogger().i("registerListener()")
        PurchasingService.registerListener(
            application as Application,
            purchasingListener as AmazonIapListener
        )
        listenerRegistered = true
        getUserData()
        getPurchaseUpdates(false)
    }

    override fun getUserData() {
        Fore.getLogger().i("getUserData() listenerRegistered:$listenerRegistered")
        if (listenerRegistered) {
            PurchasingService.getUserData()
        }
    }

    override fun getPurchaseUpdates(boolean: Boolean) {
        Fore.getLogger().i("getPurchaseUpdates() listenerRegistered:$listenerRegistered")
        if (listenerRegistered) {
            PurchasingService.getPurchaseUpdates(boolean)
        }
    }

    override fun getProductData(skus: Set<String>) {
        Fore.getLogger().i("getProductData() listenerRegistered:$listenerRegistered")
        if (listenerRegistered) {
            PurchasingService.getProductData(skus)
        }
    }

    override fun purchase(sku: String) {
        Fore.getLogger().i("purchase() listenerRegistered:$listenerRegistered")
        if (listenerRegistered) {
            PurchasingService.purchase(sku)
        }
    }

    override fun notifyFulfillment(receiptId: String, result: FulfillmentRes) {
        Fore.getLogger().i("notifyFulfillment() listenerRegistered:$listenerRegistered")
        if (listenerRegistered) {
            PurchasingService.notifyFulfillment(receiptId, result.toData())
        }
    }
}

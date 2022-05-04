package com.example.amzn.data.iap

import co.early.fore.kt.core.delegate.Fore
import com.amazon.device.iap.PurchasingListener
import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.ProductDataResponse
import com.amazon.device.iap.model.PurchaseResponse
import com.amazon.device.iap.model.PurchaseResponse.RequestStatus.ALREADY_PURCHASED
import com.amazon.device.iap.model.PurchaseResponse.RequestStatus.FAILED
import com.amazon.device.iap.model.PurchaseResponse.RequestStatus.INVALID_SKU
import com.amazon.device.iap.model.PurchaseResponse.RequestStatus.NOT_SUPPORTED
import com.amazon.device.iap.model.PurchaseUpdatesResponse
import com.amazon.device.iap.model.PurchaseUpdatesResponse.RequestStatus.SUCCESSFUL as UpdateSUCCESSFUL
import com.amazon.device.iap.model.UserDataResponse
import com.example.amzn.domain.iap.ProductCatalogue
import com.example.amzn.domain.iap.UserOwned
import com.amazon.device.iap.model.UserDataResponse.RequestStatus.SUCCESSFUL as UserSUCCESSFUL
import com.amazon.device.iap.model.ProductDataResponse.RequestStatus.SUCCESSFUL as ProductSUCCESSFUL
import com.amazon.device.iap.model.PurchaseResponse.RequestStatus.SUCCESSFUL as PurchaseSUCCESSFUL

/**
 * Main listener class for Amazon IAP, based on what we receive here we interact with our system, eg
 * fulfilling purchases, clearing data etc
 */
class AmazonIapListener(
    private val productCatalogue: ProductCatalogue,
    private val amazonFulfillment: AmazonFulfillment,
    private val userOwned: UserOwned,
) : PurchasingListener {

    override fun onUserDataResponse(userDataResponse: UserDataResponse?) {

        Fore.getLogger().i(
            "onUserDataResponse() requestId:${userDataResponse?.requestId} " +
                "requestStatus:${userDataResponse?.requestStatus}"
        )

        userDataResponse?.apply {
            when (requestStatus) {
                UserSUCCESSFUL -> userOwned.setNewUser(userData.userId, userData.marketplace)
                else -> userOwned.clearOranges()
            }
        }
    }

    override fun onProductDataResponse(productDataResponse: ProductDataResponse?) {

        Fore.getLogger().i(
            "onProductDataResponse() requestId:${productDataResponse?.requestId} " +
                "requestStatus:${productDataResponse?.requestStatus}"
        )

        productDataResponse?.apply {
            when (requestStatus) {
                ProductSUCCESSFUL -> {
                    productCatalogue.enablePurchaseForSkus(productData.toDomain())
                    productCatalogue.disablePurchaseForSkus(unavailableSkus)
                }
                else -> productCatalogue.disableAllPurchases()
            }
        }
    }

    override fun onPurchaseResponse(purchaseResponse: PurchaseResponse?) {

        Fore.getLogger().i(
            "onPurchaseResponse() requestId:${purchaseResponse?.requestId} " +
                "requestStatus:${purchaseResponse?.requestStatus} " +
                "[for userId:${purchaseResponse?.userData?.userId}]"
        )

        purchaseResponse?.apply {
            when (requestStatus) {
                PurchaseSUCCESSFUL -> {
                    Fore.getLogger().d("RECEIPT:" + receipt.toJSON())
                    userOwned.setNewUser(userData.userId, userData.marketplace)
                    amazonFulfillment.handleReceipt(receipt, userData)
                }
                ALREADY_PURCHASED -> Fore.getLogger().w("already purchased," +
                    "should never get here for a consumable")
                INVALID_SKU -> {
                    Fore.getLogger().w("invalid SKU, product should not have been available to buy")
                    productCatalogue.disablePurchaseForSkus(setOf(receipt.sku))
                }
                FAILED, NOT_SUPPORTED, null -> {
                    amazonFulfillment.purchaseFailed(receipt.sku)
                }
            }
        }
    }

    override fun onPurchaseUpdatesResponse(purchaseUpdatesResponse: PurchaseUpdatesResponse?) {

        Fore.getLogger().i(
            "onPurchaseUpdatesResponse() requestId:${purchaseUpdatesResponse?.requestId} " +
                "requestStatus:${purchaseUpdatesResponse?.requestStatus} " +
                "[for userId:${purchaseUpdatesResponse?.userData?.userId}]"
        )

        purchaseUpdatesResponse?.apply {
            when (requestStatus) {
                UpdateSUCCESSFUL -> {
                    userOwned.setNewUser(userData.userId, userData.marketplace)
                    for (receipt in receipts) {
                        amazonFulfillment.handleReceipt(receipt, userData)
                    }
                    if (hasMore()) {
                        PurchasingService.getPurchaseUpdates(false) // this is weird? but it's what the sample does, maybe it should be: if(!hasMore())
                    }
                }
                else -> productCatalogue.disableAllPurchases()
            }
        }
    }
}

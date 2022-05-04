package com.example.amzn.data.iap

import co.early.fore.kt.core.delegate.Fore
import com.amazon.device.iap.model.ProductType
import com.amazon.device.iap.model.Receipt
import com.amazon.device.iap.model.UserData
import com.example.amzn.domain.iap.BoughtItems
import com.example.amzn.domain.iap.FulfillmentRes
import com.example.amzn.domain.iap.ProductCatalogue
import com.example.amzn.domain.iap.PurchasingService

/**
 * What we actually do when a purchase is Completed, paid for or Revoked etc
 */
class AmazonFulfillment(
    private val boughtItems: BoughtItems,
    private val productCatalogue: ProductCatalogue,
    private val purchasingService: PurchasingService,
) {

    val fulfilledReceiptIds = mutableListOf<String>()

    /**
     * Method to handle the receipt, based on the receipt received from InAppPurchase SDK's
     * [PurchasingListener.onPurchaseResponse] or
     * [PurchasingListener.onPurchaseUpdatesResponse] method.
     *
     * @param receipt
     * @param userData
     */
    fun handleReceipt(receipt: Receipt, userData: UserData) {
        Fore.getLogger().i("handleReceipt() receiptId:${receipt.receiptId}")
        when (receipt.productType) {
            ProductType.CONSUMABLE -> handleConsumablePurchase(receipt, userData)
            ProductType.ENTITLED -> {}
            ProductType.SUBSCRIPTION -> {}
            null -> {}
        }
    }

    private fun handleConsumablePurchase(receipt: Receipt, userData: UserData) {
        if (receipt.isCanceled) {
            revokeConsumablePurchase(receipt, userData)
        } else {
            if (!verifyReceiptFromYourService(receipt.receiptId, userData)) {
                purchasingService.notifyFulfillment(
                    receipt.receiptId,
                    FulfillmentRes.UNAVAILABLE
                )
            } else if (receiptAlreadyFulfilled(receipt.receiptId, userData)) {
                // if the receipt was fulfilled before, just notify Amazon
                // Appstore it's Fulfilled again.
                purchasingService.notifyFulfillment(
                    receipt.receiptId,
                    FulfillmentRes.FULFILLED
                )
            } else {
                grantConsumablePurchase(receipt, userData)
            }
        }
    }

    private fun grantConsumablePurchase(receipt: Receipt, userData: UserData) {

        Fore.getLogger().i(
            "grantConsumablePurchase() receipt:${receipt.receiptId} " +
                "sku:${receipt.sku}"
        )

        val product = productCatalogue.getProductForSku(receipt.sku)

        if (product != null && product.enabled) {
//                if (verifyReceiptFromYourService(receipt, userData)){
            boughtItems.addNewOrange() // TODO here we actually download the updated capabilities from the server rather than update locally?
            purchasingService.notifyFulfillment(
                receipt.receiptId,
                FulfillmentRes.FULFILLED
            )
            fulfilledReceiptIds.add(receipt.receiptId)
//                } else {
//                    // no longer available
//                    purchasingService.notifyFulfillment(
//                        receipt.receiptId,
//                        FulfillmentRes.UNAVAILABLE
//                    )
//                    // or failed to connect
//                    // do nothing, maybe alert user on UI, iap will send us a receipt again for fulfillment
//                }
        } else { // item is no longer available
            purchasingService.notifyFulfillment(
                receipt.receiptId,
                FulfillmentRes.UNAVAILABLE
            )
        }
    }

    /**
     * Developer should implement de-duplication logic based on the receiptId
     * received from Amazon Appstore. The receiptId is a unique identifier for
     * every purchase, but the same purchase receipt can be pushed to your app
     * multiple times in the event of connectivity issue while calling
     * notifyFulfillment. So if the given receiptId was tracked and fulfilled by
     * the app before, you should not grant the purchase content to the customer
     * again, otherwise you are giving the item for free.
     *
     *
     * @param receiptId
     * @param userData
     * @return
     */
    private fun receiptAlreadyFulfilled(receiptId: String, userData: UserData): Boolean {
        // TODO check server rather than depend on local data (or possibly don't even worry - the server will know if the receipt has already been fulfilled or not)
        return fulfilledReceiptIds.contains(receiptId)
    }

    /**
     * We strongly recommend verifying the receipt on your own server side
     * first. The server side verification ideally should include checking with
     * Amazon RVS (Receipt Verification Service) to verify the receipt details.
     *
     * @see [Appstore's Receipt Verification Service](https://developer.amazon.com/appsandservices/apis/earn/in-app-purchasing/docs/rvs)
     *
     *
     * @param receiptId
     * @return
     */
    private fun verifyReceiptFromYourService(receiptId: String, userData: UserData): Boolean {
        //  TODO("Add Usecase to fetch verification from server")
        return true
    }

    /**
     * From the amazon sample app:
     *
     * Private method to revoke a consumable purchase from the customer.
     *
     * If your application supports "Revoke Consumable Purchases" feature,
     * please implement your application-specific logic to handle the revocation
     * of consumable purchase. i.e. try to revoke the items issued to customer
     * in last purchase. <br>
     * <b>Be careful with the revocation logic:</b>
     * <ul>
     * <li>For example: if you give 10 items as part of a consumable purchase,
     * and the customer has consumed 5 items, it might not be possible to fully
     * revoke items since customer does not have enough items in their account.</li>
     * <li>The cancelled receipt object can be returned by
     * getPurchaseUpdatesResponse multiple times. So, always check if the
     * receipt id was previously revoked before attempting to revoke.</li>
     * </ul>
     *
     */
    private fun revokeConsumablePurchase(receipt: Receipt, userData: UserData) {
        // TODO cancel the sub, maybe inform the server?
        Fore.getLogger().e("revokeConsumablePurchase()")
    }

    fun purchaseFailed(sku: String?) {
        // TODO what to do about this? maybe nothing, or just alert user
        Fore.getLogger().e("purchaseFailed()")
    }
}

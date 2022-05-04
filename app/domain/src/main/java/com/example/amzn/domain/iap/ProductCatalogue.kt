package com.example.amzn.domain.iap

import co.early.fore.kt.core.delegate.Fore

/**
 * The catalog of products that are available to purchase,
 * we fetch the list from the GAP and Atom
 * then disable or enable the purchases based on the response from Amazon IAP
 */
class ProductCatalogue {

    private val products: MutableList<Product> = mutableListOf()

    // TODO populate products from gap / atom / amazon iap instead
    init {
        products.add(
            Product(
                sku = "com.amazon.sample.iap.consumable.orange",
                productType = ProductType.CONSUMABLE,
                description = "description",
                price = "9.99",
                smallIconUrl = "https://openclipart.org/image/800px/svg_to_png/22897/chovynz-Orange-Icon.png",
                title = "Orange"
            )
        )
    }

    fun fetchProductCatalogFromServer() {
        Fore.getLogger().i("fetchProductCatalogFromServer()")
        // TODO
    }

    fun annotatePurchasesWithDataFromAtom() {
        Fore.getLogger().i("annotatePurchasesWithDataFromAtom()")
        // TODO
    }

    fun disableAllPurchases() {
        Fore.getLogger().i("disableAllPurchases()")
        // TODO
    }

    fun enablePurchaseForSkus(productData: Map<String, Product>) {
        Fore.getLogger().i("enablePurchaseForSkus()")
        // TODO
    }

    fun disablePurchaseForSkus(unavailableSkus: Set<String>) {
        Fore.getLogger().i("disablePurchaseForSkus()")
        // TODO
    }

    fun allAvailablePurchases(): Map<String, Product> {
        Fore.getLogger().i("allAvailablePurchases()")
        // TODO
        return emptyMap()
    }

    fun getProductForSku(sku: String): Product? {
        Fore.getLogger().i("getProductForSku() sku:$sku")
        return products.find { it.sku == sku }
    }
}

data class Product(
    val sku: String,
    val marketplaceId: String = "",
    val productType: ProductType,
    val description: String,
    val price: String,
    val smallIconUrl: String,
    val title: String,
    val enabled: Boolean = true
)

enum class ProductType {
    CONSUMABLE, ENTITLED, SUBSCRIPTION
}

enum class FulfillmentRes {
    FULFILLED, UNAVAILABLE
}

package com.example.vpn.payment

// BillingManager.kt

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*

/*

class BillingManager(
    private val context: Context,
    private val onPremiumActive: () -> Unit,
    private val onError: (Throwable) -> Unit
) : PurchasesUpdatedListener {

    /* 1. enablePendingPurchases(...) требует объект параметров */
    private val billingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder().build()
        )
        .setListener(this)
        .build()

    /* ───────── подключение ───────── */
    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(r: BillingResult) =
                if (r.responseCode == BillingClient.BillingResponseCode.OK) queryPurchases()
                else onError(Exception("Billing setup failed: ${r.debugMessage}"))

            override fun onBillingServiceDisconnected() = startConnection()
        })
    }

    /* ───────── активные подписки ───────── */
    private fun queryPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { r, purchases ->
            if (r.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases.filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
                    .forEach { p -> if (!p.isAcknowledged) acknowledge(p) else onPremiumActive() }
            } else onError(Exception("Query purchases failed: ${r.debugMessage}"))
        }
    }

    /* ───────── запуск покупки ───────── */
    fun launchPurchaseFlow(activity: Activity, pd: ProductDetails) {
        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams
                        .newBuilder()
                        .setProductDetails(pd)
                        .build()
                )
            ).build()
        billingClient.launchBillingFlow(activity, flowParams)
    }

    /* ───────── callback покупки ───────── */
    override fun onPurchasesUpdated(r: BillingResult, purchases: MutableList<Purchase>?) {
        if (r.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            purchases.forEach { p -> if (p.purchaseState == Purchase.PurchaseState.PURCHASED) {
                if (!p.isAcknowledged) acknowledge(p) else onPremiumActive()
            }}
        } else if (r.responseCode != BillingClient.BillingResponseCode.USER_CANCELED) {
            onError(Exception("Purchase error: ${r.debugMessage}"))
        }
    }

    private fun acknowledge(purchase: Purchase) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.acknowledgePurchase(params) { ack ->
            if (ack.responseCode == BillingClient.BillingResponseCode.OK) onPremiumActive()
            else onError(Exception("Acknowledge failed: ${ack.debugMessage}"))
        }
    }

    /* ───────── детали продукта ───────── */
    fun queryProductDetails(productId: String, cb: (List<ProductDetails>) -> Unit) {
        val req = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product
                        .newBuilder()
                        .setProductId(productId)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            ).build()


    fun endConnection() = billingClient.endConnection()
}


 */
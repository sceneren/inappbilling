package com.hypersoft.billing

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import com.hypersoft.billing.latest.BillingController
import com.hypersoft.billing.latest.interfaces.BillingListener
import com.hypersoft.billing.oldest.helper.BillingHelper
import com.hypersoft.billing.oldest.interfaces.OnConnectionListener
import com.hypersoft.billing.common.interfaces.OnPurchaseListener
import com.hypersoft.billing.latest.dataClasses.ProductDetail

/**
 * @param context: Context can be of Application class
 */

class BillingManager(private val context: Context) : BillingHelper(context) {

    private val billingController by lazy { BillingController(context) }

    val debugProductId =  billingController.debugProductId

    /**
     *  @param productInAppPurchases: Pass list of in-app product's ID
     *  @param productSubscriptions: Pass list of subscription product's ID
     */

    fun initialize(
        productInAppPurchases: List<String>,
        productSubscriptions: List<String>,
        billingListener: BillingListener? = null
    ) {
        billingController.startBillingConnection(
            userInAppPurchases = productInAppPurchases,
            userSubsPurchases = productSubscriptions,
            billingListener = billingListener
        )
    }

    fun observeQueryProducts(): LiveData<List<ProductDetail>> {
        return billingController.productsObserver
    }

    fun makeInAppPurchase(activity: Activity?, productId: String, onPurchaseListener: OnPurchaseListener) {
        billingController.makePurchaseInApp(activity = activity, productId = productId, onPurchaseListener = onPurchaseListener)
    }

    fun makeSubPurchase(activity: Activity?, productId: String, planId: String, onPurchaseListener: OnPurchaseListener) {
        billingController.makePurchaseSub(activity = activity, productId = productId, planId = planId, onPurchaseListener = onPurchaseListener)
    }

    fun updateSubPurchase(activity: Activity?, oldProductId: String, oldPlanId: String, productId: String, planId: String, onPurchaseListener: OnPurchaseListener) {
        billingController.updatePurchaseSub(activity = activity, oldProductId = oldProductId, oldPlanId = oldPlanId, productId = productId, planId = planId, onPurchaseListener = onPurchaseListener)
    }

    fun destroyBilling() = billingController.cleanBilling()

    @Deprecated(
        "Skip this method with new approach",
        ReplaceWith("null"),
        DeprecationLevel.WARNING
    )
    override fun setCheckForSubscription(isCheckRequired: Boolean) {
        checkForSubscription = isCheckRequired
    }


    /**
     * Deprecated. Use [initialize] with individual productId parameters instead.
     */
    @Deprecated(
        "Use initialize with individual productId and plan ids parameters instead.",
        ReplaceWith("initialize(productInAppPurchases, productSubscriptions, billingListener)"),
        DeprecationLevel.WARNING
    )
    override fun startConnection(productIdsList: List<String>, onConnectionListener: OnConnectionListener) = startBillingConnection(productIdsList, onConnectionListener)


    /**
     * Deprecated. Use [makeInAppPurchase] with individual productId parameters instead.
     */
    @Deprecated(
        "Use makeInAppPurchase with individual productId parameters instead.",
        ReplaceWith("makeInAppPurchase(activity, productId, onPurchaseListener)"),
        DeprecationLevel.WARNING
    )
    fun makeInAppPurchase(activity: Activity?, onPurchaseListener: OnPurchaseListener) = purchaseInApp(activity, onPurchaseListener)

    /**
     * Deprecated. Use [makeSubPurchase] with individual productId and planId parameters instead.
     */
    @Deprecated(
        "Use makeSubPurchase with individual productId and planId parameters instead.",
        ReplaceWith("makeSubPurchase(activity, productId, planId, onPurchaseListener)"),
        DeprecationLevel.WARNING
    )
    fun makeSubPurchase(activity: Activity?, subscriptionPlans: String, onPurchaseListener: OnPurchaseListener) = purchaseSub(activity, subscriptionPlans, onPurchaseListener)
}
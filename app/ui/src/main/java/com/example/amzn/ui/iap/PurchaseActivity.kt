package com.example.amzn.ui.iap

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import co.early.fore.core.ui.SyncableView
import co.early.fore.kt.core.delegate.Fore
import co.early.fore.kt.core.ui.LifecycleObserver
import co.early.fore.kt.core.ui.showOrInvisible
import com.example.amzn.domain.iap.BoughtItems
import com.example.amzn.domain.iap.PurchasingService
import com.example.amzn.ui.R
import kotlinx.android.synthetic.main.activity_purchase.*
import org.koin.android.ext.android.inject

class PurchaseActivity : FragmentActivity(R.layout.activity_purchase), SyncableView {

    private val boughtItems: BoughtItems by inject()
    private val purchasingService: PurchasingService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setup observers
        lifecycle.addObserver(LifecycleObserver(this, boughtItems))

        //set up click listeners
        purchase_eatorange_btn.setOnClickListener { boughtItems.eatOrange() }
        purchase_clearalloranges_btn.setOnClickListener { boughtItems.clearOranges() }
        purchase_buyorange_btn.setOnClickListener { purchasingService.purchase("com.amazon.sample.iap.consumable.orange") }
    }

    override fun syncView() {
        boughtItems.state.apply {
            purchase_busy.showOrInvisible(amazonUserId.isEmpty())
            //purchase_status_text = TODO
            purchase_buyorange_btn.isEnabled = amazonUserId.isNotEmpty()
            purchase_eatorange_btn.isEnabled = canEatOrange()
            purchase_oranges_text.text = "you have $numberOfOrangesAvailable orange(s)"
            purchase_clearalloranges_btn.isEnabled = canEatOrange()
        }
    }

    override fun onResume() {
        super.onResume()
        Fore.getLogger().d("refreshing amazon user data and getting purchase updates")
        purchasingService.getUserData()
        purchasingService.getPurchaseUpdates(false)
    }
}

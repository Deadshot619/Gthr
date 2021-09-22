package com.gthr.gthrcollect.ui.splash

import androidx.activity.viewModels
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.databinding.ActivitySplashBinding
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.homebottomnav.HomeBottomNavActivity
import com.gthr.gthrcollect.utils.constants.DynamicLinkConstants
import com.gthr.gthrcollect.utils.extensions.getUserCollectionId
import com.gthr.gthrcollect.utils.getProductType
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>() {


    override val mViewModel: SplashViewModel by viewModels()


    override fun getViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    private val mainScope = MainScope()

    override fun onBinding() {
        mViewBinding.lifecycleOwner = this
        checkLink()
    }

    private fun checkLink() {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData -> // Get deep link from result (may be null if no link is found)
                if (pendingDynamicLinkData != null) {
                    val deepLink = pendingDynamicLinkData.link
                    if (deepLink != null && deepLink.getBooleanQueryParameter(DynamicLinkConstants.OBJECT_ID, false)) {
                        val objectId = deepLink.getQueryParameter(DynamicLinkConstants.OBJECT_ID)
                        val productType = deepLink.getQueryParameter(DynamicLinkConstants.PRODUCT_TYPE)
                        startActivity(HomeBottomNavActivity.getInstance(this,objectId!!, getProductType(productType!!)!!))
                        finish()
                        GthrLogger.i("dihviufhdih", "getBooleanQueryParameter: $objectId $productType ")
                    }
                    else if (deepLink != null && deepLink.getBooleanQueryParameter(DynamicLinkConstants.COLLECTION_ID, false)) {
                        val objectId = deepLink.getQueryParameter(DynamicLinkConstants.COLLECTION_ID)
                        if(objectId==GthrCollect.prefs?.getUserCollectionId()){
                            goToHomeScreen()
                        }
                        else{
                            startActivity(HomeBottomNavActivity.getInstance(this,objectId!!))
                            finish()
                        }
                        GthrLogger.i("dihviufhdih", "COLLECTION_ID: $objectId ")
                    }
                    else{
                        GthrLogger.i("dihviufhdih", "getBooleanQueryParameter: 1")
                        pauseScreen()
                    }
                } else {
                    GthrLogger.i("dihviufhdih", "getBooleanQueryParameter: 2")
                    pauseScreen()
                }
            }.addOnFailureListener {
                GthrLogger.i("dihviufhdih", "getBooleanQueryParameter: 3")
                pauseScreen()
            }
    }

    private fun pauseScreen() {
        mainScope.launch {
            ticker(PAUSE_DURATION).receive()
            goToHomeScreen()
        }
    }

    private fun goToHomeScreen() {
        startActivity(HomeBottomNavActivity.getInstance(this))
        finish()
    }

    companion object {
        private const val PAUSE_DURATION = 3000L
    }
}
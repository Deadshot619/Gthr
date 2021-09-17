package com.gthr.gthrcollect

import android.app.Application
import com.gthr.gthrcollect.utils.Prefs
import com.gthr.gthrcollect.utils.constants.StripeConstants
import com.stripe.android.BuildConfig
import com.stripe.android.PaymentConfiguration
import timber.log.Timber

class GthrCollect: Application() {
    override fun onCreate() {
        super.onCreate()

        PaymentConfiguration.init(applicationContext, StripeConstants.STRIPE_PUBLISHABLE_KEY)

        initLogger()
        initSharedPref()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    private fun initSharedPref() {
        prefs = Prefs(applicationContext)
    }

    companion object {
        var prefs: Prefs? = null
            private set
    }
}
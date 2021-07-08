package com.gthr.gthrcollect

import android.app.Application
import com.gthr.gthrcollect.utils.Prefs
import timber.log.Timber

class GthrCollect: Application() {
    override fun onCreate() {
        super.onCreate()

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
    }
}
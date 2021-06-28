package com.gthr.gthrcollect

import android.app.Application
import timber.log.Timber

class GthrCollect: Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}
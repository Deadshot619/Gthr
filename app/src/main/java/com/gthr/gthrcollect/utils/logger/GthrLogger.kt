package com.gthr.gthrcollect.utils.logger

import timber.log.Timber


object GthrLogger {
    private const val TAG = "GTHR_Application"

    fun i(tag: String = TAG, message: String) {
        Timber.tag(tag).i(message)
    }

    fun d(tag: String = TAG, message: String) {
        Timber.tag(tag).d(message)
    }

    fun v(tag: String = TAG, message: String) {
        Timber.tag(tag).v(message)
    }

    fun e(tag: String = TAG, message: String) {
        Timber.tag(tag).e(message)
    }

    fun w(tag: String = TAG, message: String) {
        Timber.tag(tag).w(message)
    }
}
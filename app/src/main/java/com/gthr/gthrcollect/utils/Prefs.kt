package com.gthr.gthrcollect.utils

import android.content.Context
import android.content.SharedPreferences

class Prefs(mContext: Context) {
    private val sharedPreferences: SharedPreferences =
        mContext.getSharedPreferences(DEFAULT_SHARED_PREFERENCE, Context.MODE_PRIVATE)

    var shippingAddresses: String
        set(value) = sharedPreferences.edit().putString(SHIPPING_ADDRESS_LIST, value).apply()
        get() = sharedPreferences.getString(SHIPPING_ADDRESS_LIST, "[]") ?: "[]"


    companion object {
        private const val DEFAULT_SHARED_PREFERENCE = "default_shared_preference"
        private const val SHIPPING_ADDRESS_LIST = "shipping_address_list"
    }
}
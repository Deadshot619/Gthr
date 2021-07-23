package com.gthr.gthrcollect.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.domain.SignUpAuthCred
import com.gthr.gthrcollect.model.domain.User
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.utils.extensions.fromJsonString
import com.gthr.gthrcollect.utils.extensions.gson
import com.gthr.gthrcollect.utils.extensions.toJsonString

class Prefs(mContext: Context) {
    private val sharedPreferences: SharedPreferences =
        mContext.getSharedPreferences(DEFAULT_SHARED_PREFERENCE, Context.MODE_PRIVATE)

    var shippingAddresses: String
        set(value) = sharedPreferences.edit().putString(SHIPPING_ADDRESS_LIST, value).apply()
        get() = sharedPreferences.getString(SHIPPING_ADDRESS_LIST, "[]") ?: "[]"

    var signedInUser: User?
        set(value) = sharedPreferences.edit().putString(SIGNED_IN_USER, gson.toJsonString(value))
            .apply()
        get() = gson.fromJsonString(sharedPreferences.getString(SIGNED_IN_USER, "") ?: "")

    var userInfoModel: UserInfoDomainModel?
        set(value) = sharedPreferences.edit().putString(USER_INFO_MODEL, gson.toJsonString(value))
            .apply()
        get() = gson.fromJsonString(sharedPreferences.getString(USER_INFO_MODEL, "") ?: "")

    var collectionInfoModel: CollectionInfoDomainModel?
        set(value) = sharedPreferences.edit().putString(COLLECTION_INFO_MODEL, gson.toJsonString(value))
            .apply()
        get() = gson.fromJsonString(sharedPreferences.getString(COLLECTION_INFO_MODEL, "") ?: "")

    var signUpCred: SignUpAuthCred?
        set(value) = sharedPreferences.edit().putString(SIGN_UP_CRED, gson.toJsonString(value)).apply()
        get() = gson.fromJsonString(sharedPreferences.getString(SIGN_UP_CRED, "") ?: "")

    @SuppressLint("CommitPrefEdits")
    fun clearPref(){
        sharedPreferences.edit().clear().apply()
    }



    companion object {
        private const val DEFAULT_SHARED_PREFERENCE = "default_shared_preference"
        private const val SHIPPING_ADDRESS_LIST = "shipping_address_list"
        private const val SIGN_UP_CRED = "sign_up_cred"
        private const val SIGNED_IN_USER = "signed_in_user"
        private const val USER_INFO_MODEL = "user_info_model"
        private const val COLLECTION_INFO_MODEL = "collection_info_model"
    }
}
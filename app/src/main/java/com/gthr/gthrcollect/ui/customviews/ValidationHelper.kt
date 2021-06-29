package com.gthr.gthrcollect.ui.customviews

import android.text.TextUtils
import android.util.Patterns

object ValidationHelper {
    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return !TextUtils.isEmpty(password)
    }
}
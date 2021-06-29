package com.gthr.gthrcollect.utils.extensions

import android.text.TextUtils
import android.util.Patterns

fun String.isValidEmail(): Boolean =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean = !TextUtils.isEmpty(this)
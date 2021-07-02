package com.gthr.gthrcollect.utils.extensions

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern


fun String.isValidEmail(): Boolean =
    !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean {
    return !TextUtils.isEmpty(this) && length >= 8 && contains(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"))
}

fun String.isValidPhoneNumber():Boolean = length==10

fun String.isValidDisplayName(): Boolean {
    val regex = "^[\\p{L} 0-9._]+$"
    val p: Pattern = Pattern.compile(regex)
    if (this.isEmpty()) return false
    val m: Matcher = p.matcher(this)
    return if(!m.matches()) false else this.length < 25
}
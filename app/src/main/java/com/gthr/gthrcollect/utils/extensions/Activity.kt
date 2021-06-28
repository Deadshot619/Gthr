package com.gthr.gthrcollect.utils.extensions

import android.app.Activity
import android.widget.Toast

private var toast: Toast? = null

fun Activity.showToast(text: String, durationLong: Boolean = false) {
    toast?.cancel()
    toast = Toast.makeText(this, text, if (durationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    toast?.show()
}
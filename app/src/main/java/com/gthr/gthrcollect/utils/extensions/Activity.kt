package com.gthr.gthrcollect.utils.extensions

import android.app.Activity
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

private var toast: Toast? = null

fun Activity.showToast(text: String, durationLong: Boolean = false) {
    toast?.cancel()
    toast = Toast.makeText(this, text, if (durationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    toast?.show()
}

fun Activity.getBackgroundDrawable(@DrawableRes id: Int): Drawable? =
    ContextCompat.getDrawable(this, id)

fun Activity.getResolvedColor(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

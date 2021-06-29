package com.gthr.gthrcollect.utils.extensions

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.showToast(text: String, durationLong: Boolean = false) {
    activity?.showToast(text, durationLong)
}

fun Fragment.getBackgroundDrawable(@DrawableRes id: Int): Drawable? =
    ContextCompat.getDrawable(requireContext(), id)

fun Fragment.getResolvedColor(@ColorRes id: Int): Int = ContextCompat.getColor(requireContext(), id)
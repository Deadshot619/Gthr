package com.gthr.gthrcollect.utils.extensions

import androidx.fragment.app.Fragment

fun Fragment.showToast(text: String, durationLong: Boolean = false) {
    activity?.showToast(text, durationLong)
}
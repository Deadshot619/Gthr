package com.example.samplebasestructure.utils.extensions

import android.app.Activity
import androidx.fragment.app.Fragment

fun Fragment.showToast(text: String, durationLong: Boolean = false) {
    activity?.showToast(text, durationLong)
}
package com.gthr.gthrcollect.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.gthr.gthrcollect.R

fun Context.hideKeyboard(view: View) {
    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showPermissionSnackBar(view: View, title: String) {
    Snackbar.make(view, title, Snackbar.LENGTH_LONG).setAction(getString(R.string.settings_text)) {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        })
    }.show()
}

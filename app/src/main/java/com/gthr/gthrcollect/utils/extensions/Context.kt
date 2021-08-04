package com.gthr.gthrcollect.utils.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.ui.profile.editprofile.EditProfileFragment
import java.io.File

private var toast: Toast? = null

fun Context.showToast(text: String, durationLong: Boolean = false) {
    toast?.cancel()
    toast = Toast.makeText(this, text, if (durationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    toast?.show()
}

fun Context.hideKeyboard(view: View) {
    val imm: InputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.getImageDrawable(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(this, id)

//Get file from file name
fun Context.getFile(name: String): File? {
    val mediaStorageDir =
        File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), EditProfileFragment.DIR_NAME)
    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
        return null
    }
    return File(mediaStorageDir.path + File.separator + name)
}

fun Context.showPermissionSnackBar(view: View, title: String) {
    Snackbar.make(view, title, Snackbar.LENGTH_LONG).setAction(getString(R.string.settings_text)) {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        })
    }.show()
}

fun Context.sendMail(emailTo: String = "", subject: String = "", body: String = "") {
    try {
        val selectorIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
        }

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailTo))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            selector = selectorIntent
        }

        startActivity(Intent.createChooser(emailIntent, getString(R.string.intent_email_chooser_title)))
    } catch (e: ActivityNotFoundException) {
        showToast(getString(R.string.intent_email_chooser_not_found))
    }
}
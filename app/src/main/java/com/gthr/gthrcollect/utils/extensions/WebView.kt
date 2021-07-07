package com.gthr.gthrcollect.utils.extensions

import android.webkit.WebView

fun WebView.initWebView(url: String) {
    settings.javaScriptEnabled = true
    loadUrl(url)
}
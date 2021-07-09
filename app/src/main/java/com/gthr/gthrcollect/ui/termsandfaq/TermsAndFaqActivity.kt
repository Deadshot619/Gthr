package com.gthr.gthrcollect.ui.termsandfaq

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.enums.WebViewType
import com.gthr.gthrcollect.utils.extensions.initWebView

class TermsAndFaqActivity : AppCompatActivity() {
    private lateinit var mWebView: WebView
    private lateinit var webViewType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_faq)

        mWebView = findViewById(R.id.webview)
        webViewType = intent.getStringExtra(WEB_VIEW_TYPE) ?: WebViewType.TERMS_AND_CONDITION.type

        when (webViewType) {
            WebViewType.TERMS_AND_CONDITION.type -> mWebView.initWebView(URL_TERMS_AND_CONDITIONS)
            WebViewType.FAQ.type -> mWebView.initWebView(URL_FAQ)
        }
    }

    companion object {
        private const val WEB_VIEW_TYPE = "web_view_type"
        private const val URL_TERMS_AND_CONDITIONS = "https://gthrcollect.com/terms-of-service/"
        private const val URL_FAQ = "https://gthrcollect.com/documentation/"

        fun getInstance(context: Context, webViewType: WebViewType) =
            Intent(context, TermsAndFaqActivity::class.java).apply {
                putExtra(WEB_VIEW_TYPE, webViewType.type)
            }
    }
}
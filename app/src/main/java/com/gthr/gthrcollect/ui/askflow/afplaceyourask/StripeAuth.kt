package com.gthr.gthrcollect.ui.askflow.afplaceyourask

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.gthr.gthrcollect.databinding.WebViewLayoutBinding
import com.gthr.gthrcollect.ui.customcameraactivities.CustomIdCamera
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.logger.GthrLogger

class StripeAuth : AppCompatActivity() {

    lateinit var binding: WebViewLayoutBinding

    val production = "https://connect.stripe.com/express/oauth/authorize?" +
            "client_id=ca_H0t149beFopMPfOhNq6JvaQsnlkbrTpu&state=sv_53124" +
            "&redirect_uri=https://hwkrapp.page.link/mVFa"

    val testUrl =
        "https://connect.stripe.com/express/oauth/authorize?client_id=ca_H0t1jArVq47Fpzm3txMvm0v8lVszMewb&state=sv_53124&redirect_uri=https://gthrcollect.page.link/redirect"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = WebViewLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.webView.settings.pluginState = WebSettings.PluginState.ON
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true

        try {
            binding.webView.webViewClient = MyWebViewClient()
            binding.webView.loadUrl(testUrl)
        } catch (e: Exception) {
            e.toString()
        }
    }

    private class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: String?): Boolean {

            GthrLogger.e("MyWebViewClient", "TRIGGERED: " + request.toString())

            if (request.toString().contains("intent", false)) {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(request))
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                if (intent.resolveActivity(view?.context?.getPackageManager()!!) != null) {
                    view.context?.startActivity(intent)
                    return true
                } else {
                    //  view.context?.showToast("No Activity found")
                    //  (view.context as Activity).finish()
                    val returnIntent = Intent()
                    returnIntent.putExtra(AfPlaceYourAskFragment.STRIPE_AUTH_KEY, 1)
                    (view.context as Activity).setResult(RESULT_OK, returnIntent)
                    (view.context as Activity).finish()
                }
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            GthrLogger.e("URLString", url!!)
        }
    }

    companion object {

        fun getInstance(context: Context) = Intent(context, StripeAuth::class.java)
    }

}

package com.gthr.gthrcollect.ui.askflow.afplaceyourask

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.databinding.WebViewLayoutBinding
import com.gthr.gthrcollect.ui.customcameraactivities.CustomIdCamera
import com.gthr.gthrcollect.utils.constants.CloudFunctions
import com.gthr.gthrcollect.utils.enums.CameraViews
import com.gthr.gthrcollect.utils.extensions.getUserCollectionId
import com.gthr.gthrcollect.utils.extensions.getUserUID
import com.gthr.gthrcollect.utils.logger.GthrLogger
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import kotlin.random.Random

class StripeAuth : AppCompatActivity() {

    lateinit var binding: WebViewLayoutBinding

    var mUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = WebViewLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.webView.settings.pluginState = WebSettings.PluginState.ON
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true

        mUrl = intent.getStringExtra(AfPlaceYourAskFragment.STRIPE_URL)

        mUrl?.let {
            try {
                binding.webView.webViewClient = MyWebViewClient()
                binding.webView.loadUrl(mUrl.toString())
            } catch (e: Exception) {
                e.toString()
            }

        }
    }

    private class MyWebViewClient : WebViewClient() {


        override fun shouldOverrideUrlLoading(view: WebView?, request: String?): Boolean {


            if (request.toString().contains("state=sv_53124")) {

                val baseurl =
                    "${CloudFunctions.FIREBASE_FUN_BASE_URL}${CloudFunctions.CREATE_STRIPE_ACC}?"
                val uid = "&uid="

                GthrLogger.e("MyWebViewClient", "TRIGGERED: " + request.toString())

                var myUrl =
                    request.toString().removePrefix("https://gthrcollect.page.link/redirect?")

                GthrLogger.e("MyStripeUrls", "${myUrl.toString()}")

                //     myUrl= myUrl.removeSuffix("&state=sv_53124")
                //   code=ac_KI4pBdjw2PBcrkTytxT8B7VRsNnMLkoN&state=sv_53124

                val finalUrl = "${baseurl}${myUrl}${uid}${GthrCollect.prefs?.getUserUID()}"

                view?.loadUrl(finalUrl)
                GthrLogger.e("finalUrl", "${finalUrl.toString()}")


                /*  val returnIntent = Intent()
                  returnIntent.putExtra(AfPlaceYourAskFragment.STRIPE_AUTH_KEY, 1)
                  returnIntent.putExtra(AfPlaceYourAskFragment.STRIPE_AUTH_CODE, finalUrl)
                  (view?.context as Activity).setResult(RESULT_OK, returnIntent)
                  (view.context as Activity).finish()*/

                return false
            }
            GthrLogger.e("MyWebViewClient", "TRIGGERED: " + request.toString())

            return super.shouldOverrideUrlLoading(view, request)
        }


/*
        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {


            GthrLogger.e("StripeUrls", "${request?.url.toString()}")


            if (request?.url.toString().contains(" https://gthrcollect.page.link")) {

                val myUrl =
                    request?.url.toString().removePrefix("https://gthrcollect.page.link/redirect?")

                GthrLogger.e("MyStripeUrls", "${myUrl.toString()}")
            }



            return super.shouldInterceptRequest(view, request)
        }
*/


        override fun onPageFinished(view: WebView?, url: String?) {

            GthrLogger.e("onPageFinished", url!!)

            if (url.toString()
                    .contains("${CloudFunctions.FIREBASE_FUN_BASE_URL}${CloudFunctions.CREATE_STRIPE_ACC}")
            ) {
                val returnIntent = Intent()
                returnIntent.putExtra(AfPlaceYourAskFragment.STRIPE_AUTH_KEY, 1)
                returnIntent.putExtra(AfPlaceYourAskFragment.STRIPE_AUTH_CODE, url)
                (view?.context as Activity).setResult(RESULT_OK, returnIntent)
                (view.context as Activity).finish()

                GthrLogger.e("onPageFinished", "back")
            }
        }
    }

    companion object {

/*
        const val production = "https://connect.stripe.com/express/oauth/authorize?" +
                "client_id=ca_H0t149beFopMPfOhNq6JvaQsnlkbrTpu&state=sv_53124" +
                "&redirect_uri=https://hwkrapp.page.link/mVFa"

                        val testUrl =
            "https://connect.stripe.com/express/oauth/authorize?client_id=ca_H0t1jArVq47Fpzm3txMvm0v8lVszMewb&state=sv_53124&redirect_uri=https://gthrcollect.page.link/redirect"+
                    "&stripe_user[business_type]=individual&" +
                    "stripe_user[phone_number]=0000000000&" +
                    "stripe_user[street_address]=cdsvsvvdv&" +
                    "stripe_user[city]=cdvdvdv&" +
                    "stripe_user[state]=xbsbdbdb&" +
                    "stripe_user[zip]=12345&" +
                    "stripe_user[first_name]=cssfddff&" +
                    "stripe_user[last_name]=xwwfewfef"

*/

        fun getInstance(context: Context, url: String) =
            Intent(context, StripeAuth::class.java).apply {

                putExtra(AfPlaceYourAskFragment.STRIPE_URL, url)

            }
    }

    override fun onBackPressed() {
        finish()
    }

}


package com.gthr.gthrcollect.ui.settings.termsandconditions

import android.webkit.WebView
import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.TermsAndConditionsFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.extensions.initWebView

class TermsAndConditionsFragment :
    BaseFragment<TermsAndConditionsViewModel, TermsAndConditionsFragmentBinding>() {
    override fun getViewBinding() = TermsAndConditionsFragmentBinding.inflate(layoutInflater)
    override val mViewModel: TermsAndConditionsViewModel by viewModels()

    private lateinit var mWebView: WebView

    override fun onBinding() {
        initViews()
        mWebView.initWebView(URL)
    }

    fun initViews() {
        mWebView = mViewBinding.webview
    }

    companion object {
        private const val URL = "https://gthrcollect.com/terms-of-service/"
    }
}
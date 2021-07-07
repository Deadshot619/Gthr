package com.gthr.gthrcollect.ui.settings.faqandhelp

import android.webkit.WebView
import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.FaqAndHelpFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.extensions.initWebView

class FaqAndHelpFragment : BaseFragment<FaqAndHelpViewModel, FaqAndHelpFragmentBinding>() {
    override fun getViewBinding() = FaqAndHelpFragmentBinding.inflate(layoutInflater)
    override val mViewModel: FaqAndHelpViewModel by viewModels()

    private lateinit var mWebView: WebView

    override fun onBinding() {
        initViews()
        mWebView.initWebView(URL)
    }

    fun initViews() {
        mWebView = mViewBinding.webview
    }

    companion object {
        private const val URL = "https://gthrcollect.com/documentation/"
    }
}
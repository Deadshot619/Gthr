package com.gthr.gthrcollect.ui.editaccountinfo.welcome

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.WelcomeFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton

class WelcomeFragment : BaseFragment<WelcomeViewModel, WelcomeFragmentBinding>() {
    override val mViewModel: WelcomeViewModel by viewModels()
    override fun getViewBinding() = WelcomeFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnWelcome: CustomAuthenticationButton

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnWelcome = btnWelcome
        }
    }

    private fun setUpClickListeners() {
        mBtnWelcome.setOnClickListener {
            activity?.finish()
        }
    }
}
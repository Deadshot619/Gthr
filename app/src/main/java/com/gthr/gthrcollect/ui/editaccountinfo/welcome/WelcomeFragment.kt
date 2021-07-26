package com.gthr.gthrcollect.ui.editaccountinfo.welcome

import android.content.Intent
import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.WelcomeFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.homebottomnav.HomeBottomNavActivity
import com.gthr.gthrcollect.ui.settings.SettingsActivity
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
            startActivity(HomeBottomNavActivity.getInstance(requireContext()).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            activity?.finish()
        }
    }
}
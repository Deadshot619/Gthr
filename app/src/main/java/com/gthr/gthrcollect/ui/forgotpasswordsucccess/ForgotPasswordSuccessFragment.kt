package com.gthr.gthrcollect.ui.forgotpasswordsucccess

import androidx.fragment.app.activityViewModels
import com.gthr.gthrcollect.databinding.ForgotPasswordSuccessFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.forgotpassword.ForgotPasswordViewModel
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton

class ForgotPasswordSuccessFragment :
    BaseFragment<ForgotPasswordViewModel, ForgotPasswordSuccessFragmentBinding>() {
    override val mViewModel: ForgotPasswordViewModel by activityViewModels()
    override fun getViewBinding() = ForgotPasswordSuccessFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnFinish: CustomAuthenticationButton

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnFinish = btnFinish
        }
    }

    private fun setUpClickListeners() {
        mBtnFinish.setOnClickListener {
            activity?.finish()
        }
    }
}
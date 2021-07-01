package com.gthr.gthrcollect.ui.forgotpassword

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ForgotPasswordFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton
import com.gthr.gthrcollect.utils.customviews.CustomEmailEditText
import com.gthr.gthrcollect.utils.extensions.isValidEmail

class ForgotPasswordFragment :
    BaseFragment<ForgotPasswordViewModel, ForgotPasswordFragmentBinding>() {
    override val mViewModel: ForgotPasswordViewModel by activityViewModels()
    override fun getViewBinding() = ForgotPasswordFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnForgotPassword: CustomAuthenticationButton
    private lateinit var mCetEmail: CustomEmailEditText


    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnForgotPassword = btnForgotPassword
            mCetEmail = cetEmail
        }
    }

    private fun setUpClickListeners() {
        mBtnForgotPassword.setOnClickListener {
            if (validate())
                findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToForgotPasswordSuccessFragment())
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        if (!mCetEmail.mEtEmail.text.toString().trim().isValidEmail()) {
            mCetEmail.showError(getString(R.string.enter_valid_email_error_text))
            isValid = false
        } else
            mCetEmail.hideError()

        return isValid
    }
}
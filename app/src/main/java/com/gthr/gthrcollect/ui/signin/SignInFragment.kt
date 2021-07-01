package com.gthr.gthrcollect.ui.signin

import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.LayoutSignUpHeaderBinding
import com.gthr.gthrcollect.databinding.SignInFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.forgotpassword.ForgotPasswordActivity
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton
import com.gthr.gthrcollect.utils.customviews.CustomEmailEditText
import com.gthr.gthrcollect.utils.customviews.CustomPasswordEditText
import com.gthr.gthrcollect.utils.extensions.isValidEmail
import com.gthr.gthrcollect.utils.extensions.isValidPassword

class SignInFragment : BaseFragment<SignInViewModel, SignInFragmentBinding>() {

    override fun getViewBinding() = SignInFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SignInViewModel by viewModels()

    private lateinit var mLayoutHeader: LayoutSignUpHeaderBinding
    private lateinit var mTvCreateAccount: TextView
    private lateinit var mCetEmail: CustomEmailEditText
    private lateinit var mCetPassword: CustomPasswordEditText
    private lateinit var mBtnSignIn: CustomAuthenticationButton
    private lateinit var mTvForgotPassword: TextView


    override fun onBinding() {
        initViews()

        //Title
        mLayoutHeader.tvTitle.text = getString(R.string.sign_in_text)

        setUpListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mLayoutHeader = layoutHeader
            mTvCreateAccount = tvCreateAccount
            mCetEmail = cetEmail
            mCetPassword = cetPassword
            mBtnSignIn = btnSignIn
            mTvForgotPassword = tvForgotPassword
        }
    }

    private fun setUpListeners() {
        mBtnSignIn.setOnClickListener {
            validate()
        }

        mTvCreateAccount.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInToSignUpFragment())
        }

        mTvForgotPassword.setOnClickListener {
            startActivity(ForgotPasswordActivity.getInstance(requireContext()))
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        if (!mCetEmail.mEtEmail.text.toString().trim().isValidEmail()) {
            mCetEmail.showError(getString(R.string.enter_valid_email_error_text))
            isValid = false
        } else
            mCetEmail.hideError()

        if (!mCetPassword.mEtPassword.text.toString().trim().isValidPassword()) {
            mCetPassword.showError(getString(R.string.enter_valid_password_error_text))
            isValid = false
        } else
            mCetPassword.hideError()

        return isValid
    }
}
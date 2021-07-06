package com.gthr.gthrcollect.ui.homebottomnav.signup

import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.SignInFlowRepository
import com.gthr.gthrcollect.databinding.LayoutSignUpHeaderBinding
import com.gthr.gthrcollect.databinding.SignUpFragmentBinding
import com.gthr.gthrcollect.model.domain.SignUpAuthCred
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.editaccountinfo.EditAccountInfoActivity
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton
import com.gthr.gthrcollect.utils.customviews.CustomEmailEditText
import com.gthr.gthrcollect.utils.customviews.CustomPasswordEditText
import com.gthr.gthrcollect.utils.extensions.isValidEmail
import com.gthr.gthrcollect.utils.extensions.isValidPassword


class SignUpFragment : BaseFragment<SignUpViewModel, SignUpFragmentBinding>() {

    private val TAG: String = this.javaClass.name

    private val repository = SignInFlowRepository()

    override fun getViewBinding() = SignUpFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SignUpViewModel by viewModels { SignUpViewModelFactory(repository) }

    private lateinit var mLayoutHeader: LayoutSignUpHeaderBinding
    private lateinit var mBtnSignUp: CustomAuthenticationButton
    private lateinit var mTvSignIn: TextView
    private lateinit var mCetEmail: CustomEmailEditText
    private lateinit var mCetPassword: CustomPasswordEditText

    override fun onBinding() {
        initViews()

        //Title
        mLayoutHeader.tvTitle.text = getString(R.string.sign_up_text)

        setUpListeners()

        mCetEmail.mEtEmail.setText("abc@gmail.com")
        mCetPassword.mEtPassword.setText("Abc@12345")
    }

    private fun initViews() {
        mViewBinding.run {
            mLayoutHeader = layoutHeader
            mBtnSignUp = btnSignUp
            mTvSignIn = tvSignIn
            mCetEmail = cetEmail
            mCetPassword = cetPassword
            initProgressBar(layoutProgress)
        }
    }

    private fun setUpListeners() {
        mBtnSignUp.setOnClickListener {
            if (validate()) {
                val email = mCetEmail.mEtEmail.text.toString().trim()
                val password = mCetPassword.mEtPassword.text.toString().trim()
                GthrCollect.prefs?.signUpCred = SignUpAuthCred(email, password)
                startActivity(EditAccountInfoActivity.getInstance(requireContext()))
            }
        }

        mTvSignIn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignIn())
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
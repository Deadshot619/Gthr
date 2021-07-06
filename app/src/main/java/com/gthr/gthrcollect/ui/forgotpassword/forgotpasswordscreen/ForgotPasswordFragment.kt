package com.gthr.gthrcollect.ui.forgotpassword.forgotpasswordscreen

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.ForgotPasswordFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.forgotpassword.ForgotPasswordViewModel
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton
import com.gthr.gthrcollect.utils.customviews.CustomEditText
import com.gthr.gthrcollect.utils.extensions.isValidEmail
import com.gthr.gthrcollect.utils.extensions.showToast

class ForgotPasswordFragment :
    BaseFragment<ForgotPasswordViewModel, ForgotPasswordFragmentBinding>() {
    private val TAG: String = this.javaClass.name
    override val mViewModel: ForgotPasswordViewModel by activityViewModels()
    override fun getViewBinding() = ForgotPasswordFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnForgotPassword: CustomAuthenticationButton
    private lateinit var mCetEmail: CustomEditText
    private lateinit var mAuth: FirebaseAuth

    override fun onBinding() {
        mAuth = Firebase.auth

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
                sendPasswordLink(mCetEmail.mEtMain.text.toString().trim())
        }
    }

    private fun sendPasswordLink(email: String) {

        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    activity?.showToast(getString(R.string.password_link_sent))
                    findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToForgotPasswordSuccessFragment())

                } else {
                    activity?.showToast(task.exception?.message.toString())
                }
            }
    }

    private fun validate(): Boolean {
        var isValid = true
        if (!mCetEmail.mEtMain.text.toString().trim().isValidEmail()) {
            mCetEmail.setError(getString(R.string.enter_valid_email_error_text))
            isValid = false
        } else
            mCetEmail.setInitial()

        return isValid
    }


}
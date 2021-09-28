package com.gthr.gthrcollect.ui.homebottomnav.signin

import android.content.Intent
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.GthrCollect.Companion.prefs
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.SignInFlowRepository
import com.gthr.gthrcollect.databinding.LayoutSignUpHeaderBinding
import com.gthr.gthrcollect.databinding.SignInFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.forgotpassword.ForgotPasswordActivity
import com.gthr.gthrcollect.ui.homebottomnav.HomeBottomNavActivity
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton
import com.gthr.gthrcollect.utils.customviews.CustomEmailEditText
import com.gthr.gthrcollect.utils.customviews.CustomPasswordEditText
import com.gthr.gthrcollect.utils.extensions.*

class SignInFragment : BaseFragment<SignInViewModel, SignInFragmentBinding>() {

    private val TAG: String = this.javaClass.name

    private val repository = SignInFlowRepository()

    override fun getViewBinding() = SignInFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SignInViewModel by viewModels { SignInViewModelFactory(repository) }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mLayoutHeader: LayoutSignUpHeaderBinding
    private lateinit var mTvCreateAccount: TextView
    private lateinit var mCetEmail: CustomEmailEditText
    private lateinit var mCetPassword: CustomPasswordEditText
    private lateinit var mBtnSignIn: CustomAuthenticationButton
    private lateinit var mTvForgotPassword: TextView

    override fun onBinding() {
        mAuth = Firebase.auth
        initViews()

        //Title
        mLayoutHeader.tvTitle.text = getString(R.string.sign_in_text)

        setUpListeners()
        setUpObservers()

        if (isUserLoggedIn()) {
            goToProfilePage()
        }
/*        mCetEmail.mEtEmail.setText("abc@gmail.com")
        mCetPassword.mEtPassword.setText("Abc@12345")*/
    }

    private fun initViews() {
        mViewBinding.run {
            mLayoutHeader = layoutHeader
            mTvCreateAccount = tvCreateAccount
            mCetEmail = cetEmail
            mCetPassword = cetPassword
            mBtnSignIn = btnSignIn
            mTvForgotPassword = tvForgotPassword
            initProgressBar(layoutProgress)
        }
    }

    private fun setUpListeners() {
        mBtnSignIn.setOnClickListener {
            if (validate()) {
                val email = mCetEmail.mEtEmail.text.toString().trim()
                val password = mCetPassword.mEtPassword.text.toString().trim()
                mViewModel.signIn(email, password)
            }
        }

        mTvCreateAccount.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInToSignUpFragment())
        }

        mTvForgotPassword.setOnClickListener {
            startActivity(ForgotPasswordActivity.getInstance(requireContext()))
        }
    }

    private fun setUpObservers() {
        mViewModel.userInfo.observe(viewLifecycleOwner, {
            when (it) {
                is State.Loading -> {
                    showProgressBar()
                }
                is State.Success -> {
                    showProgressBar(false)
                    mViewModel.getUserData(it.data.uid)
                }
                is State.Failed -> {
                    showProgressBar(false)
                    showToast(it.message)
                }
            }
        })

        mViewModel.userInfoAndCollectionInfo.observe(viewLifecycleOwner, {
            when (it) {
                is State.Loading -> {
                    showProgressBar()
                }
                is State.Success -> {
                    showProgressBar(false)
                    prefs?.run {
                        updateUserInfoModelData(it.data.first)
                        updateCollectionInfoModelData(it.data.second)
                        signedInUser = (mViewModel.userInfo.value as State.Success).data
                    }
//                    goToProfilePage()
                    startActivity(HomeBottomNavActivity.getInstance(requireContext()).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }
                is State.Failed -> {
                    showProgressBar(false)
                    showToast(it.message)
                }
            }
        })
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

    private fun isUserLoggedIn(): Boolean {
        prefs?.signedInUser?.let {
            return@isUserLoggedIn !it.email.isNullOrEmpty() && it.uid.isNotEmpty()
        } ?: return false
    }

    private fun goToProfilePage(){
        findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToAccountFragment())
    }
}
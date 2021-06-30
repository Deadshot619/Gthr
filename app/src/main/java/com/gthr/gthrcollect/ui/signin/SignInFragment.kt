package com.gthr.gthrcollect.ui.signin

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.SignInFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class SignInFragment : BaseFragment<SignInViewModel, SignInFragmentBinding>() {

    override fun getViewBinding() = SignInFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SignInViewModel by viewModels()

    override fun onBinding() {
        //Title
        mViewBinding.layoutHeader.tvTitle.text = getString(R.string.sign_in_text)

        setUpListeners()
    }

    private fun setUpListeners() {
        mViewBinding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInToSignUpFragment())
        }
    }
}
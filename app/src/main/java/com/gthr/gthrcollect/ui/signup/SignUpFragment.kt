package com.gthr.gthrcollect.ui.signup

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.SignUpFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.editaccountinfo.EditAccountInfoActivity

class SignUpFragment : BaseFragment<SignUpViewModel, SignUpFragmentBinding>() {
    override fun getViewBinding() = SignUpFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SignUpViewModel by viewModels()

    override fun onBinding() {
        //Title
        mViewBinding.layoutHeader.tvTitle.text = getString(R.string.sign_up_text)

        setUpListeners()

    }

    private fun setUpListeners() {
        mViewBinding.btnSignUp.setOnClickListener {
            startActivity(EditAccountInfoActivity.getInstance(requireContext()))
        }

        mViewBinding.tvSignIn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignIn())
        }
    }
}
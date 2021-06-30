package com.gthr.gthrcollect.ui.signin

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.SignInFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class SignInFragment : BaseFragment<SignInViewModel, SignInFragmentBinding>() {

    override fun getViewBinding() = SignInFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SignInViewModel by viewModels()

    override fun onBinding() {
    }
}
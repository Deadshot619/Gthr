package com.gthr.gthrcollect.ui.eauserinfo

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.EaUserInfoFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class EaUserInfoFragment : BaseFragment<EaUserInfoViewModel, EaUserInfoFragmentBinding>() {
    override val mViewModel: EaUserInfoViewModel by viewModels()
    override fun getViewBinding() = EaUserInfoFragmentBinding.inflate(layoutInflater)
    override fun onBinding() {
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        mViewBinding.btnNext.setOnClickListener {
            findNavController().navigate(EaUserInfoFragmentDirections.actionEaUserInfoFragmentToEaIdVerificationFragment())
        }
    }
}
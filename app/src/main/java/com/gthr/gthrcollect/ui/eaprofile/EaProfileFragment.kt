package com.gthr.gthrcollect.ui.eaprofile

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.EaProfileFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class EaProfileFragment : BaseFragment<EaProfileViewModel, EaProfileFragmentBinding>() {
    override val mViewModel: EaProfileViewModel by viewModels()
    override fun getViewBinding() = EaProfileFragmentBinding.inflate(layoutInflater)
    override fun onBinding() {
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        mViewBinding.btnNext.setOnClickListener {
            findNavController().navigate(EaProfileFragmentDirections.actionEaProfileFragmentToEaUserInfoFragment())
        }
    }
}
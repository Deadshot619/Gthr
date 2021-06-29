package com.gthr.gthrcollect.ui.eaprofile

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.EaProfileFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class EaProfileFragment : BaseFragment<EaProfileViewModel, EaProfileFragmentBinding>() {
    override val mViewModel: EaProfileViewModel by viewModels()
    override fun getViewBinding() = EaProfileFragmentBinding.inflate(layoutInflater)
    override fun onBinding() {
    }

    override fun onStart() {
        super.onStart()
//        (activity as EditAccountInfoActivity).setSectionSelection(EditAccountSection.PROFILE)
    }
}
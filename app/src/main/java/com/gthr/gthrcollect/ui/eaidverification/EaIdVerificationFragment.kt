package com.gthr.gthrcollect.ui.eaidverification

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.EaIdVerificationFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class EaIdVerificationFragment :
    BaseFragment<EaIdVerificationViewModel, EaIdVerificationFragmentBinding>() {
    override val mViewModel: EaIdVerificationViewModel by viewModels()
    override fun getViewBinding() = EaIdVerificationFragmentBinding.inflate(layoutInflater)
    override fun onBinding() {
    }
}
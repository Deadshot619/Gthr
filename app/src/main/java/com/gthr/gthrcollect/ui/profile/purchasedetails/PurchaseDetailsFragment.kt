package com.gthr.gthrcollect.ui.profile.purchasedetails

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.PurchaseDetailsFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class PurchaseDetailsFragment : BaseFragment<PurchaseDetailsViewModel, PurchaseDetailsFragmentBinding>() {

    override val mViewModel: PurchaseDetailsViewModel by viewModels()
    override fun getViewBinding() = PurchaseDetailsFragmentBinding.inflate(layoutInflater)

    override fun onBinding() {

    }
}
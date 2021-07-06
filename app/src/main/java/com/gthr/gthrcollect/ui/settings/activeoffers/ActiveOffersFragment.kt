package com.gthr.gthrcollect.ui.settings.activeoffers

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.ActiveOffersFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class ActiveOffersFragment : BaseFragment<ActiveOffersViewModel, ActiveOffersFragmentBinding>() {
    override fun getViewBinding() = ActiveOffersFragmentBinding.inflate(layoutInflater)
    override val mViewModel: ActiveOffersViewModel by viewModels()

    override fun onBinding() {}
}
package com.gthr.gthrcollect.ui.askflow.afconfigurecard

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.AfConfigureCardFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfConfigureCardFragment : BaseFragment<AskFlowViewModel, AfConfigureCardFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfConfigureCardFragmentBinding.inflate(layoutInflater)

    override fun onBinding() {

    }

}
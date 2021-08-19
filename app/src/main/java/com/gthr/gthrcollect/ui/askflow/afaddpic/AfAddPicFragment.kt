package com.gthr.gthrcollect.ui.askflow.afaddpic

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.AfAddPicFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfAddPicFragment : BaseFragment<AskFlowViewModel, AfAddPicFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()

    override fun getViewBinding() = AfAddPicFragmentBinding.inflate(layoutInflater)

    override fun onBinding() {

    }

}
package com.gthr.gthrcollect.ui.askflow.afedition

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.AfEditionFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfEditionFragment : BaseFragment<AskFlowViewModel, AfEditionFragmentBinding>() {
    override val mViewModel: AskFlowViewModel by viewModels()

    override fun getViewBinding() = AfEditionFragmentBinding.inflate(layoutInflater)

    override fun onBinding() {
        TODO("Not yet implemented")
    }
}
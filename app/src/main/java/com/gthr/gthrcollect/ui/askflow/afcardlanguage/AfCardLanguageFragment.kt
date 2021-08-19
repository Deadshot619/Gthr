package com.gthr.gthrcollect.ui.askflow.afcardlanguage

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.AfConfigureCardFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfCardLanguageFragment : BaseFragment<AskFlowViewModel, AfConfigureCardFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfConfigureCardFragmentBinding.inflate(layoutInflater)

    override fun onBinding() {
        TODO("Not yet implemented")
    }


}
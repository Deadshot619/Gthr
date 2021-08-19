package com.gthr.gthrcollect.ui.askflow.afcardlanguage

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.AfCardLanguageFragmentBinding
import com.gthr.gthrcollect.databinding.AfConfigureCardFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfCardLanguageFragment : BaseFragment<AskFlowViewModel, AfCardLanguageFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfCardLanguageFragmentBinding.inflate(layoutInflater)

    override fun onBinding() {

        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
    }

    private fun setUpClickListeners(){
        mViewBinding.root.setOnClickListener {
            findNavController().navigate(AfCardLanguageFragmentDirections.actionAfCardLanguageFragmentToAfEditionFragment())
        }
    }
}
package com.gthr.gthrcollect.ui.askflow.afbuylistdetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AfBuylistDetailsFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfBuyListDetailsFragment : BaseFragment<AskFlowViewModel, AfBuylistDetailsFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfBuylistDetailsFragmentBinding.inflate(layoutInflater)

    override fun onBinding() {

    }




}
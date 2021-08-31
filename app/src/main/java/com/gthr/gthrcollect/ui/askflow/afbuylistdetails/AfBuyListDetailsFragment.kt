package com.gthr.gthrcollect.ui.askflow.afbuylistdetails

import androidx.fragment.app.activityViewModels
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.AfBuylistDetailsFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton

class AfBuyListDetailsFragment : BaseFragment<AskFlowViewModel, AfBuylistDetailsFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels{
        AskFlowViewModelFactory(AskFlowRepository())
    }
    override fun getViewBinding() = AfBuylistDetailsFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnNext: CustomSecondaryButton

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnNext = btnNext
        }
    }

    private fun setUpClickListeners() {
        mViewBinding.run {
            mBtnNext.setOnClickListener {
                activity?.finish()
            }
        }
    }
}
package com.gthr.gthrcollect.ui.askflow.afbuylistdetails

import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.activityViewModels
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.AfBuylistDetailsFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.constants.CalendarConstants
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.extensions.showToast
import java.text.SimpleDateFormat
import java.util.*

class AfBuyListDetailsFragment : BaseFragment<AskFlowViewModel, AfBuylistDetailsFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels {
        AskFlowViewModelFactory(AskFlowRepository())
    }

    override fun getViewBinding() = AfBuylistDetailsFragmentBinding.inflate(layoutInflater)

    private lateinit var mTvBuyListValue: AppCompatTextView
    private lateinit var mTvTotalBuyListValue: TextView
    private lateinit var mTvDateValue: TextView
    private lateinit var mBtnNext: CustomSecondaryButton

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpObservers()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnNext = btnNext
            mTvBuyListValue = tvBuyListValue
            mTvTotalBuyListValue = tvTotalBuyListValue
            mTvDateValue = tvDateValue

            mTvDateValue.text =
                SimpleDateFormat(
                    CalendarConstants
                        .MONTH_DATE_YEAR, Locale.getDefault()
                ).format(Calendar.getInstance().time)
        }
    }

    private fun setUpClickListeners() {
        mViewBinding.run {
            mBtnNext.setOnClickListener {
                mViewModel.insertBid()
            }
        }
    }

    private fun setUpObservers() {
        mViewModel.buyListPrice.observe(viewLifecycleOwner) {
            mTvBuyListValue.text = String.format(getString(R.string.rate_common), it)
            mTvTotalBuyListValue.text = String.format(getString(R.string.rate_common), it)
        }

        mViewModel.insertBidRDB.observe(viewLifecycleOwner){
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        mViewModel.setBidId(it.data)
                        mViewModel.insertBuy()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.insertBuyRDB.observe(viewLifecycleOwner){
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        mViewModel.setBuyKey(it.data)
                        if(mViewModel.productDisplayModel?.highestBidCost!!<mViewModel.buyListPrice.value!!&&mViewModel.mBidId!=mViewModel.productDisplayModel?.highestBidID)
                            mViewModel.updateProductForBid()
                        else
                            (activity as AskFlowActivity)?.finish()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.updateProductForBidRDB.observe(viewLifecycleOwner){
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.finish()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

    }
}
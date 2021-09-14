package com.gthr.gthrcollect.ui.askflow.afreviewyourask


import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.AfReviewYourAskFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.extensions.visible
import com.gthr.gthrcollect.utils.helper.getTier

class AfReviewYourAskFragment : BaseFragment<AskFlowViewModel, AfReviewYourAskFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels {
        AskFlowViewModelFactory(AskFlowRepository())
    }

    override fun getViewBinding() = AfReviewYourAskFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnNext: CustomSecondaryButton
    private lateinit var mIvBack: ImageView
    private lateinit var mEtAsk: AppCompatEditText
    private lateinit var mEtBuyValue: AppCompatEditText

    private lateinit var mGroup: Group
    private lateinit var mGroupBuy: Group

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpTextChangeListeners()
        setUpObservers()
    }

    private fun setUpTextChangeListeners() {
        mEtAsk.afterTextChanged {
            mBtnNext.setState(
                if (it.toFloatOrNull() == null)
                    CustomSecondaryButton.State.DISABLE
                else
                    CustomSecondaryButton.State.BLUE_GRADIENT
            )
        }

        mEtBuyValue.afterTextChanged {
            mBtnNext.setState(
                if (it.toFloatOrNull() == null)
                    CustomSecondaryButton.State.DISABLE
                else
                    CustomSecondaryButton.State.BLUE_GRADIENT
            )
        }
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnNext = btnNext
            mIvBack = ivBack
            mGroup = group
            mGroupBuy = groupBuy
            mEtAsk = etAsk
            mEtBuyValue = etBuyValue
        }

        when ((requireActivity() as AskFlowActivity).getAskFlowType()) {
            AskFlowType.BUY -> {
                mBtnNext.text = getString(R.string.text_add_to_buylist_ask_flow)
                mGroup.gone()
                mGroupBuy.visible()
            }
            else -> {
                mBtnNext.text = getString(R.string.text_review_your_ask_flow)
                mGroup.visible()
                mGroupBuy.gone()
            }
        }

        //If this fragment is start destination, then hide back button
        if (findNavController().previousBackStackEntry == null)
            mIvBack.gone()
    }


    private fun setUpClickListeners() {
        mViewBinding.run {
            mBtnNext.setOnClickListener {
                when ((requireActivity() as AskFlowActivity).getAskFlowType()) {
                    AskFlowType.BUY ->
                        getBuylistPrice()?.let {
                            goToNextPage(it)
                        }
                    else ->
                        getAskValue()?.let {
                            val tier =
                                getTier(mViewModel.productDisplayModel!!, it.toDouble()).toString()
                            if (tier == "0")
                                mViewModel.getShippingTierInfo(tier)
                            else
                                goToNextPage(it)
                        }
                }
            }
        }
        mIvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpObservers() {
        mViewModel.shippingTierInfo.observe(viewLifecycleOwner, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Failed -> {
                        showToast(it.message)
                        (activity as AskFlowActivity).showProgressBar(false)
                    }
                    is State.Loading -> {
                        (activity as AskFlowActivity).showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity).showProgressBar(false)
                        goToNextPage(getAskValue()!!)
                    }
                }
            }
        })
    }

    private fun getAskValue(): Float? = mEtAsk.text.toString().toFloatOrNull()

    private fun getBuylistPrice(): Float? = mEtBuyValue.text.toString().toFloatOrNull()

    private fun goToNextPage(value: Float) {
        when ((requireActivity() as AskFlowActivity).getAskFlowType()) {
            AskFlowType.BUY -> mViewModel.setBuylistPrice(value)
            else -> mViewModel.setAskPrice(value)
        }
        findNavController().navigate(AfReviewYourAskFragmentDirections.actionAfReviewYourAskFragmentToAfPlaceYourAskFragment())
    }
}
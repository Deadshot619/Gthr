package com.gthr.gthrcollect.ui.askflow.afreviewyourask


import android.os.Build
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.AfReviewYourAskFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class AfReviewYourAskFragment : BaseFragment<AskFlowViewModel, AfReviewYourAskFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels{
        AskFlowViewModelFactory(AskFlowRepository())
    }
    override fun getViewBinding() = AfReviewYourAskFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnNext: CustomSecondaryButton
    private lateinit var mIvBack: ImageView
    private lateinit var mEtAsk: AppCompatEditText

    private lateinit var mGroup: Group
    private lateinit var mGroupBuy: Group

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpTextChangeListeners()
    }

    private fun setUpTextChangeListeners() {
        mEtAsk.afterTextChanged {
            mBtnNext.setState(
                if (mEtAsk.text.toString().toFloatOrNull() == null)
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
                mEtAsk.text.toString().toFloatOrNull()?.let {
                    mViewModel.setAskPrice(it)
                }
                findNavController().navigate(AfReviewYourAskFragmentDirections.actionAfReviewYourAskFragmentToAfPlaceYourAskFragment())
            }
        }
        mIvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }


}
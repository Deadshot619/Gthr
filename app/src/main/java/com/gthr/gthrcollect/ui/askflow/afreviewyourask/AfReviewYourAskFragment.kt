package com.gthr.gthrcollect.ui.askflow.afreviewyourask


import android.widget.ImageView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AfReviewYourAskFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.afaddpic.AfAddPicFragmentDirections
import com.gthr.gthrcollect.ui.askflow.afcardcondition.AfCardConditionFragmentDirections
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class AfReviewYourAskFragment : BaseFragment<AskFlowViewModel,AfReviewYourAskFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfReviewYourAskFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnNext: CustomSecondaryButton
    private lateinit var mIvBack: ImageView

    private lateinit var mGroup : Group
    private lateinit var mGroupBuy : Group

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnNext = btnNext
            mIvBack = ivBack
            mGroup = group
            mGroupBuy = groupBuy
        }

        when ((requireActivity() as AskFlowActivity).getAskFlowType()) {
            AskFlowType.BUY -> {
                mBtnNext.text = getString(R.string.text_add_to_buylist_ask_flow)
                mGroup.gone()
                mGroupBuy.visible()
            }
            else ->{
                mBtnNext.text = getString(R.string.text_review_your_ask_flow)
                mGroup.visible()
                mGroupBuy.gone()
            }
        }
    }


    private fun setUpClickListeners(){
        mViewBinding.run {
            mBtnNext.setOnClickListener {
                findNavController().navigate(AfReviewYourAskFragmentDirections.actionAfReviewYourAskFragmentToAfPlaceYourAskFragment())
            }
        }
        mIvBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }


}
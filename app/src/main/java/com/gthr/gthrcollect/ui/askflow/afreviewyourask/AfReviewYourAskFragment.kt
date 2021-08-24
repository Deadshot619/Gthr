package com.gthr.gthrcollect.ui.askflow.afreviewyourask


import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.AfReviewYourAskFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.afaddpic.AfAddPicFragmentDirections
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton

class AfReviewYourAskFragment : BaseFragment<AskFlowViewModel,AfReviewYourAskFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfReviewYourAskFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnNext: CustomSecondaryButton
    private lateinit var mIvBack: ImageView

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnNext = btnNext
            mIvBack = ivBack
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
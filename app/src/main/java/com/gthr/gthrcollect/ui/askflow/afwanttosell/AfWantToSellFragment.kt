package com.gthr.gthrcollect.ui.askflow.afwanttosell

import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.AfWantToSellFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton

class AfWantToSellFragment : BaseFragment<AskFlowViewModel, AfWantToSellFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() =  AfWantToSellFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvBack: ImageView
    private lateinit var mBtnNext: CustomSecondaryButton

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mIvBack = ivBack
            mBtnNext = btnNext
        }
    }

    private fun setUpClickListeners(){
        mViewBinding.run {
            mBtnNext.setOnClickListener {
                findNavController().navigate(AfWantToSellFragmentDirections.actionAfWantToSellFragmentToAfAddPicFragment())
            }
            mIvBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}
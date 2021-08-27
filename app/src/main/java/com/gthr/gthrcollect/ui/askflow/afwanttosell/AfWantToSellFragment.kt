package com.gthr.gthrcollect.ui.askflow.afwanttosell

import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.gthr.gthrcollect.databinding.AfWantToSellFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.extensions.gone

class AfWantToSellFragment : BaseFragment<AskFlowViewModel, AfWantToSellFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels()
    override fun getViewBinding() =  AfWantToSellFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvBack: ImageView
    private lateinit var mBtnNext: CustomSecondaryButton
    private lateinit var mSwitchWantToSell: SwitchMaterial

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mIvBack = ivBack
            mBtnNext = btnNext
            mSwitchWantToSell = scWantToSell

            //If this fragment is start destination, then hide back button
            if (findNavController().previousBackStackEntry == null)
                mIvBack.gone()
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

            mSwitchWantToSell.setOnClickListener {
                mViewModel.setSell(mSwitchWantToSell.isChecked)
            }
        }
    }
}
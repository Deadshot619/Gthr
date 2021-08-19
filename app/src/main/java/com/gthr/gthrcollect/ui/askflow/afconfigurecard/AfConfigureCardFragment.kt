package com.gthr.gthrcollect.ui.askflow.afconfigurecard

import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.AfConfigureCardFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.afcardlanguage.AfCardLanguageFragmentDirections
import com.gthr.gthrcollect.ui.askflow.afwanttosell.AfWantToSellFragmentDirections
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfConfigureCardFragment : BaseFragment<AskFlowViewModel, AfConfigureCardFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfConfigureCardFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvBack: ImageView

    override fun onBinding() {

        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mIvBack = ivBack
        }
    }

    private fun setUpClickListeners(){
        mViewBinding.run {
            root.setOnClickListener {
                findNavController().navigate(AfConfigureCardFragmentDirections.actionAfConfigureCardFragmentToAfWantToSellFragment())
            }

            mIvBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}
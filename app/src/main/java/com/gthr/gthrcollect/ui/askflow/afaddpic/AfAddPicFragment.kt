package com.gthr.gthrcollect.ui.askflow.afaddpic

import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.AfAddPicFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.afcardlanguage.AfCardLanguageFragmentDirections
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfAddPicFragment : BaseFragment<AskFlowViewModel, AfAddPicFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfAddPicFragmentBinding.inflate(layoutInflater)

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
            mIvBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}
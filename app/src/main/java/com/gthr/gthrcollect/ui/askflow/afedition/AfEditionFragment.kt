package com.gthr.gthrcollect.ui.askflow.afedition

import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.AfEditionFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.afcardlanguage.AfCardLanguageFragmentDirections
import com.gthr.gthrcollect.ui.askflow.afwanttosell.AfWantToSellFragmentDirections
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfEditionFragment : BaseFragment<AskFlowViewModel, AfEditionFragmentBinding>() {
    override val mViewModel: AskFlowViewModel by viewModels()

    override fun getViewBinding() = AfEditionFragmentBinding.inflate(layoutInflater)

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
                findNavController().navigate(AfEditionFragmentDirections.actionAfEditionFragmentToAfConfigureCardFragment())
            }

            mIvBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}
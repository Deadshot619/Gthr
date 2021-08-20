package com.gthr.gthrcollect.ui.askflow.afcardlanguage

import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.AfCardLanguageFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfCardLanguageFragment : BaseFragment<AskFlowViewModel, AfCardLanguageFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfCardLanguageFragmentBinding.inflate(layoutInflater)

    private lateinit var mTvEnglish : TextView
    private lateinit var mTvChineseS : TextView
    private lateinit var mTvChineseT : TextView
    private lateinit var mTvFrench : TextView

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mTvEnglish = tvEnglish
            mTvChineseS = tvChineseS
            mTvChineseT = tvChineseT
            mTvFrench = tvFrench
        }
    }

    private fun setUpClickListeners(){

        mTvEnglish.setOnClickListener {
            goToEdition()
        }
        mTvChineseS.setOnClickListener {
            goToEdition()
        }
        mTvChineseT.setOnClickListener {
            goToEdition()
        }
        mTvFrench.setOnClickListener {
            goToEdition()
        }
    }

    private fun goToEdition() {
        findNavController().navigate(AfCardLanguageFragmentDirections.actionAfCardLanguageFragmentToAfEditionFragment())
    }
}
package com.gthr.gthrcollect.ui.askflow.afcardedition

import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.AfCardEditionFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfCardEditionFragment : BaseFragment<AskFlowViewModel, AfCardEditionFragmentBinding>() {
    override val mViewModel: AskFlowViewModel by viewModels()

    override fun getViewBinding() = AfCardEditionFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvBack: ImageView
    private lateinit var mTvFail: TextView
    private lateinit var mTvFirstEdition: TextView
    private lateinit var mTvRevHolo: TextView
    private lateinit var mTvvAsdasd: TextView


    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mIvBack = ivBack
            mTvFail = tvFail
            mTvFirstEdition = tvFirstEdition
            mTvRevHolo = tvRevHolo
            mTvvAsdasd = tvAsdasd
        }
    }

    private fun setUpClickListeners(){
        mViewBinding.run {

            mTvFail.setOnClickListener {
                goToCardCondition()
            }
            mTvFirstEdition.setOnClickListener {
                goToCardCondition()
            }
            mTvRevHolo.setOnClickListener {
                goToCardCondition()
            }
            mTvvAsdasd.setOnClickListener {
                goToCardCondition()
            }

            mIvBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun goToCardCondition() {
        findNavController().navigate(AfCardEditionFragmentDirections.actionAfEditionFragmentToAfConfigureCardFragment())
    }
}
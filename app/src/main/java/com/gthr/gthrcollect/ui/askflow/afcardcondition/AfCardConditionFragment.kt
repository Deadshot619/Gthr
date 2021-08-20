package com.gthr.gthrcollect.ui.askflow.afcardcondition

import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.AfCardConditionFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AfCardConditionFragment : BaseFragment<AskFlowViewModel, AfCardConditionFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfCardConditionFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvBack: ImageView

    private lateinit var mTvChineseT : TextView
    private lateinit var mTvFrench : TextView
    private lateinit var mTvBgs : TextView
    private lateinit var mTvBgs9 : TextView

    private lateinit var mCctRaw : CustomCollectionTypeView
    private lateinit var mCctPsa : CustomCollectionTypeView
    private lateinit var mCctBgs : CustomCollectionTypeView
    private lateinit var mCctCgc : CustomCollectionTypeView

    private lateinit var mCctvList: List<CustomCollectionTypeView>
    private var mainJob: Job? = null

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mIvBack = ivBack
            mTvBgs = tvBlBgs
            mTvBgs9 = tvBgs95
            mTvChineseT = tvChineseT
            mTvFrench = tvFrench
            mCctRaw = cctRaw
            mCctPsa = cctPsa
            mCctBgs = cctBgs
            mCctCgc = cctCgc
            mCctvList = listOf(mCctRaw, mCctPsa, mCctBgs, mCctCgc)
        }
    }

    private fun setUpClickListeners(){
        mViewBinding.run {
            mTvChineseT.setOnClickListener {
                goToWantToSell()
            }
            mTvFrench.setOnClickListener {
                goToWantToSell()
            }
            mTvBgs.setOnClickListener {
                goToWantToSell()
            }
            mTvBgs9.setOnClickListener {
                goToWantToSell()
            }
            mIvBack.setOnClickListener {
                findNavController().navigateUp()
            }
            mCctvList.forEach { view ->
                view.setOnClickListener {
                    view.selectView()
                }
            }
        }
    }

    //Method to select Single Collection Filter
    private fun CustomCollectionTypeView.selectView() {
        if (this.mIsActive) return
        mainJob?.cancel()
        mainJob = MainScope().launch {
            mCctvList.forEach {
                it.setActive(it == this@selectView)
            }
        }
    }

    private fun goToWantToSell() {
        findNavController().navigate(AfCardConditionFragmentDirections.actionAfConfigureCardFragmentToAfWantToSellFragment())
    }

    override fun onDestroy() {
        super.onDestroy()
        mainJob?.cancel()
    }
}
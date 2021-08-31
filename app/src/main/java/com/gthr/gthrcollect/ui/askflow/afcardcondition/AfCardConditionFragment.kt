package com.gthr.gthrcollect.ui.askflow.afcardcondition

import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.AfCardConditionFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.askflow.ConfigurationAdapter
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import com.gthr.gthrcollect.utils.enums.AskFlowType
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AfCardConditionFragment : BaseFragment<AskFlowViewModel, AfCardConditionFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels{
        AskFlowViewModelFactory(AskFlowRepository())
    }
    override fun getViewBinding() = AfCardConditionFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvBack: ImageView

    private lateinit var mCctRaw : CustomCollectionTypeView
    private lateinit var mCctPsa : CustomCollectionTypeView
    private lateinit var mCctBgs : CustomCollectionTypeView
    private lateinit var mCctCgc : CustomCollectionTypeView

    private lateinit var mCctvList: List<CustomCollectionTypeView>
    private var mainJob: Job? = null

    private lateinit var mRvMain : RecyclerView
    private lateinit var mAdapter: ConfigurationAdapter

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpCondition()
    }

    private fun setUpCondition() {
        mAdapter = ConfigurationAdapter{
            goToNextPage()
        }
        mRvMain.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = mAdapter
        }
        mAdapter.submitList(getCondition())
    }

    private fun getCondition(): List<String> {
        val list = arrayListOf<String>()
        list.add(getString(R.string.bl_bgs))
        list.add(getString(R.string.bgs_9))
        list.add(getString(R.string.chinese))
        list.add(getString(R.string.french))
        list.add(getString(R.string.bl_bgs))
        list.add(getString(R.string.bgs_9))
        list.add(getString(R.string.chinese))
        list.add(getString(R.string.french))
        list.add(getString(R.string.bl_bgs))
        list.add(getString(R.string.bgs_9))
        list.add(getString(R.string.chinese))
        list.add(getString(R.string.french))
        return list
    }

        private fun initViews() {
        mViewBinding.run {
            mIvBack = ivBack
            mCctRaw = cctRaw
            mCctPsa = cctPsa
            mCctBgs = cctBgs
            mCctCgc = cctCgc
            mRvMain = rvMain
            mCctvList = listOf(mCctRaw, mCctPsa, mCctBgs, mCctCgc)
        }
    }

    private fun setUpClickListeners(){
        mViewBinding.run {
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

    private fun goToNextPage() {
        when ((requireActivity() as AskFlowActivity).getAskFlowType()) {
            AskFlowType.COLLECT -> findNavController().navigate(AfCardConditionFragmentDirections.actionAfConfigureCardFragmentToAfWantToSellFragment())
            AskFlowType.SELL -> findNavController().navigate(AfCardConditionFragmentDirections.actionAfConfigureCardFragmentToAfAddPicFragment())
            AskFlowType.BUY -> findNavController().navigate(AfCardConditionFragmentDirections.actionAfConfigureCardFragmentToAfReviewYourAskFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainJob?.cancel()
    }
}
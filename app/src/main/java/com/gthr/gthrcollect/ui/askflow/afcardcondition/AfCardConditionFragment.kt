package com.gthr.gthrcollect.ui.askflow.afcardcondition

import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.AfCardConditionFragmentBinding
import com.gthr.gthrcollect.model.domain.ConditionDomainModel
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.enums.ConditionType
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AfCardConditionFragment : BaseFragment<AskFlowViewModel, AfCardConditionFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels {
        AskFlowViewModelFactory(AskFlowRepository())
    }

    override fun getViewBinding() = AfCardConditionFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvBack: ImageView

    private lateinit var mCctRaw: CustomCollectionTypeView
    private lateinit var mCctPsa: CustomCollectionTypeView
    private lateinit var mCctBgs: CustomCollectionTypeView
    private lateinit var mCctCgc: CustomCollectionTypeView

    private lateinit var mCctvList: List<CustomCollectionTypeView>
    private var mainJob: Job? = null

    private lateinit var mRvMain: RecyclerView
    private lateinit var mAdapter: ConfigurationConditionAdapter

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpCondition()
        setUpObservers()
    }

    private fun setUpCondition() {
        mAdapter = ConfigurationConditionAdapter(object :
            ConfigurationConditionAdapter.IConfigurationConditionListener {
            override fun onClick(conditionDomainModel: ConditionDomainModel) {
                mViewModel.setSelectedConditionValue(conditionDomainModel)
                goToNextPage()
            }
        })
        mRvMain.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }
    }

    private fun setUpObservers() {
        mViewModel.conditionList.observe(viewLifecycleOwner, {
            it.peekContent().let {
                mAdapter.submitList(it)
            }
        })
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

    private fun setUpClickListeners() {
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
                if (it == this@selectView) {
                    it.setActive(true)
                    when (it) {
                        mCctRaw -> mViewModel.setSelectedConditionTitle(ConditionType.UG)
                        mCctPsa -> mViewModel.setSelectedConditionTitle(ConditionType.PSA)
                        mCctBgs -> mViewModel.setSelectedConditionTitle(ConditionType.BGS)
                        mCctCgc -> mViewModel.setSelectedConditionTitle(ConditionType.CGC)
                    }
                } else
                    it.setActive(false)
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
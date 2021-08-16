package com.gthr.gthrcollect.ui.homebottomnav.feed

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.FeedFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FeedFragment : BaseFragment<FeedViewModel, FeedFragmentBinding>() {

    override fun getViewBinding() = FeedFragmentBinding.inflate(layoutInflater)
    override val mViewModel: FeedViewModel by viewModels()

    private var mainJob: Job? = null

    private lateinit var mAll: CustomCollectionTypeView
    private lateinit var mCards: CustomCollectionTypeView
    private lateinit var mSealed: CustomCollectionTypeView
    private lateinit var mFunko: CustomCollectionTypeView

    private lateinit var mRvMain : RecyclerView

    //List of Collection filter views
    private lateinit var mCctvList: List<CustomCollectionTypeView>

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpFeedRecyclerView()
    }

    private fun setUpFeedRecyclerView() {
        mRvMain.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = FeedAdapter()
        }
    }

    private fun initViews() {
        mViewBinding.run {
            mAll = cctAll
            mCards = cctCards
            mSealed = cctSealed
            mFunko = cctFunko
            mRvMain = rvMain
            mCctvList = listOf(mAll, mCards, mSealed, mFunko)
        }
    }

    private fun setUpClickListeners() {
        mCctvList.forEach { view ->
            view.setOnClickListener {
                view.selectView()
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

    override fun onDestroy() {
        super.onDestroy()
        mainJob?.cancel()
    }
}
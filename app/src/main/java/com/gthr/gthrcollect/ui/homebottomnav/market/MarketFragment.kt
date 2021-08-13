package com.gthr.gthrcollect.ui.homebottomnav.market

import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.FeedRepository
import com.gthr.gthrcollect.databinding.MarketFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.homebottomnav.HomeBottomNavActivity
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import com.gthr.gthrcollect.utils.customviews.CustomSeeAllView
import com.gthr.gthrcollect.utils.enums.SearchType
import com.gthr.gthrcollect.utils.extensions.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MarketFragment : BaseFragment<MarketViewModel, MarketFragmentBinding>() {

    override fun getViewBinding() = MarketFragmentBinding.inflate(layoutInflater)

    private val repository = FeedRepository()
    override val mViewModel: MarketViewModel by viewModels {
            MarketViewModelFactory(
            repository
        )
    }

    private var mainJob: Job? = null

    private lateinit var mAll: CustomCollectionTypeView
    private lateinit var mCards: CustomCollectionTypeView
    private lateinit var mSealed: CustomCollectionTypeView
    private lateinit var mFunko: CustomCollectionTypeView

    private lateinit var mRvPopularCollections : RecyclerView
    private lateinit var mPopularCollectionsSeeAll : CustomSeeAllView
    private lateinit var mBannerImage : AppCompatImageView

    //List of Collection filter views
    private lateinit var mCctvList: List<CustomCollectionTypeView>

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpUpForSell()
        setUpObservers()
        mViewModel.fetchBannerImage()
    }

    private fun initViews() {
        mViewBinding.run {
            mAll = cctAll
            mCards = cctCards
            mSealed = cctSealed
            mFunko = cctFunko
            mRvPopularCollections = rvPopularCollections
            mPopularCollectionsSeeAll = csavPopularCollectionsSeeAll
            mBannerImage = ivBanner
            mCctvList = listOf(mAll, mCards, mSealed, mFunko)
            initProgressBar(mViewBinding.layoutProgress)
        }
    }

    private fun setUpObservers() {
        mViewModel.bannerImage.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        Glide.with(this).load(it.data).placeholder(R.drawable.banner)
                            .into(mBannerImage)
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }
    }

    private fun setUpUpForSell() {
        mRvPopularCollections.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = PopularCollectionAdapter()
        }
    }


    private fun setUpClickListeners() {
        mCctvList.forEach { view ->
            view.setOnClickListener {
                view.selectView()
            }
        }
        mPopularCollectionsSeeAll.setOnClickListener {
            goToSearch()
        }
    }

    private fun goToSearch() {
        (requireActivity() as HomeBottomNavActivity).goToSearch(SearchType.COLLECTIONS)
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
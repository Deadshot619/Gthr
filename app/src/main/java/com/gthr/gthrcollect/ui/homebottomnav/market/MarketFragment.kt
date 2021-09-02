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
import com.gthr.gthrcollect.ui.productdetail.adapter.ProductAdapter
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.customviews.CustomSeeAllView
import com.gthr.gthrcollect.utils.enums.ProductCategoryFilter
import com.gthr.gthrcollect.utils.enums.ProductSortFilter
import com.gthr.gthrcollect.utils.enums.ProductTypeOld
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

    private lateinit var mRvLowestAsk : RecyclerView
    private lateinit var mLowestAskSeeAll : CustomSeeAllView

    private lateinit var mRvHighestAsk : RecyclerView
    private lateinit var mHighestAskSeeAll : CustomSeeAllView

    private var mCurrentMarketType = MarketType.ALL

    //List of Collection filter views
    private lateinit var mCctvList: List<CustomCollectionTypeView>

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpPopularCollections()
        setUpLowestAsk()
        setUpHighestAsk()
        setUpObservers()
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
            mRvLowestAsk = rvLowestAskCollections
            mLowestAskSeeAll = csavLowestAskSeeAll
            mRvHighestAsk = rvHighestAskCollections
            mHighestAskSeeAll = csavHighestAskSeeAll
            mCctvList = listOf(mAll, mCards, mSealed, mFunko)
            initProgressBar(mViewBinding.layoutProgress)

            mRvPopularCollections.setNestedScrollingEnabled(false)
            mRvLowestAsk.setNestedScrollingEnabled(false)
            mRvHighestAsk.setNestedScrollingEnabled(false)
        }
    }

    private fun setUpObservers() {
        mViewModel.bannerImage.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        Glide.with(this).load(it.data).placeholder(R.drawable.banner).error(R.drawable.banner)
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

    private fun setUpPopularCollections() {
        mRvPopularCollections.apply {

            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = PopularCollectionAdapter()
        }
    }

    private fun setUpLowestAsk() {
        mRvLowestAsk.apply {

            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = ProductAdapter(ProductTypeOld.FUNKO, CustomProductCell.State.FOR_SALE)
        }
    }

    private fun setUpHighestAsk() {
        mRvHighestAsk.apply {

            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = ProductAdapter(ProductTypeOld.FUNKO,CustomProductCell.State.FOR_SALE)
        }
    }


    private fun setUpClickListeners() {
        mAll.setOnClickListener {
            mCurrentMarketType = MarketType.ALL
            mAll.selectView()
        }

        mCards.setOnClickListener {
            mCurrentMarketType = MarketType.CARDS
            mCards.selectView()
        }

        mSealed.setOnClickListener {
            mCurrentMarketType = MarketType.SEALED
            mSealed.selectView()
        }

        mFunko.setOnClickListener {
            mCurrentMarketType = MarketType.FUNKO
            mFunko.selectView()
        }

        mPopularCollectionsSeeAll.setOnClickListener {
            goToSearch(SearchType.COLLECTIONS,ProductSortFilter.NONE,getProductCategoryFilter())
        }
        mLowestAskSeeAll.setOnClickListener {
            goToSearch(SearchType.PRODUCT,ProductSortFilter.LOWEST_ASK,getProductCategoryFilter())
        }
        mHighestAskSeeAll.setOnClickListener {
            goToSearch(SearchType.PRODUCT,ProductSortFilter.HIGHEST_ASK,getProductCategoryFilter())
        }
    }

    private fun getProductCategoryFilter(): ProductCategoryFilter {
        return when(mCurrentMarketType){
            MarketType.ALL -> ProductCategoryFilter.NONE
            MarketType.CARDS -> ProductCategoryFilter.CARD
            MarketType.SEALED -> ProductCategoryFilter.SEALED
            MarketType.FUNKO -> ProductCategoryFilter.TOY
        }
    }

    private fun goToSearch(type : SearchType,filter: ProductSortFilter,categoryFilter : ProductCategoryFilter) {
        (requireActivity() as HomeBottomNavActivity).goToSearch(type,filter,categoryFilter)
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

    enum class MarketType{
        ALL,CARDS,SEALED,FUNKO
    }
}
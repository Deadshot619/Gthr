package com.gthr.gthrcollect.ui.homebottomnav.market

import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.FeedRepository
import com.gthr.gthrcollect.data.repository.SearchRepository
import com.gthr.gthrcollect.databinding.MarketFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.homebottomnav.HomeBottomNavActivity
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.customviews.CustomSeeAllView
import com.gthr.gthrcollect.utils.enums.*
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MarketFragment : BaseFragment<MarketViewModel, MarketFragmentBinding>() {

    override fun getViewBinding() = MarketFragmentBinding.inflate(layoutInflater)

    private val feedRepository = FeedRepository()
    private val searchRepository = SearchRepository()

    override val mViewModel: MarketViewModel by viewModels {
            MarketViewModelFactory(
                feedRepository,searchRepository
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

    private lateinit var mAdapterPopularCollections: PopularCollectionAdapter
    private lateinit var mLowestAskAdapter: AskAdapter
    private lateinit var mHighestAskAdapter: AskAdapter


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

        mViewModel.collectionList.observe(viewLifecycleOwner) {  it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                    is State.Success -> {
                        mAdapterPopularCollections.submitList(it.data)
                        showProgressBar(false)

                        GthrLogger.e("observedata", "data: ${it.data}")
                    }
                }
            }
        }

        // lowestAskList Obesrver
        mViewModel.lowestAskList.observe(viewLifecycleOwner) {  it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                    is State.Success -> {
                        mLowestAskAdapter.submitList(it.data)

                        showProgressBar(false)

                        GthrLogger.e("observedata", "data: ${it.data}")
                    }
                }
            }
        }

        // Height list ask observer
        mViewModel.heightAskList.observe(viewLifecycleOwner) {  it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                    is State.Success -> {
                        mHighestAskAdapter.submitList(it.data)

                        showProgressBar(false)

                        GthrLogger.e("observedata", "data: ${it.data}")
                    }
                }
            }
        }


    }

    private fun setUpPopularCollections() {

        mRvPopularCollections.apply {

            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            mAdapterPopularCollections = PopularCollectionAdapter {

                it.objectId?.let {
                    startActivity(
                        ProfileActivity.getInstance(
                            requireActivity(),
                            ProfileNavigationType.PROFILE, it
                        )
                    )
                }
            }
            adapter = mAdapterPopularCollections
        }



    }


    private fun setUpLowestAsk() {
        // Fetching Lowest Ask
        mViewModel.searchLowestAsk(isAscending = 0, limit = 10,sortBy = PRICE)


          /*  layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = ProductAdapter(ProductTypeOld.FUNKO, CustomProductCell.State.FOR_SALE)*/

            mRvLowestAsk.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                mLowestAskAdapter =
                    AskAdapter(CustomProductCell.State.FOR_SALE) {

                        startActivity(
                            AskFlowActivity.getInstance(
                                requireContext(),
                                AskFlowType.BUY_DIRECTLY_FROM_SOMEONE,
                                it
                            )
                        )

                    }
                mRvLowestAsk.adapter = mLowestAskAdapter

            }


    }

    private fun setUpHighestAsk() {
        mViewModel.searchHeightsAsk(isAscending = 1, limit = 10,sortBy = PRICE)

               /*   layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = ProductAdapter(ProductTypeOld.FUNKO,CustomProductCell.State.FOR_SALE)*/

            mRvHighestAsk.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                mHighestAskAdapter =
                    AskAdapter(CustomProductCell.State.FOR_SALE) {
                        startActivity(
                            AskFlowActivity.getInstance(
                                requireContext(),
                                AskFlowType.BUY_DIRECTLY_FROM_SOMEONE,
                                it
                            )
                        )

                    }
                mRvHighestAsk.adapter = mHighestAskAdapter

            }

    }


    private fun setUpClickListeners() {
        mAll.setOnClickListener {
            mCurrentMarketType = MarketType.ALL
            mAll.selectView()

            mViewModel.searchHeightsAsk(isAscending = 1,productCategory = null, limit = 10,sortBy = PRICE)
            mViewModel.searchLowestAsk(isAscending = 0,productCategory = null,limit = 10,sortBy = PRICE)
        }

        mCards.setOnClickListener {
            mCurrentMarketType = MarketType.CARDS
            mCards.selectView()
            mViewModel.searchHeightsAsk(isAscending = 1,productCategory = ProductCategory.CARDS.title,limit = 10,sortBy = PRICE)
            mViewModel.searchLowestAsk(isAscending = 0,productCategory = ProductCategory.CARDS.title,limit = 10,sortBy = PRICE)
        }

        mSealed.setOnClickListener {
            mCurrentMarketType = MarketType.SEALED
            mSealed.selectView()
            mViewModel.searchHeightsAsk(isAscending = 1,productCategory = ProductCategory.SEALED.title,limit = 10,sortBy = PRICE)
            mViewModel.searchLowestAsk(isAscending = 0,productCategory = ProductCategory.SEALED.title,limit = 10,sortBy = PRICE)


        }

        mFunko.setOnClickListener {
            mCurrentMarketType = MarketType.FUNKO
            mFunko.selectView()
            mViewModel.searchHeightsAsk(isAscending = 1,productCategory = ProductCategory.TOYS.title,limit = 10,sortBy = PRICE)
            mViewModel.searchLowestAsk(isAscending = 0,productCategory = ProductCategory.TOYS.title,limit = 10,sortBy = PRICE)


        }

        mPopularCollectionsSeeAll.setOnClickListener {
            goToSearch(SearchType.COLLECTIONS,ProductSortFilter.NONE,getProductCategoryFilter())
        }
        mLowestAskSeeAll.setOnClickListener {
            goToSearch(SearchType.FOR_SALE,ProductSortFilter.LOWEST_ASK,getProductCategoryFilter())
        }
        mHighestAskSeeAll.setOnClickListener {
            goToSearch(SearchType.FOR_SALE,ProductSortFilter.HIGHEST_ASK,getProductCategoryFilter())
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

    companion object{
        const val PRICE="price"
    }
}
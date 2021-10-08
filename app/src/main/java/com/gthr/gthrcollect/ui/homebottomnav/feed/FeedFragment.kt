package com.gthr.gthrcollect.ui.homebottomnav.feed

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.data.repository.FeedRepository
import com.gthr.gthrcollect.databinding.FeedFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.FeedDomainModel
import com.gthr.gthrcollect.model.domain.ForSaleItemDomainModel
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.editaccountinfo.EditAccountInfoActivity
import com.gthr.gthrcollect.ui.homebottomnav.HomeBottomNavActivity
import com.gthr.gthrcollect.ui.productdetail.ProductDetailActivity
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.utils.constants.DynamicLinkConstants
import com.gthr.gthrcollect.utils.constants.FirebaseStorage
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import com.gthr.gthrcollect.utils.enums.*
import com.gthr.gthrcollect.utils.extensions.isUserLoggedIn
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.getProductTypeFromObjectId
import com.gthr.gthrcollect.utils.helper.isUserVerified
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class FeedFragment : BaseFragment<FeedViewModel, FeedFragmentBinding>() {

    override fun getViewBinding() = FeedFragmentBinding.inflate(layoutInflater)
    override val mViewModel: FeedViewModel by viewModels{
        FeedViewModelFactory(
            FeedRepository(),
            DynamicLinkRepository()
        )
    }

    private var mainJob: Job? = null

    private lateinit var mAll: CustomCollectionTypeView
    private lateinit var mCards: CustomCollectionTypeView
    private lateinit var mSealed: CustomCollectionTypeView
    private lateinit var mFunko: CustomCollectionTypeView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mRvMain : RecyclerView
    private lateinit var mAdapter: FeedAdapter

    //List of Collection filter views
    private lateinit var mCctvList: List<CustomCollectionTypeView>

    //Filter var
    private var mProductCategory: ProductCategory? = null
    private var mCreatorUID: String? = null

    private var mLimit : Int = 30
    private var mPage: Int = 0

    //Paging
    private var mIsLoading = false
    private var mHasNext = true

    private lateinit var feedModel: FeedDomainModel

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpFeedRecyclerView()
        setUpObserver()
        setUpSwipeRefresh()
        getFeed(0,null,GthrCollect.prefs?.collectionInfoModel?.userRefKey?:null)
    }

    private fun setUpSwipeRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener {
            loadFeed()
        }
    }

    private fun loadFeed() {
        when{
            mAll.mIsActive-> getFeed(0,null,GthrCollect.prefs?.collectionInfoModel?.userRefKey?:null,true)
            mCards.mIsActive-> getFeed(0,ProductCategory.CARDS,GthrCollect.prefs?.collectionInfoModel?.userRefKey?:null,true)
            mSealed.mIsActive-> getFeed(0,ProductCategory.SEALED,GthrCollect.prefs?.collectionInfoModel?.userRefKey?:null,true)
            mFunko.mIsActive-> getFeed(0,ProductCategory.TOYS,GthrCollect.prefs?.collectionInfoModel?.userRefKey?:null,true)
        }
    }

    private fun getFeed(page: Int, productCategory: ProductCategory?, creatorUID: String?,setReset: Boolean = false){
        if (setReset)
            resetFeed()

        mPage = page
        mProductCategory = productCategory
        mCreatorUID = creatorUID
        mViewModel.fetchFeed(mLimit,mPage,mProductCategory,mCreatorUID)
    }

    private fun setUpObserver() {
        mViewModel.mDynamicLink.observe(this){
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)

                        var imageUrl = ""
                        if (feedModel.profileImageURL.isNullOrEmpty()){
                            imageUrl=FirebaseStorage.APP_ICON_URL
                        }else{
                            imageUrl=feedModel.profileImageURL.toString()
                        }

                        val textData = DynamicLinkConstants.CHECK_OUT+" "+feedModel.collectionDisplayName+" "+DynamicLinkConstants.PROFILE_ON_GTHR+" \n\n"+imageUrl + "\n\n"+it.data
                        val intent = Intent()
                        // val msg = "Click and install this application $shortLink Refer code : mayankbaba"
                        intent.action = Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_TEXT, textData)
                        intent.type = "text/plain"
                        startActivity(intent)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }

        mViewModel.mProductDynamicLink.observe(this){
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {

                        showProgressBar(false)

                        var imageUrl= ""
                        if (feedModel.product_firImageURL.isNullOrEmpty()){
                            imageUrl=FirebaseStorage.APP_ICON_URL
                        }else{
                            imageUrl=feedModel.product_firImageURL.toString()
                        }


                        val textData = DynamicLinkConstants.CHECK_OUT+" "+feedModel.product_productName+" "+DynamicLinkConstants.ON_GTHR+" \n\n"+imageUrl + "\n\n"+it.data

                        val intent = Intent()
                        // val msg = "Click and install this application $shortLink Refer code : mayankbaba"
                        intent.action = Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_TEXT, textData)
                        intent.type = "text/plain"
                        startActivity(intent)
                    }
                    is State.Failed -> showProgressBar(false)
                }
            }
        }


        mViewModel.mFeedDisplayList.observe(this){
            val list = it.map { it.copy() }
            mAdapter.submitList(list)
            GthrLogger.i("dscndskc", "=====================")
            list.forEach {
                GthrLogger.i("dscndskc", "${it.id}")
            }

        }

        mViewModel.mFeedList.observe(this){
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        if (mPage == 0){
                            if(!mSwipeRefreshLayout.isRefreshing)
                                showProgressBar()
                        }
                        else
                            mViewModel.addFeedDisplayListLoadMore()
                    }
                    is State.Failed -> {
                        mSwipeRefreshLayout.isRefreshing = false;
                        showProgressBar(false)
                        showToast(it.message)
                        mIsLoading = false
                        if (mPage == 0)
                            showProgressBar(false)
                        else
                            mViewModel.removeFeedDisplayListLoadMore()
                    }
                    is State.Success -> {
                        mSwipeRefreshLayout.isRefreshing = false;
                        showProgressBar(false)

                        mViewModel.setFeedDisplayList(it.data)
                        if (it.data.size < mLimit) {
                            mHasNext = false
                            GthrLogger.e("mayank", "data: ${mHasNext}")
                        }
                        mIsLoading = false
                        mPage++
                        GthrLogger.e("observedata", "data: ${it.data}")
                    }
                }
            }
        }
    }

    private fun setUpFeedRecyclerView() {
        mAdapter = FeedAdapter(object : FeedAdapter.FeedListener{
            override fun buyNow(feedDomainModel: FeedDomainModel) {
                when {
                    GthrCollect.prefs?.isUserLoggedIn() == true -> {
                        lifecycleScope.launch {
                            showProgressBar()
                            activity?.isUserVerified(runEverytime = {
                                showProgressBar(false)
                            }, verified = {
                                startActivity(
                                    AskFlowActivity.getInstance(
                                        requireContext(),
                                        AskFlowType.BUY_DIRECTLY_FROM_SOMEONE,
                                        ProductDisplayModel(ForSaleItemDomainModel(feedDomainModel))
                                    )
                                )
                            }, notVerified = {
                                startActivityForResult(
                                    EditAccountInfoActivity.getInstance(
                                        requireContext(),
                                        EditAccountInfoFlow.GOV_ID
                                    ), REQUEST_CODE_ID_VERIFICATION_BUY
                                )
                            })
                        }
                    }
                    else -> {
                        (activity as HomeBottomNavActivity).goToProfileSignUp()
                    }
                }
            }

            override fun goToProductDetail(feedDomainModel: FeedDomainModel) {
                if(!feedDomainModel.product_prodObjectID.isNullOrEmpty()){
                    startActivity(ProductDetailActivity.getInstance(requireContext(),feedDomainModel.product_prodObjectID, getProductTypeFromObjectId(feedDomainModel.product_prodObjectID)))
                }
            }

            override fun goToProfile(feedDomainModel: FeedDomainModel) {
                startActivity(ProfileActivity.getInstance(requireContext(), ProfileNavigationType.PROFILE,feedDomainModel.collection_firebaseRef))
            }

            override fun share(feedDomainModel: FeedDomainModel) {
                if (feedDomainModel.feedType == FeedType.BID || feedDomainModel.feedType == FeedType.ASK)
                    if (feedDomainModel.product_prodObjectID != null && feedDomainModel.productType != null)
                        mViewModel.getProductDynamicLink(
                            feedDomainModel.product_prodObjectID,
                            feedDomainModel.productType
                        )
                if (feedDomainModel.feedType == FeedType.COLLECTION)
                    if (feedDomainModel.collection_firebaseRef != null){
                        feedModel=feedDomainModel
                        mViewModel.getCollectionsDynamicLink(feedDomainModel.collection_firebaseRef)
                    }

            }
        })
        val linearLayoutManager = LinearLayoutManager(requireContext())
        mRvMain.apply {
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }

        mRvMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mPage == 0 || mHasNext) {
                    val total: Int = linearLayoutManager.itemCount
                    val lastVisibleItemCount: Int = linearLayoutManager.findLastVisibleItemPosition()
                    GthrLogger.i("sdnvksdnf", "onScrolled: total $total lastVisibleItemCount $lastVisibleItemCount ")
                    if (!mIsLoading) {
                        if (total > 0)
                            if ((total-1) == lastVisibleItemCount) {
                                mIsLoading = true
                                when {
                                    mAll.mIsActive -> getFeed(mPage,null,GthrCollect.prefs?.collectionInfoModel?.userRefKey?:null)
                                    mCards.mIsActive -> getFeed(mPage,ProductCategory.CARDS,GthrCollect.prefs?.collectionInfoModel?.userRefKey?:null)
                                    mSealed.mIsActive -> getFeed(mPage,ProductCategory.SEALED,GthrCollect.prefs?.collectionInfoModel?.userRefKey?:null)
                                    mFunko.mIsActive -> getFeed(mPage,ProductCategory.TOYS,GthrCollect.prefs?.collectionInfoModel?.userRefKey?:null)
                                }
                            }
                    }
                }
            }
        })
    }

    private fun initViews() {
        mViewBinding.run {
            mAll = cctAll
            mCards = cctCards
            mSealed = cctSealed
            mFunko = cctFunko
            mRvMain = rvMain
            mSwipeRefreshLayout = swipeRefreshItems
            mCctvList = listOf(mAll, mCards, mSealed, mFunko)
            initProgressBar(mViewBinding.layoutProgress)
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
            loadFeed()
        }
    }

    private fun resetFeed() {
        mPage = 0
        mHasNext = true
        mViewModel.clearFeedDisplayList()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainJob?.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ID_VERIFICATION_BUY)
                showToast(getString(R.string.text_id_under_review))
        }
    }

    companion object {
        private const val REQUEST_CODE_ID_VERIFICATION_BUY = 69
    }
}
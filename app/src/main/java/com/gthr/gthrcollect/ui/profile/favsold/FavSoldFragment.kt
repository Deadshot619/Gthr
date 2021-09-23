package com.gthr.gthrcollect.ui.profile.favsold

import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.data.repository.ProfileRepository
import com.gthr.gthrcollect.data.repository.SearchRepository
import com.gthr.gthrcollect.databinding.FollowFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.homebottomnav.search.SearchViewModelFactory
import com.gthr.gthrcollect.ui.productdetail.ProductDetailActivity
import com.gthr.gthrcollect.ui.profile.MyProfileViewModelFactory
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.ui.profile.follow.FavSoldAdapter
import com.gthr.gthrcollect.ui.profile.follow.FollowFragmentArgs
import com.gthr.gthrcollect.ui.profile.follow.FollowViewModel
import com.gthr.gthrcollect.ui.profile.follow.FollowViewModelFactory
import com.gthr.gthrcollect.ui.profile.my_profile.MyProfileFragment
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.customviews.CustomSearchView
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType
import com.gthr.gthrcollect.utils.extensions.getUserCollectionId
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch

class FavSoldFragment : BaseFragment<FollowViewModel, FollowFragmentBinding>() {

    override val mViewModel: FollowViewModel by viewModels{
        FollowViewModelFactory(
            ProfileRepository()
        )
    }
    override fun getViewBinding() = FollowFragmentBinding.inflate(layoutInflater)



    private val args by navArgs<FollowFragmentArgs>()
    private lateinit var mType: ProfileNavigationType

    private lateinit var mAdapter: FavSoldAdapter
    private lateinit var mFavProductAdapter: FavuriteProductAdapter

    private lateinit var mRvMain: RecyclerView
    private lateinit var mSearchView: CustomSearchView
    var mSearchTypingJob: Job? = null

    override fun onBinding() {
        mType = args.type

        initViews()
        setUpViews(mType)
        setUpClickListeners()
        setUpObservers()
        setUpRecyclerView(mType)
        setTextChangeListener()
        setKeyBoardListener()
    }

    private fun setKeyBoardListener() {
        mSearchView.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!mSearchView.text.toString().trim().isNullOrEmpty()) {
                        searchProduct()
                    }
                    return true
                }
                return false
            }
        })
    }

    private fun setTextChangeListener() {
        mSearchView.setTextChangeListener {
            mSearchTypingJob?.cancel()
            mSearchTypingJob = MainScope().launch {
                ticker(SEARCH_DELAY).receive()  //Add some delay before an api call
                searchProduct()
            }
        }
    }

    private fun searchProduct() {
        GthrLogger.e("dscnsdk", "data: ${mViewModel.mAllFavProductList}")
        val productList = mutableListOf<ProductDisplayModel>()
        for (productDisplayModel in mViewModel.mAllFavProductList) {
            if (productDisplayModel?.name!=null&&productDisplayModel?.name?.contains(mSearchView.text.toString(),true)!!)
                productList.add(productDisplayModel)
            else if(productDisplayModel?.productNumber!=null&&productDisplayModel?.productNumber?.contains(mSearchView.text.toString(),true)!!)
                productList.add(productDisplayModel)
            else if(productDisplayModel?.rarity!=null&&productDisplayModel?.rarity?.contains(mSearchView.text.toString(),true)!!)
                productList.add(productDisplayModel)
            else if(productDisplayModel?.description!=null&&productDisplayModel?.description?.contains(mSearchView.text.toString(),true)!!)
                productList.add(productDisplayModel)
        }
        GthrLogger.e("dscnsdk", "search: ${productList}")
        mViewModel.setDisplayFavProducts(productList)
    }

    private fun setUpObservers() {

        mViewModel.mDisplayFavProduct.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                GthrLogger.i("dscnsdk","List $it")
                GthrLogger.i("dscnsdk","List ${it.size}")
                mFavProductAdapter.submitList(it.map { it.copy() })
            }
        }

        mViewModel.favProductdata.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar(true)
                    }
                    is State.Failed -> {
                        showToast(it.message)
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        mViewModel.setDisplayFavProducts(it.data)
                        mViewModel.setAllFavProductList(it.data)
                        GthrLogger.e("mayank", "data: ${it.data.size}")
                    }
                }
            }
        }
    }

    private fun initViews() {
        mViewBinding.run {
            mRvMain = rvMain
            mSearchView = csvSearch
        }

        initProgressBar(mViewBinding.layoutProgress)
    }

    private fun setUpViews(type: ProfileNavigationType){
        when(type) {
            ProfileNavigationType.FAVOURITES -> {
                (activity as ProfileActivity).setToolbarTitle(getString(R.string.text_favorites))
                mSearchView.hint = getString(R.string.hint_favourites_search)
                GthrLogger.e("ProductList", "id: ${GthrCollect.prefs?.getUserCollectionId().toString()}")
                GthrLogger.e("ProductList", "collectionId: ${GthrCollect.prefs?.collectionInfoModel?.collectionId.toString()}")
//                    mViewModel.fetchFollowingData(GthrCollect.prefs?.collectionInfoModel?.collectionId.toString())
                    mViewModel.fetchFollowingData(GthrCollect.prefs?.getUserCollectionId().toString())
            }
            ProfileNavigationType.SOLD -> {
                (activity as ProfileActivity).setToolbarTitle(getString(R.string.title_sold))
                mSearchView.hint = getString(R.string.hint_sold_search)
            }
        }
    }

    private fun setUpClickListeners() {
    }

    private fun setUpRecyclerView(type: ProfileNavigationType) {

        when(type) {
            ProfileNavigationType.FAVOURITES -> {
                mFavProductAdapter = FavuriteProductAdapter(CustomProductCell.State.FAVORITE){
                    startActivity(ProductDetailActivity.getInstance(requireContext(), it.objectID!!,it.productType!!))
                }
                mRvMain.apply {
                    layoutManager = GridLayoutManager(requireContext(),2)
                    mRvMain.adapter = mFavProductAdapter
                }
            }
            ProfileNavigationType.SOLD -> {
                mAdapter = FavSoldAdapter(mType,{})
                mRvMain.apply {
                    layoutManager = GridLayoutManager(requireContext(),2)
                    mRvMain.adapter = mAdapter
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DELAY = 1000L
    }

}
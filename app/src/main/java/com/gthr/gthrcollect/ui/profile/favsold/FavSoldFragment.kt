package com.gthr.gthrcollect.ui.profile.favsold

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
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.homebottomnav.search.SearchViewModelFactory
import com.gthr.gthrcollect.ui.profile.MyProfileViewModelFactory
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.ui.profile.follow.FavSoldAdapter
import com.gthr.gthrcollect.ui.profile.follow.FollowFragmentArgs
import com.gthr.gthrcollect.ui.profile.follow.FollowViewModel
import com.gthr.gthrcollect.ui.profile.follow.FollowViewModelFactory
import com.gthr.gthrcollect.utils.customviews.CustomProductCell
import com.gthr.gthrcollect.utils.customviews.CustomSearchView
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.logger.GthrLogger

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

    override fun onBinding() {
        mType = args.type

        initViews()
        setUpViews(mType)
        setUpClickListeners()
        setUpObservers()
        setUpRecyclerView(mType)
    }

    private fun setUpObservers() {

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
                        mFavProductAdapter.submitList( it.data)

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
                mViewModel.fetchFollowingData(GthrCollect.prefs?.collectionInfoModel?.collectionId.toString())
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

                mFavProductAdapter = FavuriteProductAdapter(CustomProductCell.State.FAVORITE,{})
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

}
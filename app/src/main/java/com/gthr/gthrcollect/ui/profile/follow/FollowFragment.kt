package com.gthr.gthrcollect.ui.profile.follow

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.FollowFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.profile.MyProfileViewModelFactory
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.ui.profile.ProfileViewModel
import com.gthr.gthrcollect.data.repository.ProfileRepository
import com.gthr.gthrcollect.utils.customviews.CustomSearchView
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType
import com.gthr.gthrcollect.utils.extensions.getUserCollectionId
import com.gthr.gthrcollect.utils.extensions.showToast

class FollowFragment : BaseFragment<ProfileViewModel, FollowFragmentBinding>() {
    private val repository = ProfileRepository()


    override val mViewModel: ProfileViewModel by viewModels {
        MyProfileViewModelFactory(
            repository,
            null
        )
    }

    override fun getViewBinding() = FollowFragmentBinding.inflate(layoutInflater)

    private val args by navArgs<FollowFragmentArgs>()
    private lateinit var mType: ProfileNavigationType
    private var otherUserId: String? =null

    private lateinit var mAdapter: FollowUserAdapter
    private lateinit var mRvMain: RecyclerView
    private lateinit var mSearchView: CustomSearchView

    override fun onBinding() {
        mType = args.type
        otherUserId = args.userCId

        initViews()
        setUpViews(mType)
        setUpClickListeners()
        setUpObservers()

        if (mType == ProfileNavigationType.FOLLOWERS) {
            if (otherUserId.isNullOrEmpty())
                mViewModel.fetchFollowersData(GthrCollect.prefs?.getUserCollectionId().toString())
            else
                mViewModel.fetchFollowersData(otherUserId!!)
        } else {
            mViewModel.fetchFollowingData(GthrCollect.prefs?.getUserCollectionId().toString())
        }
    }

    private fun setUpObservers() {

        mViewModel.followersList.observe(viewLifecycleOwner, {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        mAdapter.submitList(it.data)
                        showProgressBar(false)
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        })

        mViewModel.followingList.observe(viewLifecycleOwner, {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        mAdapter.submitList(it.data)
                        showProgressBar(false)
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        })

    }

    private fun initViews() {
        initProgressBar(mViewBinding.layoutProgress)

        mViewBinding.run {
            mRvMain = rvMain
            mSearchView = csvSearch
        }

        setUpRecyclerView()
    }

    private fun setUpViews(type: ProfileNavigationType) {
        when (type) {
            ProfileNavigationType.FOLLOWING -> {
                (activity as ProfileActivity).setToolbarTitle(getString(R.string.title_following))
                mSearchView.hint = getString(R.string.hint_following_search)
            }
            ProfileNavigationType.FOLLOWERS -> {
                (activity as ProfileActivity).setToolbarTitle(getString(R.string.title_followers))
                mSearchView.hint = getString(R.string.hint_follower_search)
            }
        }
    }

    private fun setUpClickListeners() {

    }

    private fun setUpRecyclerView() {
        mAdapter = FollowUserAdapter(object : FollowUserAdapter.FollowUserListener {
            override fun onClick(collectionInfoDomainModel: CollectionInfoDomainModel) {
                findNavController().navigate(
                    FollowFragmentDirections.actionFollowFragment2ToMyProfileFragment(collectionInfoDomainModel.collectionId)
                )
            }
        })
        mRvMain.apply {
            layoutManager = LinearLayoutManager(requireContext())
            mRvMain.adapter = mAdapter
        }
    }

}
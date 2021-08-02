package com.gthr.gthrcollect.ui.profile.follow

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.FollowFragmentBinding
import com.gthr.gthrcollect.model.domain.FollowDomainModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.ui.profile.navigation.ProfileNavigationFragmentArgs
import com.gthr.gthrcollect.utils.customviews.CustomSearchView
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType

class FollowFragment : BaseFragment<FollowViewModel, FollowFragmentBinding>() {
    override val mViewModel: FollowViewModel by viewModels()
    override fun getViewBinding() = FollowFragmentBinding.inflate(layoutInflater)

    private val args by navArgs<FollowFragmentArgs>()
    private lateinit var mType: ProfileNavigationType

    private lateinit var mAdapter: FollowUserAdapter
    private lateinit var mRvMain: RecyclerView
    private lateinit var mSearchView: CustomSearchView

    override fun onBinding() {
        mType = args.type

        initViews()
        setUpViews(mType)
        setUpClickListeners()
        setUpRecyclerView()
    }

    private fun initViews() {
        mViewBinding.run {
            mRvMain = rvMain
            mSearchView = csvSearch
        }
    }

    private fun setUpViews(type: ProfileNavigationType){
        when(type) {
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
        mAdapter = FollowUserAdapter(object : FollowUserAdapter.FollowUserListener{
            override fun onClick(followDomainModel: FollowDomainModel?) {
                findNavController().navigate(FollowFragmentDirections.actionFollowFragment2ToMyProfileFragment(isOtherUser = true))
            }
        })
        mRvMain.apply {
            layoutManager = LinearLayoutManager(requireContext())
            mRvMain.adapter = mAdapter
        }
    }
}
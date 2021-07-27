package com.gthr.gthrcollect.ui.profile.my_profile

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.MyProfileBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.profile.follow.FollowUserAdapter
import com.gthr.gthrcollect.ui.profile.follow.FollowViewModel
import com.gthr.gthrcollect.utils.customviews.CustomFelloView

class MyProfileFragment : BaseFragment<FollowViewModel, MyProfileBinding>() {
    override val mViewModel: FollowViewModel by viewModels()
    override fun getViewBinding() = MyProfileBinding.inflate(layoutInflater)

    private lateinit var mAdapter: FollowUserAdapter
    private lateinit var mRvMain: RecyclerView
    private lateinit var mFollower: CustomFelloView

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpRecyclerView()
    }

    private fun initViews() {
        mRvMain = mViewBinding.rvMain
    }

    private fun setUpClickListeners() {


    }

    private fun setUpRecyclerView() {

        mAdapter = FollowUserAdapter {}
        mRvMain.apply {

            layoutManager = LinearLayoutManager(requireContext())
            mRvMain.adapter = mAdapter

        }
    }

}
package com.gthr.gthrcollect.ui.profile.favsold

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.FollowFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.profile.follow.FollowViewModel

class FavSoldFragment : BaseFragment<FollowViewModel, FollowFragmentBinding>() {
    override val mViewModel: FollowViewModel by viewModels()
    override fun getViewBinding() = FollowFragmentBinding.inflate(layoutInflater)

    private lateinit var mRvMain: RecyclerView

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

    }

}
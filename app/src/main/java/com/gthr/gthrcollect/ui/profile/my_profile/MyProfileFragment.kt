package com.gthr.gthrcollect.ui.profile.my_profile

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.MyProfileBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.ui.profile.follow.FollowUserAdapter
import com.gthr.gthrcollect.ui.profile.follow.FollowViewModel
import com.gthr.gthrcollect.utils.customviews.CustomCollectionButton
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import com.gthr.gthrcollect.utils.customviews.CustomFelloView
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType
import com.gthr.gthrcollect.utils.extensions.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MyProfileFragment : BaseFragment<FollowViewModel, MyProfileBinding>() {
    override val mViewModel: FollowViewModel by viewModels()
    override fun getViewBinding() = MyProfileBinding.inflate(layoutInflater)

    private var mainJob: Job? = null

    private lateinit var mAdapter: FollowUserAdapter
    private lateinit var mRvMain: RecyclerView
    private lateinit var mFollowers: CustomFelloView
    private lateinit var mFollowing: CustomFelloView
    private lateinit var mSold: CustomFelloView
    private lateinit var mFavourites: CustomCollectionButton

    private lateinit var mAll: CustomCollectionTypeView
    private lateinit var mCards: CustomCollectionTypeView
    private lateinit var mToys: CustomCollectionTypeView
    private lateinit var mBuyList: CustomCollectionTypeView

    //List of Collection filter views
    private lateinit var mCctvList: List<CustomCollectionTypeView>

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpRecyclerView()
    }

    private fun initViews() {
        mViewBinding.run {
            mRvMain = rvMain
            mFavourites = ccbFavourites
            mFollowers = profileLayout.follower
            mFollowing = profileLayout.following
            mSold = profileLayout.sold
            mAll = cctvAll
            mCards = cctvCards
            mToys = cctvToys
            mBuyList = cctvBuyList
            mCctvList = listOf(mAll, mCards, mToys, mBuyList)
        }
    }

    private fun setUpClickListeners() {
        mFollowers.setOnClickListener {
            goToProfilePage(ProfileNavigationType.FOLLOWERS)
        }
        mFollowing.setOnClickListener {
            goToProfilePage(ProfileNavigationType.FOLLOWING)
        }
        mSold.setOnClickListener {
            goToProfilePage(ProfileNavigationType.SOLD)
        }
        mFavourites.setOnClickListener {
            goToProfilePage(ProfileNavigationType.FAVOURITES)
        }

        mCctvList.forEach { view ->
            view.setOnClickListener {
                view.selectView()
            }
        }
    }

    private fun setUpRecyclerView() {
        mAdapter = FollowUserAdapter {}
        mRvMain.apply {
            layoutManager = LinearLayoutManager(requireContext())
            mRvMain.adapter = mAdapter
        }
    }

    private fun goToProfilePage(type: ProfileNavigationType){
        startActivity(ProfileActivity.getInstance(requireContext(), type))
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
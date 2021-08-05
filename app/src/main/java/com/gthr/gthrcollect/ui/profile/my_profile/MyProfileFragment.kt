package com.gthr.gthrcollect.ui.profile.my_profile

import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.MyProfileBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.domain.FollowDomainModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.editprofile.EditProfileActivity
import com.gthr.gthrcollect.ui.profile.MyProfileViewModelFactory
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.ui.profile.ProfileViewModel
import com.gthr.gthrcollect.ui.profile.editprofile.ProfileRepository
import com.gthr.gthrcollect.ui.profile.follow.FollowUserAdapter
import com.gthr.gthrcollect.utils.customviews.CustomCollectionButton
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import com.gthr.gthrcollect.utils.customviews.CustomFelloView
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType
import com.gthr.gthrcollect.utils.extensions.showToast
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MyProfileFragment : BaseFragment<ProfileViewModel, MyProfileBinding>() {
    private val repository = ProfileRepository()

    override val mViewModel: ProfileViewModel by viewModels {
        MyProfileViewModelFactory(
            repository
        )
    }

    override fun getViewBinding() = MyProfileBinding.inflate(layoutInflater)

    private var mainJob: Job? = null

    private lateinit var mAdapter: FollowUserAdapter
    private lateinit var mRvMain: RecyclerView
    private lateinit var mFollowers: CustomFelloView
    private lateinit var mFollowing: CustomFelloView
    private lateinit var mSold: CustomFelloView
    private lateinit var mFavourites: CustomCollectionButton
    private lateinit var mEdit: AppCompatImageView
    private lateinit var mAbout: TextView
    private lateinit var mDisplayName: TextView
    private lateinit var mProfilePic: CircleImageView

    private lateinit var mAll: CustomCollectionTypeView
    private lateinit var mCards: CustomCollectionTypeView
    private lateinit var mToys: CustomCollectionTypeView
    private lateinit var mBuyList: CustomCollectionTypeView

    private var imageURl : String=""

    //List of Collection filter views
    private lateinit var mCctvList: List<CustomCollectionTypeView>

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpRecyclerView()
        setUpObservers()
    }

    private fun initViews() {
        mViewBinding.run {
            mRvMain = rvMain
            mFavourites = ccbFavourites
            mFollowers = profileLayout.follower
            mFollowing = profileLayout.following
            mSold = profileLayout.sold
            mEdit = profileLayout.ivEdit
            mAll = cctvAll
            mCards = cctvCards
            mToys = cctvToys
            mBuyList = cctvBuyList
            mCctvList = listOf(mAll, mCards, mToys, mBuyList)
            mAbout = profileLayout.tvUserBio
            mProfilePic = profileLayout.ivProfilePic
            mDisplayName = profileLayout.tvUserName
            initProgressBar(mViewBinding.layoutProgress)
        }
    }

    private fun setUpObservers() {

        mViewModel.userCollectionInfo.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        setData(it.data)
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.userProfilePic.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        imageURl=it.data
                        Glide.with(this).load(it.data).placeholder(R.drawable.profile_pic)
                            .into(mProfilePic)
                    }
                    is State.Failed -> {
                        showToast(it.message)
                    }
                }
            }
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
        mEdit.setOnClickListener {
            startActivity(
                EditProfileActivity.getInstance(
                    requireContext(),
                    display_name = mDisplayName.text.toString().trim(),
                    about = mAbout.text.toString().trim(),imageURl
                )
            )
        }

        mCctvList.forEach { view ->
            view.setOnClickListener {
                view.selectView()
            }
        }
    }

    private fun setData(data: CollectionInfoDomainModel) {
        mAbout.setText(data.about)
        mDisplayName.setText(data.collectionDisplayName)
        mFollowers.setCount(data.followersList?.size.toString())
        mFollowing.setCount(data.favoriteCollectionList?.size.toString())
    }

    private fun setUpRecyclerView() {
        mAdapter = FollowUserAdapter(object : FollowUserAdapter.FollowUserListener {
            override fun onClick(followDomainModel: FollowDomainModel?) {
            }
        })
        mRvMain.apply {
            layoutManager = LinearLayoutManager(requireContext())
            mRvMain.adapter = mAdapter
        }
    }

    private fun goToProfilePage(type: ProfileNavigationType) {
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
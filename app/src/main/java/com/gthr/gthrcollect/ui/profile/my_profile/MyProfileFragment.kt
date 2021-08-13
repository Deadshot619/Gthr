package com.gthr.gthrcollect.ui.profile.my_profile

import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.MyProfileBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.editprofile.EditProfileActivity
import com.gthr.gthrcollect.ui.profile.MyProfileViewModelFactory
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.ui.profile.ProfileViewModel
import com.gthr.gthrcollect.data.repository.ProfileRepository
import com.gthr.gthrcollect.ui.profile.follow.FollowUserAdapter
import com.gthr.gthrcollect.utils.customviews.CustomCollectionButton
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import com.gthr.gthrcollect.utils.customviews.CustomFelloView
import com.gthr.gthrcollect.utils.customviews.CustomFollowView
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.setImageByUrl
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.extensions.visible
import com.gthr.gthrcollect.utils.logger.GthrLogger
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MyProfileFragment : BaseFragment<ProfileViewModel, MyProfileBinding>() {
    private val repository = ProfileRepository()

    private val args by navArgs<MyProfileFragmentArgs>()

    //Variable to indicate whether the user is current user or other user
    private var otherUserId: String? = null

    override val mViewModel: ProfileViewModel by viewModels {
        MyProfileViewModelFactory(
            repository,
            otherUserId
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
    private lateinit var mIvShare: AppCompatImageView
    private lateinit var mBtnFollow: CustomFollowView
    private lateinit var mAbout: TextView
    private lateinit var mDisplayName: TextView
    private lateinit var mProfilePic: CircleImageView

    private lateinit var mAll: CustomCollectionTypeView
    private lateinit var mCards: CustomCollectionTypeView
    private lateinit var mToys: CustomCollectionTypeView
    private lateinit var mBuyList: CustomCollectionTypeView

    private var imageURl: String = ""

    //List of Collection filter views
    private lateinit var mCctvList: List<CustomCollectionTypeView>

    override fun onBinding() {
        try {
            otherUserId = args.otherUserId/*"-MSsuhfKKQP7mxZocWER"*/
        } catch (e: Exception) {
            GthrLogger.e(message = e.message.toString())
        }

        initViews()
        setUpClickListeners()
     //   setUpRecyclerView()
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
            mIvShare = profileLayout.ivShare
            mBtnFollow = profileLayout.ivFollow
            mAll = cctvAll
            mCards = cctvCards
            mToys = cctvToys
            mBuyList = cctvBuyList
            mCctvList = listOf(mAll, mCards, mToys, mBuyList)
            mAbout = profileLayout.tvUserBio
            mProfilePic = profileLayout.ivProfilePic
            mDisplayName = profileLayout.tvUserName
            initProgressBar(layoutProgress)
            setViewsForOtherUser()
        }
    }

    private fun setViewsForOtherUser(){
        if(!otherUserId.isNullOrEmpty()){
            mEdit.gone()
            mBtnFollow.visible()
            mFollowing.setTypeCollection()
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
        mAbout.text = data.about
        mDisplayName.text = data.collectionDisplayName
        mFollowers.setCount(data.followersList?.size.toString())
        mFollowing.setCount(data.favoriteCollectionList?.size.toString())
        imageURl = data.profileImage
        mProfilePic.setImageByUrl(imageURl)
    }

    private fun setUpRecyclerView() {
      /*  mAdapter = FollowUserAdapter(object : FollowUserAdapter.FollowUserListener {
            override fun onClick(followDomainModel: FollowDomainModel?) {
            }
        })*/
        mRvMain.apply {
            layoutManager = LinearLayoutManager(requireContext())
            mRvMain.adapter = mAdapter
        }
    }

    private fun goToProfilePage(type: ProfileNavigationType) {
        startActivity(ProfileActivity.getInstance(requireContext(), type, null))
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
package com.gthr.gthrcollect.ui.profile.my_profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.data.repository.ProfileRepository
import com.gthr.gthrcollect.databinding.MyProfileBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.editprofile.EditProfileActivity
import com.gthr.gthrcollect.ui.homebottomnav.HomeBottomNavActivity
import com.gthr.gthrcollect.ui.profile.MyProfileViewModelFactory
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.ui.profile.ProfileViewModel
import com.gthr.gthrcollect.utils.customviews.*
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType
import com.gthr.gthrcollect.utils.extensions.*
import com.gthr.gthrcollect.utils.logger.GthrLogger
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MyProfileFragment : BaseFragment<ProfileViewModel, MyProfileBinding>() {


    private val args by navArgs<MyProfileFragmentArgs>()

    //Variable to indicate whether the user is current user or other user
    private var otherUserId: String? = null

    override val mViewModel: ProfileViewModel by viewModels {
        MyProfileViewModelFactory(
            ProfileRepository(),
            DynamicLinkRepository(),
            otherUserId
        )
    }

    override fun getViewBinding() = MyProfileBinding.inflate(layoutInflater)

    private var mainJob: Job? = null

    private lateinit var mAdapter: CollectionsAdapter
    private lateinit var mMlMain: MotionLayout
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
    private lateinit var mCcbCollection: CustomCollectionButton

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
            mMlMain = mlMain
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
            mCcbCollection = ccbCollection
            initProgressBar(layoutProgress)
            setViewsForOtherUser()
            setUpRecyclerView()
        }
    }

    private fun setViewsForOtherUser() {
        if (isOtherUser()) {
            mEdit.gone()
            mBtnFollow.visible()
            mFollowing.setTypeCollection()
            mMlMain.getConstraintSet(R.id.start)?.let { startConstraintSet ->
                startConstraintSet.setVisibility(R.id.linearLayoutCompat, View.GONE)
                startConstraintSet.setVisibility(R.id.collection_layout, View.GONE)
            }

        }

        mAdapter = CollectionsAdapter(CustomProductCell.State.FOR_SALE) { }
    }

    private fun setUpObservers() {
        mViewModel.mProductDynamicLink.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        val intent = Intent()
                        // val msg = "Click and install this application $shortLink Refer code : mayankbaba"
                        intent.action = Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_TEXT, it.data)
                        intent.type = "text/plain"
                        startActivity(intent)
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }
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

        mViewModel.followUser.observe(viewLifecycleOwner, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        showToast(it.data)
                        mBtnFollow.setFollowing()
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        })

        mViewModel.unFollowUser.observe(viewLifecycleOwner, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        showToast(it.data)
                        mBtnFollow.setFollow()
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        })

    }

    private fun setUpClickListeners() {
        mIvShare.setOnClickListener {
            mViewModel.getProductDynamicLink(otherUserId ?: GthrCollect.prefs?.getUserCollectionId().toString())
        }

        mFollowers.setOnClickListener {
            goToProfilePage(ProfileNavigationType.FOLLOWERS, otherUserId)

        }
        mFollowing.setOnClickListener {
            if (!isOtherUser())
                goToProfilePage(ProfileNavigationType.FOLLOWING)
        }
        mSold.setOnClickListener {
            goToProfilePage(ProfileNavigationType.SOLD)
        }
        mFavourites.setOnClickListener {
            goToProfilePage(ProfileNavigationType.FAVOURITES)
        }
        mEdit.setOnClickListener {
            startActivityForResult(
                EditProfileActivity.getInstance(
                    requireContext(),
                    display_name = mDisplayName.text.toString().trim(),
                    about = mAbout.text.toString().trim(), imageURl
                ),
                REQUEST_CODE_EDIT_PROFILE
            )
        }

        mBtnFollow.setOnClickListener {
            if(GthrCollect.prefs?.isUserLoggedIn()==true){
                if (mBtnFollow.mCurrentType == CustomFollowView.Type.FOLLOW) {
                    mViewModel.followToUser(otherUserId!!)
                } else {
                    mViewModel.unFollowToUser(otherUserId!!)
                }
            }
            else{
                startActivity(HomeBottomNavActivity.getInstance(requireContext()).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                activity?.finish()
            }

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
        mFollowing.setCount(
            if (isOtherUser())
                data.collectionList?.size?.toString() ?: "0"
            else
                data.favoriteCollectionList?.size.toString()
        )
        imageURl = data.profileImage
        mProfilePic.setProfileImage(imageURl)

        if (isOtherUser()) {
            for (item: String in data.followersList!!) {
                if (item == GthrCollect.prefs?.userInfoModel?.collectionId.toString()) {
                    mBtnFollow.setFollowing()
                    break
                }
            }
        }

    }

    private fun setUpRecyclerView() {
        mRvMain.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            mRvMain.adapter = mAdapter
        }
    }

    private fun goToProfilePage(type: ProfileNavigationType, userId: String? = null) {
        startActivity(ProfileActivity.getInstance(requireContext(), type, userId))
    }

    //Method to select Single Collection Filter
    private fun CustomCollectionTypeView.selectView() {
        if (this.mIsActive) return

        mainJob?.cancel()
        mainJob = MainScope().launch {
            mCctvList.forEach {
                it.setActive(it == this@selectView)
            }
            when {
                mAll.mIsActive || mCards.mIsActive || mToys.mIsActive -> {
                    mAdapter = CollectionsAdapter(CustomProductCell.State.FOR_SALE) { }
                    mRvMain.adapter = mAdapter
                }
                mBuyList.mIsActive -> {
                    mAdapter = CollectionsAdapter(CustomProductCell.State.WANT) { }
                    mRvMain.adapter = mAdapter
                }
            }
        }
    }

    //Method to check if current user is Other User i.e. viewing other's profile
    private fun isOtherUser() = !otherUserId.isNullOrEmpty()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            when (requestCode) {
                REQUEST_CODE_EDIT_PROFILE -> {
                    if (resultCode == RESULT_OK)
                        mViewModel.fetchUserProfileData(
                            GthrCollect.prefs?.getUserCollectionId().toString()
                        )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainJob?.cancel()
    }

    companion object {
        private const val REQUEST_CODE_EDIT_PROFILE = 69
    }
}
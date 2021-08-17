package com.gthr.gthrcollect.ui.profile.navigation

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gthr.gthrcollect.databinding.ProfileNavigationFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.editaccountinfo.eaotp.EaOtpFragmentArgs
import com.gthr.gthrcollect.ui.profile.ProfileViewModel
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType

class ProfileNavigationFragment: BaseFragment<ProfileViewModel, ProfileNavigationFragmentBinding>() {
    override val mViewModel: ProfileViewModel by viewModels()
    override fun getViewBinding() = ProfileNavigationFragmentBinding.inflate(layoutInflater)

    private val args by navArgs<ProfileNavigationFragmentArgs>()

    private lateinit var mType: ProfileNavigationType
    private var mOtherUserCollectionId: String? = null

    override fun onBinding() {
        mType = args.type
        mOtherUserCollectionId = args.otherUserId

        navigateTo(mType)
    }

    private fun navigateTo(profileNavigationType: ProfileNavigationType){
        when(profileNavigationType){
            ProfileNavigationType.FOLLOWERS, ProfileNavigationType.FOLLOWING -> {
                findNavController().navigate(ProfileNavigationFragmentDirections.actionProfileNavigationFragmentToFollowFragment2(profileNavigationType,mOtherUserCollectionId))
            }
            ProfileNavigationType.FAVOURITES, ProfileNavigationType.SOLD -> {
                findNavController().navigate(ProfileNavigationFragmentDirections.actionProfileNavigationFragmentToFavSoldFragment(profileNavigationType))
            }
            ProfileNavigationType.PROFILE -> {
                findNavController().navigate(ProfileNavigationFragmentDirections.actionProfileNavigationFragmentToMyProfileFragment(mOtherUserCollectionId))
            }
        }
    }
}
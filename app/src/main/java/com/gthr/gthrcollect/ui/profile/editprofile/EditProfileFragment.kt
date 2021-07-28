package com.gthr.gthrcollect.ui.profile.editprofile

import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.EditProfileFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class EditProfileFragment : BaseFragment<EditProfileViewModel, EditProfileFragmentBinding>() {

    override val mViewModel: EditProfileViewModel by viewModels()
    override fun getViewBinding() = EditProfileFragmentBinding.inflate(layoutInflater)
    private lateinit var mTvEditProfilePicture : AppCompatTextView

    override fun onBinding() {
        initViews()
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        mTvEditProfilePicture.setOnClickListener {
            val sheet = ProfileImageBottomSheet(object : ProfileImageBottomSheet.ClickAction {
                override fun takePhoto() {

                }
                override fun chooseFromLibrary() {

                }
            })
            sheet.show(childFragmentManager,BOTTOM_SHEET_TAG)
        }
    }

    private fun initViews() {
        mViewBinding.let {
            mTvEditProfilePicture = it.tvEditProfilePicture
        }
    }

    companion object{
        const val TAKE_PICTURE  = 100
        const val BOTTOM_SHEET_TAG  = "EditProfileFragment"
    }
}
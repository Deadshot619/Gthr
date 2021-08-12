package com.gthr.gthrcollect.ui.profile.editprofile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.ProfileRepository
import com.gthr.gthrcollect.databinding.EditProfileFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.profile.MyProfileViewModelFactory
import com.gthr.gthrcollect.ui.profile.ProfileViewModel
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton
import com.gthr.gthrcollect.utils.customviews.CustomEditText
import com.gthr.gthrcollect.utils.extensions.showPermissionSnackBar
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.logger.GthrLogger
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException

class EditProfileFragment : BaseFragment<ProfileViewModel, EditProfileFragmentBinding>() {

    val REQUEST_IMAGE_CAPTURE = 1
    private val repository = ProfileRepository()
    private var latestTmpUri: Uri? = null
    lateinit var uri: Uri

    override val mViewModel: ProfileViewModel by viewModels {
        MyProfileViewModelFactory(
            repository
        )
    }

    override fun getViewBinding() = EditProfileFragmentBinding.inflate(layoutInflater)

    private lateinit var mProfileLayout: MaterialCardView
    private lateinit var mTvEditProfilePicture: AppCompatTextView
    private lateinit var mIvProfilePic: CircleImageView
    private lateinit var mEt_display_name: CustomEditText
    private lateinit var mEtBio: AppCompatEditText
    private lateinit var mSaveBtn: CustomAuthenticationButton

    private var fileName: String = ""

    override fun onBinding() {
        setHasOptionsMenu(true)
        initViews()
        setUpOnClickListeners()
        setUpProfile()
        setUpObservers()
    }

    private fun setUpObservers() {

        mViewModel.userCollectionInfoData.observe(viewLifecycleOwner, {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        showToast(getString(R.string.success_note))
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        })

        mViewModel.profileImageUpload.observe(viewLifecycleOwner, {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        GthrLogger.e("uploadTask", "BackFrag")
                        showToast(getString(R.string.success_note))
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
        mViewBinding.let {
            mProfileLayout = it.mcvProfile
            mTvEditProfilePicture = it.tvEditProfilePicture
            mIvProfilePic = it.ivProfilePic
            mEtBio = it.etBio
            mSaveBtn = it.btnSignIn
            mEt_display_name = it.etDisplayName
            mEt_display_name.mEtMain.isClickable = false
            mEt_display_name.mEtMain.isFocusable = false
            mEt_display_name.mEtMain.isFocusableInTouchMode = false

            initProgressBar(mViewBinding.layoutProgress)
        }
    }

    private fun setUpProfile() {
        mEt_display_name.mEtMain.setText(arguments?.getString(D_NAME_KEY))
        mEtBio.setText(arguments?.getString(ABOUT_KEY))
        Glide.with(this).load(arguments?.getString(PROFILE_URL)).into(mIvProfilePic)

    }

    private fun checkMultiplePermissions(
        permissions: Collection<String>,
        onPermissionGranted: () -> Unit
    ) {
        Dexter.withContext(requireContext())
            .withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0?.areAllPermissionsGranted() == true)
                        onPermissionGranted()

                    if (p0?.isAnyPermissionPermanentlyDenied == true)
                        context?.showPermissionSnackBar(
                            mViewBinding.root,
                            title = getString(R.string.permission_needed_text)
                        )
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            })
            .check()
    }

    private fun setUpOnClickListeners() {

        mProfileLayout.setOnClickListener {
            val sheet = ProfileImageBottomSheet(object : ProfileImageBottomSheet.ClickAction {
                override fun takePhoto() {
                    checkMultiplePermissions(takePicturePermissions) {
                        captureImage()
                    }
                }

                override fun chooseFromLibrary() {
                    checkMultiplePermissions(pickPicturePermissions) {
                        pickImage()
                    }
                }
            })
            sheet.show(childFragmentManager, BOTTOM_SHEET_TAG)
        }

        mSaveBtn.setOnClickListener {
            saveUserdata()
        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PICTURE_REQUEST)
    }

    fun captureImage() {
        val fileName = "new-photo-name.jpg"
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, fileName)
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera")
        uri = requireActivity().getContentResolver().insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )!!
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, TAKE_PICTURE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            TAKE_PICTURE_REQUEST -> if (resultCode == RESULT_OK) {

                try {
                    try {
                        Glide.with(this).load(uri).into(mIvProfilePic)
                        mViewModel.uploadProfileImage(uri)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        GthrLogger.e("Exception", e.message!!)
                        //    showToast(e.message!!)
                    }
                } catch (e: IllegalArgumentException) {
                    GthrLogger.e("Exception", e.message!!)
                    e.printStackTrace()
                    //  showToast(e.message!!)
                }
            }
            PICK_PICTURE_REQUEST -> if (resultCode == RESULT_OK) {

                try {
                    data?.data?.let { it ->
                        uri = data.data!!
                        Glide.with(this).load(uri).into(mIvProfilePic)
                        mViewModel.uploadProfileImage(uri)
                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

        }

    }

    fun saveUserdata() {


        val userdata = UserInfoDomainModel()
        userdata.bio = mEtBio.text.toString().trim()
        userdata.displayName = mEt_display_name.mEtMain.text.toString().trim()
        if (userdata.bio.isEmpty()) {
            mEtBio.setError(getString(R.string.required_field))
        } else if (userdata.displayName.isEmpty()) {
            mEt_display_name.mEtMain.setError((getString(R.string.required_field)))
        } else {
            mViewModel.addCollectionInfoModel(userdata)
        }
    }

    companion object {
        const val TAKE_PICTURE_REQUEST = 100
        const val PICK_PICTURE_REQUEST = 111
        const val BOTTOM_SHEET_TAG = "EditProfileFragment"
        const val IMAGE_NAME = "_image.jpg"
        const val DIR_NAME = "GTHR"
        const val PROVIDER = ".provider"
        const val IMAGE_CAPTURE_ACTION = "android.media.action.IMAGE_CAPTURE"
        const val D_NAME_KEY = "display_name"
        const val ABOUT_KEY = "about"
        const val PROFILE_URL = "image_URL"

        private val takePicturePermissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        private val pickPicturePermissions = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}
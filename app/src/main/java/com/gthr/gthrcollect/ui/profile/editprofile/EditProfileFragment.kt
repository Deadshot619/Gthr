package com.gthr.gthrcollect.ui.profile.editprofile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.BuildConfig
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.EditProfileFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.extensions.convertBitmapToFile
import com.gthr.gthrcollect.utils.extensions.getFile
import com.gthr.gthrcollect.utils.extensions.rotateImage
import com.gthr.gthrcollect.utils.extensions.showPermissionSnackBar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import de.hdodenhof.circleimageview.CircleImageView

class EditProfileFragment : BaseFragment<EditProfileViewModel, EditProfileFragmentBinding>() {

    override val mViewModel: EditProfileViewModel by viewModels()
    override fun getViewBinding() = EditProfileFragmentBinding.inflate(layoutInflater)

    private lateinit var mTvEditProfilePicture: AppCompatTextView
    private lateinit var mIvProfilePic: CircleImageView

    private var fileName : String = ""

    override fun onBinding() {
        initViews()
        setUpOnClickListeners()
    }

    private fun initViews() {
        mViewBinding.let {
            mTvEditProfilePicture = it.tvEditProfilePicture
            mIvProfilePic = it.ivProfilePic
        }
    }

    private fun checkMultiplePermissions(onPermissionGranted: () -> Unit) {
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
        mTvEditProfilePicture.setOnClickListener {
            val sheet = ProfileImageBottomSheet(object : ProfileImageBottomSheet.ClickAction {
                override fun takePhoto() {
                    checkMultiplePermissions {
                        openCamera()
                    }
                }
                override fun chooseFromLibrary() {}
            })
            sheet.show(childFragmentManager, BOTTOM_SHEET_TAG)
        }
    }

    private fun openCamera() {
        fileName = "${System.currentTimeMillis()}${IMAGE_NAME}"
        val photoFile = requireContext().getFile(fileName)
        photoFile?.let {
            val intent = Intent(IMAGE_CAPTURE_ACTION)
            val fileProvider = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + PROVIDER, it)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, TAKE_PICTURE_REQUEST)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PICTURE_REQUEST -> if (resultCode == RESULT_OK) {
                val rowFile = requireContext().getFile(fileName)
                rowFile?.let {
                    val bitmap = BitmapFactory.decodeFile(it.absolutePath).rotateImage(rowFile)
                    val file = bitmap.convertBitmapToFile(it)
                    mIvProfilePic.setImageBitmap(bitmap)
                }
            }
        }
    }


    companion object {
        const val TAKE_PICTURE_REQUEST = 100
        const val BOTTOM_SHEET_TAG = "EditProfileFragment"
        const val IMAGE_NAME = "_image.jpg"
        const val DIR_NAME = "GTHR"
        const val PROVIDER = ".provider"
        const val IMAGE_CAPTURE_ACTION = "android.media.action.IMAGE_CAPTURE"

        private val permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}
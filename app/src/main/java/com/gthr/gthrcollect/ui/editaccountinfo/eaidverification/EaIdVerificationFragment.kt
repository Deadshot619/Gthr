package com.gthr.gthrcollect.ui.editaccountinfo.eaidverification

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.EaIdVerificationFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.customcameraactivities.CustomCamera
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.CameraViews
import com.gthr.gthrcollect.utils.extensions.getBackgroundDrawable
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.showPermissionSnackBar
import com.gthr.gthrcollect.utils.extensions.visible
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class EaIdVerificationFragment :
    BaseFragment<EaIdVerificationViewModel, EaIdVerificationFragmentBinding>() {
    override val mViewModel: EaIdVerificationViewModel by viewModels()

    override fun getViewBinding() = EaIdVerificationFragmentBinding.inflate(layoutInflater)


    private lateinit var mIvFrontImage: AppCompatImageView
    private lateinit var mIvBackImage: AppCompatImageView
    private lateinit var mFrontIdCapture: MaterialCardView
    private lateinit var mBackIdCapture: MaterialCardView
    private lateinit var mSkipBtn: CustomSecondaryButton
    private lateinit var mCompleteAccBtn: CustomSecondaryButton

    lateinit var mfrontLable: TextView
    lateinit var mBackLable: TextView
    lateinit var mFront_repls: LinearLayout
    lateinit var mBack_repls: LinearLayout

    lateinit var mIdScanner: AppCompatImageView

    override fun onBinding() {
        initViews()
        addListeners()
    }

    private fun initViews() {
        mIvBackImage = mViewBinding.ivBackImage
        mIvFrontImage = mViewBinding.ivFrontImage
        mFrontIdCapture = mViewBinding.frontIdCapture
        mBackIdCapture = mViewBinding.backIdCapture
        mIdScanner = mViewBinding.ivIdScanner
        mSkipBtn = mViewBinding.btnSkipId
        mCompleteAccBtn = mViewBinding.btnCompleteAccount

        mfrontLable = mViewBinding.tvFrontLable
        mBackLable = mViewBinding.tvBackLable

        mFront_repls = mViewBinding.frontRepls
        mBack_repls = mViewBinding.backRepls

        mFront_repls.background = null
        mBack_repls.background = null

        Glide.with(this).load(R.drawable.id_scanner).into(mIdScanner)

        mIvFrontImage.gone()
        mIvBackImage.gone()
    }

    private fun addListeners() {
        mFrontIdCapture.setOnClickListener {
            checkMultiplePermissions {
                startActivityForResult(
                    CustomCamera.getInstance(
                        requireContext(),
                        CameraViews.ID_VERIFICATION,
                        isFront = true
                    ),
                    REQUEST_CODE_FRONT_ID
                )
            }
        }

        mBackIdCapture.setOnClickListener {
            checkMultiplePermissions {
                startActivityForResult(
                    CustomCamera.getInstance(
                        requireContext(),
                        CameraViews.ID_VERIFICATION,
                        isFront = false
                    ),
                    REQUEST_CODE_BACK_ID
                )
            }
        }

        mCompleteAccBtn.setOnClickListener {
            findNavController().navigate(EaIdVerificationFragmentDirections.actionEaIdVerificationFragmentToWelcomeFragment())
        }

        mSkipBtn.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.dialog_skip_verification_title))
                .setMessage(getString(R.string.dialog_skip_verification_subtitle))
                .setPositiveButton(getString(R.string.dialog_skip_verification_yes)) { dialog, _ ->
                    findNavController().navigate(EaIdVerificationFragmentDirections.actionEaIdVerificationFragmentToWelcomeFragment())
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.dialog_skip_verification_no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            val bitmap = BitmapFactory.decodeFile(data.getStringExtra(CustomCamera.INTENT_KEY_URL))

            if (requestCode == REQUEST_CODE_FRONT_ID) {
                mIvFrontImage.visible()
                mIvFrontImage.setImageBitmap(bitmap)

                mfrontLable.text = getString(R.string.replae_front)

                mFront_repls.background = getBackgroundDrawable(R.drawable.rectangle_5)
            } else if (requestCode == Companion.REQUEST_CODE_BACK_ID) {
                mIvBackImage.visible()
                mIvBackImage.setImageBitmap(bitmap)

                mBackLable.text = getString(R.string.replace_back)

                mCompleteAccBtn.setState(CustomSecondaryButton.State.YELLOW)

                mBack_repls.background = getBackgroundDrawable(R.drawable.rectangle_5)
            }
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

    companion object {
        private const val REQUEST_CODE_FRONT_ID = 1
        private const val REQUEST_CODE_BACK_ID = 2

        private val permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}
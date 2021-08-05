package com.gthr.gthrcollect.ui.editaccountinfo.eaidverification

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.EditAccountInfoRepository
import com.gthr.gthrcollect.databinding.EaIdVerificationFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.customcameraactivities.CustomCamera
import com.gthr.gthrcollect.ui.editaccountinfo.EditAccountInfoViewModel
import com.gthr.gthrcollect.ui.editaccountinfo.EditAccountInfoViewModelFactory
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.CameraViews
import com.gthr.gthrcollect.utils.extensions.*
import com.gthr.gthrcollect.utils.logger.GthrLogger
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class EaIdVerificationFragment :
    BaseFragment<EditAccountInfoViewModel, EaIdVerificationFragmentBinding>() {
    private val TAG: String = this.javaClass.name
    private val repository = EditAccountInfoRepository()
    private var imageCheck: Int = 0

    private var mFrontImageUrl: String? = null
    private var mBackImageUrl: String? = null

    override val mViewModel: EditAccountInfoViewModel by activityViewModels {
        EditAccountInfoViewModelFactory(repository)
    }

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

    val storage = Firebase.storage("gs://dlc-db-staging.appspot.com")
    var storageRef = storage.reference
    var spaceRef = storageRef.child("government_ID")


    override fun onBinding() {
        initViews()
        addListeners()
        setUpObservers()
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


        initProgressBar(mViewBinding.layoutProgress)
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
            mFrontImageUrl?.let { url ->
                mViewModel.uploadFrontImage(url, GthrCollect.prefs!!.signedInUser!!.uid)
            }
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

    private fun setUpObservers() {
        mViewModel.frontImageUpload.observe(viewLifecycleOwner, {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        GthrLogger.e("uploadTask", "FrontFrag")
                        mBackImageUrl?.let { url ->
                            mViewModel.uploadBackImage(url, GthrCollect.prefs!!.signedInUser!!.uid)
                        }
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        })

        mViewModel.backImageUpload.observe(viewLifecycleOwner, {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        GthrLogger.e("uploadTask", "BackFrag")
                        findNavController().navigate(EaIdVerificationFragmentDirections.actionEaIdVerificationFragmentToWelcomeFragment())
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            imageCheck += 1
            val bitmap = BitmapFactory.decodeFile(data.getStringExtra(CustomCamera.INTENT_KEY_URL))
            if (requestCode == REQUEST_CODE_FRONT_ID) {
                mIvFrontImage.visible()
                mIvFrontImage.setImageBitmap(bitmap)
                mfrontLable.text = getString(R.string.replae_front)
                mFront_repls.background = getBackgroundDrawable(R.drawable.rectangle_5)

                mFrontImageUrl = data.getStringExtra(CustomCamera.INTENT_KEY_URL)

                if (imageCheck >= 2) {
                    mCompleteAccBtn.setState(CustomSecondaryButton.State.YELLOW)
                }

            } else if (requestCode == Companion.REQUEST_CODE_BACK_ID) {
                mIvBackImage.visible()
                mIvBackImage.setImageBitmap(bitmap)
                mBackLable.text = getString(R.string.replace_back)

                mBackImageUrl = data.getStringExtra(CustomCamera.INTENT_KEY_URL)

                if (imageCheck >= 2) {
                    mCompleteAccBtn.setState(CustomSecondaryButton.State.YELLOW)
                }
                mBack_repls.background = getBackgroundDrawable(R.drawable.rectangle_5)
            }
        }
    }

    /*   private fun deleteImages() {
           GthrLogger.e("Ref", spaceRef.path)
           spaceRef.child("region_0708_162844190.jpg").delete().addOnSuccessListener {
               activity?.showToast("deleted successfully")
           }.addOnFailureListener {
               // Uh-oh, an error occurred!
               activity?.showToast("Uh-oh, an error occurred!")
           }


           spaceRef.delete().addOnSuccessListener(OnSuccessListener<Void?> { // File deleted successfully

                   GthrLogger.d(TAG, "onSuccess: deleted file")

               }).addOnFailureListener(OnFailureListener { // Uh-oh, an error occurred!
                   GthrLogger.d(TAG, "onFailure: did not delete file")
               })

    }
      */


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
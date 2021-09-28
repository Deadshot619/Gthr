package com.gthr.gthrcollect.ui.askflow.afaddpic

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.AfAddPicFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.customcameraactivities.CustomCardCamera
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.extensions.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class AfAddPicFragment : BaseFragment<AskFlowViewModel, AfAddPicFragmentBinding>() {

    private lateinit var mFrontIdCapture: MaterialCardView
    private lateinit var mBackIdCapture: MaterialCardView
    private lateinit var mIvFrontImage: AppCompatImageView
    private lateinit var mIvBackImage: AppCompatImageView
    lateinit var mFront_repls: LinearLayout
    lateinit var mBack_repls: LinearLayout

    lateinit var mSkipBtn: CustomSecondaryButton
    private var mIsNextBtnClicked : Boolean = false

    override fun getViewBinding() = AfAddPicFragmentBinding.inflate(layoutInflater)
    override val mViewModel: AskFlowViewModel by activityViewModels{
        AskFlowViewModelFactory(AskFlowRepository())
    }

    private lateinit var mIvBack: ImageView
    private lateinit var mBtnNext: CustomSecondaryButton

    private var mFrontImageUrl: String = ""
    private var mBackImageUrl: String = ""

    override fun onBinding() {

        initViews()
        addListeners()
        setUpObservers()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mIvBackImage = ivBackImage
            mIvFrontImage = ivFrontImage
            mIvBack = ivBack
            mFrontIdCapture = frontIdCapture
            mBackIdCapture = backIdCapture
            mFront_repls = frontRepls
            mBack_repls = backRepls
            mSkipBtn = btnSkipId
            mBtnNext = btnNext

            mIvFrontImage.gone()
            mIvBackImage.gone()
            initProgressBar(layoutProgress)

            if ((requireActivity() as AskFlowActivity).getAskFlowType() == AskFlowType.COLLECT)
                mBtnNext.text = getString(R.string.finish)

            //If this fragment is start destination, then hide back button
            if (findNavController().previousBackStackEntry == null)
                mIvBack.gone()
        }
    }

    private fun addListeners() {
        mFrontIdCapture.setOnClickListener {
            checkMultiplePermissions {

                // call when front btn is clicked and front image is empty
                if (mFrontImageUrl.isNullOrEmpty()) {
                    startActivityForResult(
                        CustomCardCamera.getInstance(
                            requireContext(),
                            isFront = true,
                            mFrontImageUrl,
                            mBackImageUrl,
                            getString(R.string.front_lalel)
                        ),
                        REQUEST_CODE_FRONT_ID
                    )
                    // call when front btn is clicked and front image is available
                } else {
                    startActivityForResult(
                        CustomCardCamera.getInstance(
                            requireContext(),
                            isFront = true,
                            mFrontImageUrl,
                            mBackImageUrl,
                            getString(R.string.front_lalel)
                        ),
                        REQUEST_CODE_PREVIEW
                    )

                }

            }
        }

        mBackIdCapture.setOnClickListener {
            checkMultiplePermissions {

                // call when back btn is clicked and back image is empty
                if (mBackImageUrl.isNullOrEmpty()) {
                    startActivityForResult(
                        CustomCardCamera.getInstance(
                            requireContext(),
                            isFront = false,
                            mFrontImageUrl,
                            mBackImageUrl,
                            getString(R.string.back_label)
                        ),
                        REQUEST_CODE_BACK_ID
                    )
                }
                // call when back btn is clicked and back image is available
                else {
                    startActivityForResult(
                        CustomCardCamera.getInstance(
                            requireContext(),
                            isFront = false,
                            mFrontImageUrl,
                            mBackImageUrl,
                            getString(R.string.back_label)
                        ),
                        REQUEST_CODE_PREVIEW
                    )
                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {

            val bitmap =
                BitmapFactory.decodeFile(data.getStringExtra(CustomCardCamera.INTENT_KEY_URL))
            when (requestCode) {

                // result when captured FRONT image FIRST
                REQUEST_CODE_FRONT_ID -> {

                    if (resultCode == Activity.RESULT_OK) {
                        mFrontImageUrl = data.getStringExtra(CustomCardCamera.INTENT_KEY_URL) ?: ""
                        mViewModel.setFrontImage(mFrontImageUrl)

                        // when front image is captured and capturing back image
                        if (mBackImageUrl.isNullOrEmpty()) {
                            startActivityForResult(
                                CustomCardCamera.getInstance(
                                    requireContext(),
                                    isFront = false,
                                    mFrontImageUrl,
                                    mBackImageUrl,
                                    getString(R.string.back_label)
                                ),
                                REQUEST_CODE_BACK_ID
                            )
                        }
                    }

                }
                //  result when captured BACK image FIRST
                REQUEST_CODE_BACK_ID -> {
                    if (resultCode == Activity.RESULT_OK) {
                        mBackImageUrl = data.getStringExtra(CustomCardCamera.INTENT_KEY_URL) ?: ""
                        mViewModel.setBackImage(mBackImageUrl)

                        // when back image is captured and capturing first image
                        if (mFrontImageUrl.isNullOrEmpty()) {

                            startActivityForResult(
                                CustomCardCamera.getInstance(
                                    requireContext(),
                                    isFront = true,
                                    mFrontImageUrl,
                                    mBackImageUrl,
                                    getString(R.string.front_lalel)
                                ),
                                REQUEST_CODE_FRONT_ID
                            )

                        }
                    }
                }

                // On either front or back image available and press either front or back btn
                REQUEST_CODE_PREVIEW -> {
                    if (resultCode == Activity.RESULT_OK) {

                        // Call for previewing the images
                        if (mFrontImageUrl.isNullOrEmpty() || mBackImageUrl.isNullOrEmpty()) {

                            // If back image available then call for front
                            if (mFrontImageUrl.isNullOrEmpty()) {
                                startActivityForResult(
                                    CustomCardCamera.getInstance(
                                        requireContext(),
                                        isFront = true,
                                        mFrontImageUrl,
                                        mBackImageUrl,
                                        getString(R.string.front_lalel)
                                    ),
                                    REQUEST_CODE_FRONT_ID
                                )

                                // If front image available then call for back
                            } else {
                                startActivityForResult(
                                    CustomCardCamera.getInstance(
                                        requireContext(),
                                        isFront = false,
                                        mFrontImageUrl,
                                        mBackImageUrl,
                                        getString(R.string.back_label)
                                    ),
                                    REQUEST_CODE_BACK_ID
                                )

                            }
                        }

                        // result when FRONT or BACK image is re-capturing
                        val mIsFrontView = data.getBooleanExtra(IS_FRONT, true)  // check which image is capturing

                        // if FRONT image is re-captured
                        if (mIsFrontView) {
                            mFrontImageUrl =
                                data.getStringExtra(CustomCardCamera.INTENT_KEY_URL) ?: ""
                            mViewModel.setFrontImage(mFrontImageUrl)
                        } else {
                            // if BACK image is re-captured
                            mBackImageUrl = data.getStringExtra(CustomCardCamera.INTENT_KEY_URL) ?: ""
                            mViewModel.setBackImage(mBackImageUrl)
                        }
                    }
                }
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

    private fun setUpClickListeners() {
        mViewBinding.run {
            mBtnNext.setOnClickListener {
                mIsNextBtnClicked = true
                if (mViewModel.isSell.value != true){
                    if (mViewModel.frontImageUrl.value != null && mViewModel.backImageUrl.value != null){
                        if (mViewModel.isEdit) {
                            mViewModel.setCollectionItemKey(mViewModel.productDisplayModel?.forsaleItemNodel?.collectionItemRefKey.toString())
                            mViewModel.uploadFrontImage()
                        } else
                            mViewModel.insertCollection()
                    }
                    else
                        showToast("Please add pictures!")
                }
                else {
                    if (mViewModel.frontImageUrl.value != null && mViewModel.backImageUrl.value != null)
                        findNavController().navigate(AfAddPicFragmentDirections.actionAfAddPicFragmentToAfReviewYourAskFragment())
                    else
                        showToast("Please add pictures!")
                }
            }
            mIvBack.setOnClickListener {
                findNavController().navigateUp()
            }
            mSkipBtn.setOnClickListener {
                mIsNextBtnClicked = false
                if (mViewModel.isEdit) {
                    activity?.finish()
                } else if (mViewModel.isSell.value != true) {
                    mViewModel.insertCollection()
                }
            }
        }
    }

    private fun setUpObservers() {
        mViewModel.insertCollectionRDB.observe(viewLifecycleOwner){
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        mViewModel.setCollectionItemKey(it.data)
                        if(mIsNextBtnClicked)
                            mViewModel.uploadFrontImage()
                        else
                            (activity as AskFlowActivity)?.finish()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.frontImageUpload.observe(viewLifecycleOwner){
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        mViewModel.setFrontImageDownloadUrl(it.data)
                        mViewModel.uploadBackImage()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.backImageUpload.observe(viewLifecycleOwner){
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        mViewModel.setBackImageDownloadUrl(it.data)
                        mViewModel.updateCollection()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.updateCollectionRDB.observe(viewLifecycleOwner){
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        (activity as AskFlowActivity)?.finish()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }


        mViewModel.isSell.observe(viewLifecycleOwner, {
            if (it) {
                mSkipBtn.gone()
                mBtnNext.text = getString(R.string.ask_sell)
            }
        })

        mViewModel.frontImageUrl.observe(this, {
            if (it != null) {
                mIvFrontImage.visible()
                mIvFrontImage.setImage(it)
                mFront_repls.gone()
            }
        })

        mViewModel.backImageUrl.observe(this, {
            if (it != null) {
                mIvBackImage.setImage(it)
                mIvBackImage.visible()
                mBack_repls.gone()
            }
        })
    }

    companion object {
        private const val REQUEST_CODE_FRONT_ID = 1
        private const val REQUEST_CODE_BACK_ID = 2
        private const val REQUEST_CODE_PREVIEW = 3
        private const val IS_FRONT = "is_front"

        private val permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}
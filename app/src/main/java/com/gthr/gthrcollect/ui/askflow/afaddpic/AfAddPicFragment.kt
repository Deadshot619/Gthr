package com.gthr.gthrcollect.ui.askflow.afaddpic

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AfAddPicFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.customcameraactivities.CustomCamera
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.enums.CameraViews
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.invisible
import com.gthr.gthrcollect.utils.extensions.showPermissionSnackBar
import com.gthr.gthrcollect.utils.extensions.visible
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class AfAddPicFragment : BaseFragment<AskFlowViewModel, AfAddPicFragmentBinding>() {

    private lateinit var mFrontIdCapture: MaterialCardView
    private lateinit var mBackIdCapture: MaterialCardView
    private var imageCheck: Int = 0
    private lateinit var mIvFrontImage: AppCompatImageView
    private lateinit var mIvBackImage: AppCompatImageView
    lateinit var mFront_repls: LinearLayout
    lateinit var mBack_repls: LinearLayout
    lateinit var mSkipBtn: CustomSecondaryButton
    lateinit var mAskSellBtn: CustomSecondaryButton


    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfAddPicFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvBack: ImageView
    private lateinit var mBtnNext: CustomSecondaryButton

    override fun onBinding() {

        initViews()
        addListeners()
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
//            mAskSellBtn = btnCompleteAccount

            mBtnNext = btnNext


            mIvFrontImage.gone()
            mIvBackImage.gone()

            if ((requireActivity() as AskFlowActivity).getAskFlowType() == AskFlowType.COLLECT)
                mBtnNext.text = getString(R.string.finish)
        }
    }

    private fun addListeners() {
        mFrontIdCapture.setOnClickListener {
            checkMultiplePermissions {
                startActivityForResult(
                    CustomCamera.getInstance(
                        requireContext(),
                        CameraViews.CARDS,
                        isFront = true,
                        getString(R.string.front_lalel)
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
                        CameraViews.CARDS,
                        isFront = false,
                    getString(R.string.back_label)
                    ),
                    REQUEST_CODE_BACK_ID
                )
            }
        }
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
                mFront_repls.gone()

               /* mfrontLable.text = getString(R.string.replae_front)
                mFront_repls.background = getBackgroundDrawable(R.drawable.rectangle_5)

                mFrontImageUrl = data.getStringExtra(CustomCamera.INTENT_KEY_URL)

                if (imageCheck >= 2) {
                    mCompleteAccBtn.setState(CustomSecondaryButton.State.YELLOW)
                }*/

            } else if (requestCode == REQUEST_CODE_BACK_ID) {
                mIvBackImage.visible()
                mIvBackImage.setImageBitmap(bitmap)
                mBack_repls.gone()
                mSkipBtn.invisible()
               /* mBackLable.text = getString(R.string.replace_back)

                mBackImageUrl = data.getStringExtra(CustomCamera.INTENT_KEY_URL)

                if (imageCheck >= 2) {
                    mCompleteAccBtn.setState(CustomSecondaryButton.State.YELLOW)
                }
                mBack_repls.background = getBackgroundDrawable(R.drawable.rectangle_5)*/
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



    private fun setUpClickListeners(){
        mViewBinding.run {
            mBtnNext.setOnClickListener {
                if ((requireActivity() as AskFlowActivity).getAskFlowType() == AskFlowType.COLLECT)
                    activity?.finish()
                else
                    findNavController().navigate(AfAddPicFragmentDirections.actionAfAddPicFragmentToAfReviewYourAskFragment())
            }
            mIvBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
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
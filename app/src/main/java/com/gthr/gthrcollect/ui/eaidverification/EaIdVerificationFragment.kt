package com.gthr.gthrcollect.ui.eaidverification

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
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.EaIdVerificationFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.customcameraactivities.CustomCamera
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.CameraViews
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

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



        addListners()
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

    private fun addListners() {
        mFrontIdCapture.setOnClickListener {
            startActivityForResult(
                CustomCamera.getInstance(requireContext())
                    .putExtra(CustomCamera.CAMERA_VIEW, CameraViews.ID_VERIFICATION.toString()),
                REQUEST_CODE_FRONT_ID
            )
        }


        mBackIdCapture.setOnClickListener {
            startActivityForResult(
                CustomCamera.getInstance(requireContext()).putExtra(
                    CustomCamera.CAMERA_VIEW, CameraViews.ID_VERIFICATION.toString()
                ), REQUEST_CODE_BACK_ID
            )
        }

        mCompleteAccBtn.setOnClickListener {
            findNavController().navigate(EaIdVerificationFragmentDirections.actionEaIdVerificationFragmentToWelcomeFragment())
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

                mFront_repls.background = resources.getDrawable(R.drawable.rectangle_5)


            } else {
                mIvBackImage.visible()
                mIvBackImage.setImageBitmap(bitmap)

                mBackLable.text = getString(R.string.replace_back)

                mSkipBtn.gone()
                mCompleteAccBtn.setState(CustomSecondaryButton.State.YELLOW)

                mBack_repls.background = resources.getDrawable(R.drawable.rectangle_5)
            }
        }


    }

    companion object {

        const val REQUEST_CODE_FRONT_ID = 1
        const val REQUEST_CODE_BACK_ID = 2


    }

}
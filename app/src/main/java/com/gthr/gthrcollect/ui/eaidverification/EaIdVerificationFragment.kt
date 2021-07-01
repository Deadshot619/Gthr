package com.gthr.gthrcollect.ui.eaidverification

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import com.google.android.material.card.MaterialCardView
import com.gthr.gthrcollect.databinding.EaIdVerificationFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.customcameraactivities.CustomCamera
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

    override fun onBinding() {

        initViews()



        addListners()
    }

    private fun initViews() {

        mIvBackImage = mViewBinding.ivBackImage
        mIvFrontImage = mViewBinding.ivFrontImage
        mFrontIdCapture = mViewBinding.frontIdCapture
        mBackIdCapture = mViewBinding.backIdCapture

        mIvFrontImage.gone()
        mIvBackImage.gone()
    }

    private fun addListners() {

        mFrontIdCapture.setOnClickListener(View.OnClickListener {
            startActivityForResult(
                CustomCamera.getInstance(requireContext()),
                REQUEST_CODE_FRONT_ID
            )
        })


        mBackIdCapture.setOnClickListener(View.OnClickListener {
            startActivityForResult(CustomCamera.getInstance(requireContext()), REQUEST_CODE_BACK_ID)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            val bitmap = BitmapFactory.decodeFile(data.getStringExtra(CustomCamera.INTENT_KEY_URL))

            if (requestCode == REQUEST_CODE_FRONT_ID) {

                mIvFrontImage.visible()
                mIvFrontImage.setImageBitmap(bitmap)
            } else {
                mIvBackImage.visible()
                mIvBackImage.setImageBitmap(bitmap)
            }
        }


    }

    companion object{

        const val REQUEST_CODE_FRONT_ID=1
        const val REQUEST_CODE_BACK_ID=2


    }

}
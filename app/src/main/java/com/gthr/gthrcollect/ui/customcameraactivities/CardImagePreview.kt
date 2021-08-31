package com.gthr.gthrcollect.ui.customcameraactivities

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.extensions.visible

class CardImagePreview : AppCompatActivity() {
    private lateinit var mCardImage: AppCompatImageView
    private lateinit var tv_label_msg: TextView

    lateinit var mRetake: CustomSecondaryButton
    lateinit var mSavePicture: CustomSecondaryButton
    var imgFile: String? = null

    var mCameraViews: String? = null
    var mLabelMsg: String? = null
    var mIsFrontView: Boolean = false
    var mUriFront: String? = null
    var mUriBack: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_image_preview)
        mCardImage = findViewById(R.id.iv_cardImage)
        mRetake = findViewById(R.id.retake)
        mSavePicture = findViewById(R.id.savePicture)
        tv_label_msg = findViewById(R.id.tv_label_msg)

        imgFile = intent.getStringExtra(CustomCardCamera.INTENT_KEY_URL)
        val bitmap = BitmapFactory.decodeFile(imgFile)
        mUriFront = intent.getStringExtra(CustomCardCamera.URI_FRONT)
        mUriBack = intent.getStringExtra(CustomCardCamera.URI_BACK)

        mIsFrontView = intent.getBooleanExtra(CustomCardCamera.IS_FRONT, false)
        mLabelMsg = intent.getStringExtra(CustomCardCamera.LABEL_MSG)

        // if comming from FRONT Side
        if (mIsFrontView) {
            tv_label_msg.text = getString(R.string.front_lalel)

            // if back image is empty
            if (mUriBack.isNullOrEmpty()) {
                mSavePicture.text = getString(R.string.back_label)
            } else {
                mSavePicture.text = getString(R.string.complete)
            }

        }
        // if comming from BACK Side
        else {
            tv_label_msg.text = getString(R.string.back_label)

            // if FRONT imsge is empty
            if (mUriFront.isNullOrEmpty()) {
                mSavePicture.text = getString(R.string.front_lalel)
            } else {
                mSavePicture.text = getString(R.string.complete)
            }
        }

        mCardImage.visible()
        mCardImage.setImageBitmap(bitmap)
        mRetake.setState(CustomSecondaryButton.State.WHITE_WITH_BLUE_BORDER)
        mSavePicture.setState(CustomSecondaryButton.State.BLUE)

        mRetake.setOnClickListener {
            finish()
        }


        mSavePicture.setOnClickListener(View.OnClickListener {

            val returnIntent = Intent()
            returnIntent.putExtra(IS_FRONT, mIsFrontView)
            returnIntent.putExtra(
                CustomCardCamera.INTENT_KEY_URL, imgFile
            )
            setResult(RESULT_OK, returnIntent)
            finish()

        })
    }

    companion object {

        const val URI_FRONT = "uri_front"
        const val URI_BACK = "uri_back"
        private const val IS_FRONT = "is_front"

        fun getInstance(
            context: Context,
            uriFront: String,
            uriBack: String,
            fileURL: String,
            cameraSelection: Boolean,
            label_msg: String?
        ) = Intent(context, CardImagePreview::class.java)
            .apply {
                putExtra(URI_FRONT, uriFront)
                putExtra(URI_BACK, uriBack)
                putExtra(CustomCardCamera.INTENT_KEY_URL, fileURL)
                    .putExtra(CustomCardCamera.IS_FRONT, cameraSelection)
                    .putExtra(CustomCardCamera.LABEL_MSG, label_msg)
            }
    }

    override fun onBackPressed() {
        val returnIntent = Intent()
        returnIntent.putExtra(
            CustomCardCamera.INTENT_KEY_URL, imgFile
        )
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}
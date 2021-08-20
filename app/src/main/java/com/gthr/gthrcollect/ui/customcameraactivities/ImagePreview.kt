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
import com.gthr.gthrcollect.utils.enums.CameraViews
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible
import com.gthr.gthrcollect.utils.logger.GthrLogger

class ImagePreview : AppCompatActivity() {
    lateinit var mIdImage: AppCompatImageView
    private lateinit var mCardImage:AppCompatImageView
    private lateinit var tv_label_msg:TextView




    lateinit var mRetake: CustomSecondaryButton
    lateinit var mSavePicture: CustomSecondaryButton
    var imgFile: String? = null

    var mCameraViews:String?=null;
    var mLabelMsg:String?=null;

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_preview)
        mIdImage = findViewById(R.id.iv_idImage)
        mCardImage=findViewById(R.id.iv_cardImage)
        mRetake = findViewById(R.id.retake)
        mSavePicture = findViewById(R.id.savePicture)
        tv_label_msg = findViewById(R.id.tv_label_msg)

        imgFile = intent.getStringExtra(CustomCamera.INTENT_KEY_URL)
        val bitmap = BitmapFactory.decodeFile(imgFile)



        mCameraViews=intent.getStringExtra(CustomCamera.CAMERA_VIEW)
        mLabelMsg=intent.getStringExtra(CustomCamera.LABEL_MSG)

        print(mCameraViews)
        GthrLogger.e("mCameraViews",mCameraViews.toString())

        if (mCameraViews!!.equals(CameraViews.ID_VERIFICATION.toString())){
           
            mIdImage.visible()
            mCardImage.gone()
            mIdImage.setImageBitmap(bitmap)
        }
        else{
            mIdImage.gone()
            mCardImage.visible()
            mCardImage.setImageBitmap(bitmap)
            mRetake.setState(CustomSecondaryButton.State.WHITE_WITH_BLUE_BORDER)
            mSavePicture.setState(CustomSecondaryButton.State.BLUE)

            tv_label_msg.text=mLabelMsg

        }
        
        
        mRetake.setOnClickListener(View.OnClickListener { finish() })


        mSavePicture.setOnClickListener(View.OnClickListener {


            val returnIntent = Intent()
            returnIntent.putExtra(CustomCamera.INTENT_KEY_URL
                , imgFile)
            setResult(RESULT_OK, returnIntent)
            finish()
        })
    }

    companion object{

        fun getInstance(context: Context, fileURL: String, camera_view: String, label_msg: String?) = Intent(context, ImagePreview::class.java)
            .apply{ putExtra(CustomCamera.INTENT_KEY_URL,fileURL).
            putExtra(CustomCamera.CAMERA_VIEW,camera_view).
            putExtra(CustomCamera.LABEL_MSG,label_msg) }

    }
}
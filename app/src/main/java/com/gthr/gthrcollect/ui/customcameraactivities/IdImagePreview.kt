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
import com.gthr.gthrcollect.ui.askflow.afaddpic.AfAddPicFragment
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.CameraViews
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible
import com.gthr.gthrcollect.utils.logger.GthrLogger

class IdImagePreview : AppCompatActivity() {
    lateinit var mIdImage: AppCompatImageView
    private lateinit var tv_label_msg:TextView

    lateinit var mRetake: CustomSecondaryButton
    lateinit var mSavePicture: CustomSecondaryButton
    var imgFile: String? = null

    var mCameraViews:String?=null
    var mLabelMsg:String?=null
    var mIsFrontView: Boolean = false


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_preview)
        mIdImage = findViewById(R.id.iv_idImage)
        mRetake = findViewById(R.id.retake)
        mSavePicture = findViewById(R.id.savePicture)
        tv_label_msg = findViewById(R.id.tv_label_msg)

        imgFile = intent.getStringExtra(CustomIdCamera.INTENT_KEY_URL)
        val bitmap = BitmapFactory.decodeFile(imgFile)



        mCameraViews=intent.getStringExtra(CustomIdCamera.CAMERA_VIEW)
        mIsFrontView=intent.getBooleanExtra(CustomIdCamera.IS_FRONT,false)
        mLabelMsg=intent.getStringExtra(CustomIdCamera.LABEL_MSG)


        if (mCameraViews!!.equals(CameraViews.ID_VERIFICATION.toString())){
           
            mIdImage.visible()
            mIdImage.setImageBitmap(bitmap)
        }

        mRetake.setOnClickListener{ finish() }


        mSavePicture.setOnClickListener(View.OnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(CustomIdCamera.INTENT_KEY_URL
                , imgFile)
            setResult(RESULT_OK, returnIntent)
            finish()
        })
    }

    companion object{

        fun getInstance(context: Context, fileURL: String, camera_view: String, cameraSelection: Boolean, label_msg: String?) = Intent(context, IdImagePreview::class.java)
            .apply{ putExtra(CustomIdCamera.INTENT_KEY_URL,fileURL).
            putExtra(CustomIdCamera.CAMERA_VIEW,camera_view).
            putExtra(CustomIdCamera.IS_FRONT,cameraSelection).
            putExtra(CustomIdCamera.LABEL_MSG,label_msg) }

    }
}
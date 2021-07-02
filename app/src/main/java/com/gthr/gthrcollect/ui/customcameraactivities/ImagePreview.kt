package com.gthr.gthrcollect.ui.customcameraactivities

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.enums.CameraViews
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class ImagePreview : AppCompatActivity() {
    lateinit var mIdImage: AppCompatImageView
    private lateinit var mCardImage:AppCompatImageView

    lateinit var mRetake: TextView
    lateinit var mSavePicture: TextView
    var imgFile: String? = null

    var mCameraViews:String?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_preview)
        mIdImage = findViewById(R.id.iv_idImage)
        mCardImage=findViewById(R.id.iv_cardImage)
        mRetake = findViewById(R.id.retake)
        mSavePicture = findViewById(R.id.savePicture)

        imgFile = intent.getStringExtra(CustomCamera.INTENT_KEY_URL)
        val bitmap = BitmapFactory.decodeFile(imgFile)



        mCameraViews=intent.getStringExtra(CustomCamera.CAMERA_VIEW)

        if (mCameraViews!!.equals(CameraViews.ID_VERIFICATION.toString())){
           
            mIdImage.visible()
            mCardImage.gone()
            mIdImage.setImageBitmap(bitmap)
        }
        else{
            mIdImage.gone()
            mCardImage.visible()
            mCardImage.setImageBitmap(bitmap)
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

        fun getInstance(context: Context, fileURL:String, camera_view:String) = Intent(context, ImagePreview::class.java)
            .apply{ putExtra(CustomCamera.INTENT_KEY_URL,fileURL).putExtra(CustomCamera.CAMERA_VIEW,camera_view) }

    }
}
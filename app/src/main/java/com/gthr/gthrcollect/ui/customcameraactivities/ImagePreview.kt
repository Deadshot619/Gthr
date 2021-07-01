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

class ImagePreview : AppCompatActivity() {
    lateinit var mImageView: AppCompatImageView

    lateinit var mRetake: TextView
    lateinit var mSavePicture: TextView
    var imgFile: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_preview)
        mImageView = findViewById(R.id.imageView)
        mRetake = findViewById(R.id.retake)
        mSavePicture = findViewById(R.id.savePicture)

        imgFile = intent.getStringExtra(CustomCamera.INTENT_KEY_URL)
        val bitmap = BitmapFactory.decodeFile(imgFile)


        mImageView.setImageBitmap(bitmap)


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

        fun getInstance(context: Context, fileURL:String) = Intent(context, ImagePreview::class.java)
            .apply{ putExtra(CustomCamera.INTENT_KEY_URL,fileURL) }

    }
}
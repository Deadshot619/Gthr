package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable

class CustomImageTextButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val view: View =
        LayoutInflater.from(context).inflate(R.layout.layout_custom_image_text_button, this, true)
    private var imageSrc: Int = -1
    private var btnTitle: String = ""

    lateinit var mIvStart: AppCompatImageView
    lateinit var mTvTitle: TextView

    init {
        initViews(attrs)
        setBtnTitle(btnTitle)
        setBtnStartImage(imageSrc)
    }

    private fun initViews(attrs: AttributeSet?) {
        val tempAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomImageTextButton)

        btnTitle = tempAttrs.getString(R.styleable.CustomImageTextButton_citb_btn_title) ?: ""
        imageSrc = tempAttrs.getResourceId(R.styleable.CustomImageTextButton_citb_src, -1)

        mIvStart = view.findViewById(R.id.iv_start)
        mTvTitle = view.findViewById(R.id.tv_title)

        tempAttrs.recycle()
    }

    fun setBtnTitle(title: String) {
        mTvTitle.text = title
    }

    fun setBtnStartImage(@DrawableRes res: Int) {
        mIvStart.setImageDrawable(getImageDrawable(res))
    }

}
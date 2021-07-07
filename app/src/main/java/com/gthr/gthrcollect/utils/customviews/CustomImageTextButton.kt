package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class CustomImageTextButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val view: View =
        LayoutInflater.from(context).inflate(R.layout.layout_custom_image_text_button, this, true)
    private var imageSrc: Int = -1
    private var btnTitle: String = ""
    private var btnType: Int = 0

    lateinit var mMainLayout: ConstraintLayout
    lateinit var mIvStart: AppCompatImageView
    lateinit var mIvEnd: AppCompatImageView
    lateinit var mTvTitle: TextView

    init {
        initViews(attrs)
        setBtnTitle(btnTitle)
        setBtnType(btnType)
    }

    private fun initViews(attrs: AttributeSet?) {
        val tempAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomImageTextButton)

        btnTitle = tempAttrs.getString(R.styleable.CustomImageTextButton_citb_btn_title) ?: ""
        imageSrc = tempAttrs.getResourceId(R.styleable.CustomImageTextButton_citb_src, -1)
        btnType = tempAttrs.getInt(R.styleable.CustomImageTextButton_citb_btn_type, 0)

        mMainLayout = view.findViewById(R.id.cl_main_layout)
        mIvStart = view.findViewById(R.id.iv_start)
        mIvEnd = view.findViewById(R.id.iv_end)
        mTvTitle = view.findViewById(R.id.tv_title)

        tempAttrs.recycle()
    }

    fun setBtnTitle(title: String) {
        mTvTitle.text = title
    }

    fun setBtnStartImage(@DrawableRes res: Int) {
        mIvStart.setImageDrawable(getImageDrawable(res))
    }

    fun setBtnType(value: Int) {
        when (value) {
            0 -> {  //Image button
                mIvStart.visible()
                mMainLayout.background = getImageDrawable(R.drawable.blue_bg_outline)
                mIvEnd.setImageDrawable(getImageDrawable(R.drawable.ic_right_arrow_blue))
                setBtnStartImage(imageSrc)
                mTvTitle.typeface = Typeface.DEFAULT_BOLD
            }
            1 -> {  //No Image button, Grey
                mIvStart.gone()
                mMainLayout.background = getImageDrawable(R.drawable.grey_bg_outline)
                mIvEnd.setImageDrawable(getImageDrawable(R.drawable.ic_right_arrow_grey))
                mTvTitle.typeface = Typeface.DEFAULT
            }
        }
    }

}
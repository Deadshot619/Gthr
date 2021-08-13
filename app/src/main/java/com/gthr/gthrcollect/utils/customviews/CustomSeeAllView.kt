package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gthr.gthrcollect.R

class CustomSeeAllView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr)   {

    private val mView: View = LayoutInflater.from(context).inflate(R.layout.layout_custom_see_all, this, true)
    private val mIvMain: AppCompatImageView = mView.findViewById(R.id.iv_see_all)
    private val mTvTitle: AppCompatTextView = mView.findViewById(R.id.tv_see_all)

}
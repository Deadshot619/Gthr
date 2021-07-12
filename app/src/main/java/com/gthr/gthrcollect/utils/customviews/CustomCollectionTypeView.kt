package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.getResolvedColor
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class CustomCollectionTypeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val mView: View = LayoutInflater.from(context).inflate(R.layout.layout_custom_collection_type, this, true)
    private val mTvTitle: AppCompatTextView = mView.findViewById(R.id.tv_title)
    private val mVUpdate: View = mView.findViewById(R.id.v_update)
    var mIsActive: Boolean = false
        private set
    var mIsUpdate: Boolean = false
        private set

    init {
        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCollectionTypeView)
        attrs.getString(R.styleable.CustomCollectionTypeView_cccv_text)?.let { mTvTitle.text = it }
        mIsActive = attrs.getBoolean(R.styleable.CustomCollectionTypeView_ccv_isActive, false)
        mIsUpdate = attrs.getBoolean(R.styleable.CustomCollectionTypeView_ccv_isUpdate, false)
        setActive(mIsActive)
        setUpdate(mIsUpdate)
        attrs.recycle()
    }

    fun setActive(isActive: Boolean) {
        mIsActive = isActive
        if (isActive) {
            mTvTitle.background = getImageDrawable(R.drawable.bg_custom_collection_type_blue)
            mTvTitle.setTextColor(getResolvedColor(R.color.white))
            mTvTitle.setTypeface(mTvTitle.typeface, Typeface.BOLD)
        } else {
            mTvTitle.background = getImageDrawable(R.drawable.bg_custom_collection_type_gray)
            mTvTitle.setTextColor(getResolvedColor(R.color.black))
            mTvTitle.typeface = Typeface.DEFAULT
        }
    }

    fun setUpdate(isActive: Boolean) {
        mIsUpdate = isActive
        if (isActive) mVUpdate.visible() else mVUpdate.gone()
    }




}
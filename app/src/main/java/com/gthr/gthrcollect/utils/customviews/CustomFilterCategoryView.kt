package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable

class CustomFilterCategoryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr)  {

    var mIsActive: Boolean = false
        private set

    init{
        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFilterCategoryView)
        mIsActive = attrs.getBoolean(R.styleable.CustomFilterCategoryView_cfcv_isActive, false)

        this.textAlignment = View.TEXT_ALIGNMENT_CENTER
        val padding = resources.getDimensionPixelOffset(R.dimen.padding_large)
        val paddingVertical = resources.getDimensionPixelOffset(R.dimen.padding_extra_small)
        this.setPadding(padding, paddingVertical, padding, paddingVertical)
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_size_small))

        setActive(mIsActive)
    }

    fun setActive(isSelected: Boolean) {
        mIsActive = isSelected
        if (isSelected) {
            this.background = getImageDrawable(R.drawable.bg_filter_light_blue_gradient)
        } else {
            this.background = getImageDrawable(R.drawable.bg_filter_gray)
        }
    }
}
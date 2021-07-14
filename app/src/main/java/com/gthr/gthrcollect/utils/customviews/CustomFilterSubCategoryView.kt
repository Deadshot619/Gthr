package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.gthr.gthrcollect.R

class CustomFilterSubCategoryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr)   {

    var mIsActive: Boolean = false
        private set

    init{
        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFilterSubCategoryView)
        mIsActive = attrs.getBoolean(R.styleable.CustomFilterSubCategoryView_cfscv_isActive, false)

        this.textAlignment = View.TEXT_ALIGNMENT_CENTER
        val paddingVertical = resources.getDimensionPixelOffset(R.dimen.padding_extra_small)
        this.setPadding(0, paddingVertical, 0, paddingVertical)
        this.compoundDrawablePadding = paddingVertical

        setActive(mIsActive)
    }

    fun setActive(isSelected: Boolean) {
        mIsActive = isSelected
        if (isSelected) {
            this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_selected, 0, 0, 0)
        } else {
            this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unselected, 0, 0, 0)
        }
    }
}
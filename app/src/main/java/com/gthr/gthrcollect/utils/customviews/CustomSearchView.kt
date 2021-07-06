package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.getResolvedColor

class CustomSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init{

        this.background = getImageDrawable(R.drawable.bg_search_edit_text_gray)
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        this.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        val padding = resources.getDimensionPixelOffset(R.dimen.padding_normal)
        val paddingVertical = resources.getDimensionPixelOffset(R.dimen.padding_medium)
        this.setPadding(padding, paddingVertical, padding, paddingVertical)
        this.setHintTextColor(getResolvedColor(R.color.hint_color))
        this.compoundDrawablePadding = paddingVertical
        this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)

    }

}
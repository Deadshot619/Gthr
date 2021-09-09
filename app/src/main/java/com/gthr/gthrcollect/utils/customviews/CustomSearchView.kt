package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.getResolvedColor

class CustomSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init{
        setSearchBackground(true)
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        this.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        val padding = resources.getDimensionPixelOffset(R.dimen.padding_normal)
        val paddingVertical = resources.getDimensionPixelOffset(R.dimen.margin_vertical_search)
        this.setPadding(padding, paddingVertical, padding, paddingVertical)
        this.setHintTextColor(getResolvedColor(R.color.hint_color))
        this.compoundDrawablePadding = paddingVertical

        setTextChangeListener {}
    }

    fun setSearchBackground(isEmpty: Boolean) {
        if (isEmpty) {
            this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
            this.background = getImageDrawable(R.drawable.bg_search_edit_text_gray)
        } else {
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            this.background = getImageDrawable(R.drawable.bg_search_edit_text_blue)
        }
    }

    fun setTextChangeListener(search: (String) -> Unit) {
        this.afterTextChanged {
            if (it.isNotEmpty()) {
                setSearchBackground(false)
            } else {
                setSearchBackground(true)
            }
            search(it)
        }
    }
}
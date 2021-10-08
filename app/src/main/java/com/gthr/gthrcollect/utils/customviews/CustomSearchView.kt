package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
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

    private var isEmpty = false

    init {
        setSearchBackground(true)
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        this.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        val padding = resources.getDimensionPixelOffset(R.dimen.padding_normal)
        val paddingVertical = resources.getDimensionPixelOffset(R.dimen.margin_vertical_search)
        this.setPadding(padding, paddingVertical, padding, paddingVertical)
        this.setHintTextColor(getResolvedColor(R.color.hint_color))
        this.compoundDrawablePadding = paddingVertical
        this.maxLines = 1
        this.isSingleLine = true

        setTextChangeListener {}
        onCancelListener()
    }

    private fun setSearchBackground(isEmpty: Boolean) {
        if (this.isEmpty == isEmpty)
            return

        this.isEmpty = isEmpty
        if (this.isEmpty) {
            this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
            this.background = getImageDrawable(R.drawable.bg_search_edit_text_gray)
        } else {
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round_close, 0)
            this.background = getImageDrawable(R.drawable.bg_search_edit_text_blue)
           // this.setPadding(20,20,20,20)
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

    private fun onCancelListener() {
        this.setOnTouchListener(OnTouchListener { v, event ->
            try {

//            val DRAWABLE_LEFT = 0
//            val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
//            val DRAWABLE_BOTTOM = 3

                if (event.action == MotionEvent.ACTION_UP)
                    if (event.rawX >= this.right - this.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        this.setText("")
                        return@OnTouchListener true
                    }
                false
            } catch (e: Exception) {
                false
            }
        })
    }
}
package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.getResolvedColor


@RequiresApi(Build.VERSION_CODES.M)
class CustomProductButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomProductButton)
        val state = State.values()[attrs.getInt(
            R.styleable.CustomProductButton_custom_product_button_state,
            1
        )]

        this.textAlignment = View.TEXT_ALIGNMENT_CENTER
        val padding = resources.getDimensionPixelOffset(R.dimen.padding_large)
        val paddingVertical = resources.getDimensionPixelOffset(R.dimen.padding_small)
        this.setPadding(padding, paddingVertical, padding, paddingVertical)
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelOffset(R.dimen.text_size_normal).toFloat())
        setState(state)
        attrs.recycle()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun setState(state: State) {
        this.background = when (state) {
            State.OWN -> {
                this.isEnabled = true
                this.setTextColor(getResolvedColor(R.color.white))
                getImageDrawable(R.drawable.bg_btn_product_yellow)
            }
            State.BUY -> {
                this.isEnabled = true
                this.setTextColor(getResolvedColor(R.color.white))
                getImageDrawable(R.drawable.bg_btn_product_green)
            }
            State.SELL -> {
                this.isEnabled = true
                this.setTextColor(getResolvedColor(R.color.white))
                getImageDrawable(R.drawable.bg_btn_product_red)
            }
        }
    }

    enum class State {
        BUY,OWN,SELL
    }
}
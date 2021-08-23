package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.getResolvedColor


@RequiresApi(Build.VERSION_CODES.M)
class CustomSecondaryButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSecondaryButton)
        val state = State.values()[attrs.getInt(
            R.styleable.CustomSecondaryButton_secondary_button_state,
            1
        )]

        this.textAlignment = View.TEXT_ALIGNMENT_CENTER
        val padding = resources.getDimensionPixelOffset(R.dimen.padding_large)
        val paddingVertical =
            resources.getDimensionPixelOffset(R.dimen.custom_secondary_button_padding_vertical)
        this.setPadding(padding, paddingVertical, padding, paddingVertical)

        setState(state)
        attrs.recycle()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setState(state: State) {
        this.background = when (state) {
            State.DISABLE -> {
                this.isEnabled = false
                this.setTextColor(getResolvedColor(R.color.white))
                this.compoundDrawableTintList = ColorStateList.valueOf(getResolvedColor(R.color.white))
                getImageDrawable(R.drawable.bg_btn_secondary_gray)
            }
            State.BLUE -> {
                this.isEnabled = true
                this.setTextColor(getResolvedColor(R.color.white))
                this.compoundDrawableTintList = ColorStateList.valueOf(getResolvedColor(R.color.white))
                getImageDrawable(R.drawable.bg_btn_secondary_blue)
            }
            State.YELLOW -> {
                this.isEnabled = true
                this.setTextColor(getResolvedColor(R.color.white))
                this.compoundDrawableTintList = ColorStateList.valueOf(getResolvedColor(R.color.white))
                getImageDrawable(R.drawable.bg_btn_secondary_yellow)
            }
            State.GREEN -> {
                this.isEnabled = true
                this.compoundDrawableTintList = ColorStateList.valueOf(getResolvedColor(R.color.white))
                this.setTextColor(getResolvedColor(R.color.white))
                getImageDrawable(R.drawable.bg_btn_secondary_green)
            }
            State.WHITE_WITH_BLUE_BORDER -> {
                this.isEnabled = true
                this.setTextColor(getResolvedColor(R.color.light_blue))
                this.compoundDrawableTintList = ColorStateList.valueOf(getResolvedColor(R.color.light_blue))
                getImageDrawable(R.drawable.bg_btn_secondary_white_with_blue_border)
            }
            State.BLACK_WITH_BLUE_BORDER -> {
                this.isEnabled = true
                this.setTextColor(getResolvedColor(R.color.light_blue))
                this.compoundDrawableTintList = ColorStateList.valueOf(getResolvedColor(R.color.light_blue))
                getImageDrawable(R.drawable.bg_btn_secondary_black_with_blue_border)
            }
            State.BLUE_GRADIENT -> {
                this.isEnabled = true
                this.setTextColor(getResolvedColor(R.color.white))
                this.compoundDrawableTintList = ColorStateList.valueOf(getResolvedColor(R.color.white))
                getImageDrawable(R.drawable.bg_btn_secondary_blue_gradint)
            }
        }
    }

    enum class State {
        DISABLE, BLUE, YELLOW, GREEN, WHITE_WITH_BLUE_BORDER, BLACK_WITH_BLUE_BORDER,BLUE_GRADIENT
    }
}
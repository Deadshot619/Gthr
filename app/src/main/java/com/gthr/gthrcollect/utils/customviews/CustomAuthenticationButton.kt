package com.gthr.gthrcollect.utils.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.setPadding
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.getResolvedColor


@SuppressLint("Recycle")
class CustomAuthenticationButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        val attrs = context.obtainStyledAttributes(attrs, R.styleable.CustomAuthenticationButton)

        attrs.getBoolean(R.styleable.CustomAuthenticationButton_auth_btn_enable, true)?.let {
            isEnabled = it
            setBackGround()
        }

        this.setTextColor(getResolvedColor(R.color.white))
        this.textAlignment = View.TEXT_ALIGNMENT_CENTER
        this.setTypeface(typeface, Typeface.BOLD)
        val padding = resources.getDimensionPixelOffset(R.dimen.padding_normal)
        this.setPadding(padding)

        attrs.recycle()
    }

    fun enableAuthButton() {
        isEnabled = true
        setBackGround()
    }

    fun disableAuthButton() {
        isEnabled = false
        setBackGround()
    }

    private fun setBackGround() {
        this.background = if (isEnabled) {
            getImageDrawable(R.drawable.bg_btn_gradient)
        } else {
            getImageDrawable(R.drawable.bg_btn_disable)
        }
    }
}
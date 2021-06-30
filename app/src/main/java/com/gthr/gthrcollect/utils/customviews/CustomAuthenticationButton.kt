package com.gthr.gthrcollect.utils.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.LayoutCustomAuthenticationButtonBinding
import com.gthr.gthrcollect.utils.extensions.getImageDrawable


@SuppressLint("Recycle")
class CustomAuthenticationButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var mBinding: LayoutCustomAuthenticationButtonBinding =
        LayoutCustomAuthenticationButtonBinding.inflate(LayoutInflater.from(context))

    var isEnabledBtn = false
        private set

    init {
        addView(mBinding.root)

        val attrs = context.obtainStyledAttributes(attrs, R.styleable.CustomAuthenticationButton)

        attrs.getString(R.styleable.CustomAuthenticationButton_auth_btn_text)
            ?.let { mBinding.btnMain.text = it }
        attrs.getBoolean(R.styleable.CustomAuthenticationButton_auth_btn_enable, true)?.let {
            isEnabledBtn = it
            setBackGround()
        }

    }

    fun setAuthBtnText(text: String) {
        mBinding.btnMain.text = text
    }

    fun setAuthBtnEnable() {
        isEnabledBtn = true
        setBackGround()
    }

    fun setAuthBtnDisable() {
        isEnabledBtn = false
        setBackGround()
    }


    private fun setBackGround() {

        mBinding.btnMain.background = if (isEnabledBtn)
            getImageDrawable(R.drawable.bg_btn_gradient)
        else
            getImageDrawable(R.drawable.bg_btn_disable)

    }

}
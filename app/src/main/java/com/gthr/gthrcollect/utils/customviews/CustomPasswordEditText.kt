package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.textfield.TextInputLayout
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable

class CustomPasswordEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {

    private var mLlPassword: LinearLayout
    private var mTilPassword: TextInputLayout
    var mIvPassword: AppCompatImageView
        private set
    var mEtPassword: AppCompatEditText
        private set
    private val view: View =
        LayoutInflater.from(context)
            .inflate(R.layout.layout_custom_password_edit_text, this, true)

    var isErrorEnabled = false
        private set

    init {
        mLlPassword = view.findViewById(R.id.ll_password)
        mIvPassword = view.findViewById(R.id.iv_password)
        mEtPassword = view.findViewById(R.id.et_Password)
        mTilPassword = view.findViewById(R.id.til_password)
    }

    fun showError(errorMsg: String) {
        isErrorEnabled = true
        mIvPassword.background = getImageDrawable(R.drawable.edit_text_image_error_bg)
        mLlPassword.background = getImageDrawable(R.drawable.edit_text_password_error_outline_bg)
        mTilPassword.endIconMode = TextInputLayout.END_ICON_NONE
        mEtPassword.error = errorMsg
    }

    fun hideError() {
        isErrorEnabled = false
        mIvPassword.background = getImageDrawable(R.drawable.edit_text_image_bg)
        mLlPassword.background = getImageDrawable(R.drawable.edit_text_password_blue_outline_bg)
        mTilPassword.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        mEtPassword.error = null
    }

}
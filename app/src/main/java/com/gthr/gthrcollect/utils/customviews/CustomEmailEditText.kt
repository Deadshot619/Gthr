package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable

class CustomEmailEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mMainLayout: LinearLayout
    var mIvEmail: AppCompatImageView
        private set
    var mEtEmail: AppCompatEditText
        private set
    private val view: View =
        LayoutInflater.from(context)
            .inflate(R.layout.layout_custom_email_edit_text, this, true)

    var isErrorEnabled = false
        private set

    init {
        mMainLayout = view.findViewById(R.id.ll_main_layout)
        mIvEmail = view.findViewById(R.id.iv_email)
        mEtEmail = view.findViewById(R.id.et_email)
        setCustomTextChangeListener()
    }

    fun showError(errorMsg: String) {
        isErrorEnabled = true
        mIvEmail.background = getImageDrawable(R.drawable.edit_text_image_error_bg)
        mEtEmail.background = getImageDrawable(R.drawable.edit_text_error_outline_bg)
        mEtEmail.error = errorMsg
    }

    fun hideError() {
        isErrorEnabled = false
        mIvEmail.background = getImageDrawable(R.drawable.edit_text_image_bg)
        mEtEmail.background = getImageDrawable(R.drawable.edit_text_blue_outline_bg)
        mEtEmail.error = null
    }

    private fun setCustomTextChangeListener() {
        mEtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isErrorEnabled)
                    hideError()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
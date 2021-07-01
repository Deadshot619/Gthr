package com.gthr.gthrcollect.utils.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

@SuppressLint("Recycle")
class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mLlMain: LinearLayout
    private var mEtMain: AppCompatEditText
        private set
    private var mIvMain: AppCompatImageView
        private set

    private var mInitialState: ColorState
    private var mErrorState: ColorState
    private var mSuccessState: ColorState
    private var mCurrentState: CurrentState = CurrentState.INITIAL


    var mErrorText: String = ""


    private var mImageFlag = false


    private val view: View =
        LayoutInflater.from(context).inflate(R.layout.layout_custom_edit_text, this, true)

    init {
        mLlMain = view.findViewById(R.id.ll_main)
        mEtMain = view.findViewById(R.id.et_main)
        mIvMain = view.findViewById(R.id.iv_main)

        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomEditText)

        attrs.getString(R.styleable.CustomPhoneNoEditText_phone_no_hint)?.let { mEtMain.hint = it }
        attrs.getString(R.styleable.CustomPhoneNoEditText_phone_no_error_text)
            ?.let { mErrorText = it }
        attrs.getResourceId(R.styleable.CustomEditText_android_src, -1).let {
            if (it != -1) {
                mImageFlag = true
                mIvMain.visible()
                mIvMain.setImageResource(it)
            }
        }

        mInitialState =
            ColorState.values()[attrs.getInt(R.styleable.CustomEditText_edit_initial_state, 0)]
        mErrorState = ColorState.values()[attrs.getInt(R.styleable.CustomEditText_edit_error_state, 2)]
        mSuccessState =
            ColorState.values()[attrs.getInt(R.styleable.CustomEditText_edit_success_state, 1)]



        setCurrentState(CurrentState.INITIAL)


    }

    private fun setCurrentState(state: CurrentState) {
        mCurrentState = state
        when (state) {
            CurrentState.INITIAL -> setInitial()
            CurrentState.SUCCESS -> setSuccess()
            CurrentState.ERROR -> setError(mErrorText)
        }
    }

    private fun setState(state: ColorState) {
        mLlMain.background = when (state) {
            ColorState.GRAY -> {
                mIvMain.setColorFilter(ContextCompat.getColor(context, R.color.background))
                getImageDrawable(R.drawable.edit_text_gray_bg)
            }
            ColorState.GREEN -> {
                mIvMain.setColorFilter(ContextCompat.getColor(context, R.color.green))
                getImageDrawable(R.drawable.edit_text_green_outline_bg)
            }
            ColorState.BLUE -> {
                mIvMain.setColorFilter(ContextCompat.getColor(context, R.color.blue))
                getImageDrawable(R.drawable.edit_text_blue_bg_outline)
            }
            ColorState.RED -> {
                mIvMain.setColorFilter(ContextCompat.getColor(context, R.color.red))
                getImageDrawable(R.drawable.edit_text_red_outline_bg)
            }
        }
    }

    fun setError(text: String) {
        mIvMain.gone()
        mEtMain.error = text
        setState(mErrorState)
    }

    fun setSuccess() {
        if (mImageFlag) mIvMain.visible() else mIvMain.gone()
        mEtMain.error = null
        setState(mSuccessState)
    }

    fun setInitial() {
        mIvMain.gone()
        mEtMain.error = null
        setState(mInitialState)
    }


    fun setEditTextStates(
        mInitialState: ColorState = ColorState.GRAY,
        mSuccessState: ColorState = ColorState.GRAY,
        mErrorState: ColorState = ColorState.RED
    ) {
        this.mErrorState = mErrorState
        this.mSuccessState = mSuccessState
        this.mInitialState = mInitialState

        setCurrentState(CurrentState.INITIAL)
    }


    enum class ColorState {
        GRAY, GREEN, RED, BLUE
    }

    enum class CurrentState {
        INITIAL, ERROR, SUCCESS
    }


}
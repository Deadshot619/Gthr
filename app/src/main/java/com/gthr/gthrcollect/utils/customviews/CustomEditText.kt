package com.gthr.gthrcollect.utils.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
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

    private var mLlMain: LinearLayoutCompat
    private var mIvInfo : AppCompatImageView
    private var mLlInfo : LinearLayoutCompat

    var mTvTitle : AppCompatTextView
        private set
    var mTvInfo : AppCompatTextView
        private set
    var mEtMain: AppCompatEditText
        private set
    var mIvMain: AppCompatImageView
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
        mTvInfo = view.findViewById(R.id.tv_info)
        mIvInfo = view.findViewById(R.id.iv_info)
        mTvTitle = view.findViewById(R.id.tv_title)
        mLlInfo = view.findViewById(R.id.ll_info)

        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomEditText)

        attrs.getString(R.styleable.CustomEditText_edit_hint)?.let { mEtMain.hint = it }
        attrs.getString(R.styleable.CustomEditText_edit_error_text)?.let { mErrorText = it }




        attrs.getResourceId(R.styleable.CustomEditText_android_src, -1).let {
            if (it != -1) {
                mImageFlag = true
                mIvMain.visible()
                mIvMain.setImageResource(it)
            }
        }

        mInitialState = ColorState.values()[attrs.getInt(R.styleable.CustomEditText_edit_initial_state, 0)]
        mErrorState = ColorState.values()[attrs.getInt(R.styleable.CustomEditText_edit_error_state, 2)]
        mSuccessState = ColorState.values()[attrs.getInt(R.styleable.CustomEditText_edit_success_state, 1)]

        attrs.getString(R.styleable.CustomEditText_edit_info)?.let {
            mLlInfo.visible()
            mTvInfo.text = it
            setInfoColor()
        }
        attrs.getString(R.styleable.CustomEditText_edit_title)?.let {
            mTvTitle.visible()
            mTvTitle.text = it
        }

        setCurrentState(CurrentState.INITIAL)
        setCustomTextChangeListener()

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
                getImageDrawable(R.drawable.blue_bg_outline)
            }
            ColorState.RED -> {
                mIvMain.setColorFilter(ContextCompat.getColor(context, R.color.red))
                getImageDrawable(R.drawable.edit_text_red_outline_bg)
            }
        }
    }

    fun setError(text: String?) {
        mCurrentState = CurrentState.ERROR
        mIvMain.gone()
        mEtMain.error = text
        setState(mErrorState)
    }

    fun setSuccess() {
        mCurrentState = CurrentState.SUCCESS
        if (mImageFlag) mIvMain.visible() else mIvMain.gone()
        mEtMain.error = null
        setState(mSuccessState)
        setInfoColor()
    }

    fun setInitial() {
        mCurrentState = CurrentState.ERROR
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

    fun setInfoColor(){
        mIvInfo.setColorFilter(when(mSuccessState){
            ColorState.BLUE -> ContextCompat.getColor(context, R.color.blue)
            ColorState.GRAY -> ContextCompat.getColor(context, R.color.disable)
            ColorState.GREEN -> ContextCompat.getColor(context, R.color.green)
            ColorState.RED -> ContextCompat.getColor(context, R.color.red)
        })
    }

    private fun setCustomTextChangeListener() {
        mEtMain.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (mCurrentState==CurrentState.ERROR)
                    setInitial()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    enum class ColorState {
        GRAY, GREEN, RED, BLUE
    }

    enum class CurrentState {
        INITIAL, ERROR, SUCCESS
    }


}
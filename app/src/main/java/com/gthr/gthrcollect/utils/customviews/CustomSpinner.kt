package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class CustomSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr)  {


    private val view: View = LayoutInflater.from(context).inflate(R.layout.layout_custom_spinner, this, true)
    private val mLlMain: LinearLayoutCompat = view.findViewById(R.id.ll_main)

    var mSpnMain: AppCompatSpinner = view.findViewById(R.id.spn_main)
        private set

    var mTvTitle : AppCompatTextView = view.findViewById(R.id.tv_title)
        private set

    private var mInitialState: ColorState
    private var mSuccessState: ColorState
    private var mErrorState: ColorState

    private var mCurrentState: CurrentState = CurrentState.INITIAL


    init{
        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSpinner)
        attrs.getString(R.styleable.CustomSpinner_cs_title)?.let {
            mTvTitle.visible()
            mTvTitle.text = it
        }

        mInitialState = ColorState.values()[attrs.getInt(R.styleable.CustomSpinner_cs_initial_state, 0)]
        mSuccessState = ColorState.values()[attrs.getInt(R.styleable.CustomSpinner_cs_success_state, 1)]
        mErrorState = ColorState.values()[attrs.getInt(R.styleable.CustomSpinner_cs_error_state, 2)]
        setCurrentState(CurrentState.INITIAL)

        attrs.recycle()
    }


    private fun setCurrentState(state: CurrentState) {
        mCurrentState = state
        when (state) {
            CurrentState.INITIAL -> setInitial()
            CurrentState.SUCCESS -> setSuccess()
            CurrentState.ERROR -> setError()

        }
    }

    fun setError() {
        mCurrentState = CurrentState.ERROR
        setState(mErrorState)
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

    fun setSuccess() {
        mCurrentState = CurrentState.SUCCESS
        setState(mSuccessState)
    }

    fun setInitial() {
        mCurrentState = CurrentState.INITIAL
        setState(mInitialState)
    }

    private fun setState(state: ColorState) {
        mLlMain.background = when (state) {
            ColorState.GRAY -> {
                getImageDrawable(R.drawable.edit_text_gray_bg)
            }
            ColorState.GREEN -> {
                getImageDrawable(R.drawable.edit_text_green_outline_bg)
            }
            ColorState.BLUE -> {
                getImageDrawable(R.drawable.blue_bg_outline)
            }
            ColorState.RED ->{
                getImageDrawable(R.drawable.edit_text_red_outline_bg)
            }
        }
    }

    enum class ColorState {
        GRAY, GREEN,RED, BLUE
    }

    enum class CurrentState {
        INITIAL,  ERROR, SUCCESS
    }

}
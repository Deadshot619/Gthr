package com.gthr.gthrcollect.utils.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.hbb20.CountryCodePicker


@SuppressLint("Recycle")
class CustomPhoneNoEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mLlPhoneNo: LinearLayout

   var mCcp: CountryCodePicker
        private set
   var mEtPhoneNo: AppCompatEditText
        private set
    var mState = ColorState.GRAY
        private set

    private var mInitialState : ColorState
    private var mErrorState : ColorState
    private var mSuccessState : ColorState
    private var mCurrentState : CurrentState = CurrentState.INITIAL

    var mErrorText: String = ""

    private val view: View = LayoutInflater.from(context).inflate(R.layout.layout_custom_phone_no_edit_text, this, true)

    init {
        mLlPhoneNo = view.findViewById(R.id.ll_phone_no)
        mCcp = view.findViewById(R.id.ccp)
        mEtPhoneNo = view.findViewById(R.id.et_phone_no)

        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomPhoneNoEditText)

        attrs.getString(R.styleable.CustomPhoneNoEditText_phone_no_hint)?.let { mEtPhoneNo.hint = it }
        attrs.getString(R.styleable.CustomPhoneNoEditText_phone_no_error_text)?.let { mErrorText = it }
        mInitialState = ColorState.values()[attrs.getInt(R.styleable.CustomPhoneNoEditText_phone_no_initial_state, 0)]
        mErrorState = ColorState.values()[attrs.getInt(R.styleable.CustomPhoneNoEditText_phone_no_error_state, 2)]
        mSuccessState = ColorState.values()[attrs.getInt(R.styleable.CustomPhoneNoEditText_phone_no_success_state, 1)]

        setCurrentState(CurrentState.INITIAL)
    }

    private fun setState(state : ColorState) {
        mState = state
        mLlPhoneNo.background =  when(state){
            ColorState.GRAY -> getImageDrawable(R.drawable.edit_text_gray_bg)
            ColorState.GREEN -> getImageDrawable(R.drawable.edit_text_green_outline_bg)
            ColorState.RED -> getImageDrawable(R.drawable.edit_text_red_outline_bg)
            ColorState.BLUE -> getImageDrawable(R.drawable.blue_bg_outline)
        }
    }

    private fun setCurrentState(state: CurrentState) {
        mCurrentState = state
        when (state) {
           CurrentState.INITIAL -> setInitial()
           CurrentState.SUCCESS -> setSuccess()
           CurrentState.ERROR -> setError(mErrorText)
        }
    }



    fun setError(text: String) {
        mCurrentState = CurrentState.ERROR
        mEtPhoneNo.error = text
        setState(mErrorState)
    }

    fun setSuccess() {
        mCurrentState = CurrentState.SUCCESS
        mEtPhoneNo.error = null
        setState(mSuccessState)
    }

    fun setInitial() {
        mCurrentState = CurrentState.INITIAL
        mEtPhoneNo.error = null
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


    enum class ColorState{
        GRAY,GREEN,RED,BLUE
    }

    enum class CurrentState {
        INITIAL, ERROR, SUCCESS
    }


}
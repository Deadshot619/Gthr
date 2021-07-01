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
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible
import com.hbb20.CountryCodePicker


@SuppressLint("Recycle")
class CustomPhoneNoEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mLlPhoneNo: LinearLayout

    private var mCcp: CountryCodePicker
        private set
    private var mEtPhoneNo: AppCompatEditText
        private set
    var mState = State.GRAY
        private set

    var mInitialState : State
    var mErrorState : State
    var mSuccessState : State
    var mCurrentState : CurrentState

    var mErrorText: String = ""

    private val view: View = LayoutInflater.from(context).inflate(R.layout.layout_custom_phone_no_edit_text, this, true)

    init {
        mLlPhoneNo = view.findViewById(R.id.ll_phone_no)
        mCcp = view.findViewById(R.id.ccp)
        mEtPhoneNo = view.findViewById(R.id.et_phone_no)

        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomPhoneNoEditText)

        attrs.getString(R.styleable.CustomPhoneNoEditText_phone_no_hint)?.let { mEtPhoneNo.hint = it }
        attrs.getString(R.styleable.CustomPhoneNoEditText_phone_no_error_text)?.let { mErrorText = it }
        mInitialState = State.values()[attrs.getInt(R.styleable.CustomPhoneNoEditText_phone_no_initial_state, 0)]
        mErrorState = State.values()[attrs.getInt(R.styleable.CustomPhoneNoEditText_phone_no_error_state, 2)]
        mSuccessState = State.values()[attrs.getInt(R.styleable.CustomPhoneNoEditText_phone_no_success_state, 1)]
        mCurrentState = CurrentState.values()[attrs.getInt(R.styleable.CustomPhoneNoEditText_phone_no_current_state, 0)]

        setCurrentState(mCurrentState)
    }

    fun setState(state : State) {
        mState = state
        mLlPhoneNo.background =  when(state){
            State.GRAY -> getImageDrawable(R.drawable.edit_text_gray_bg)
            State.GREEN -> getImageDrawable(R.drawable.edit_text_green_outline_bg)
            State.RED -> getImageDrawable(R.drawable.edit_text_red_outline_bg)
        }
    }

    private fun setCurrentState(state: CurrentState) {
        when (state) {
           CurrentState.INITIAL -> setInitial()
           CurrentState.SUCCESS -> setSuccess()
           CurrentState.ERROR -> setError(mErrorText)
        }
    }

    fun showError(text : String){
        mLlPhoneNo.background = getImageDrawable(R.drawable.edit_text_red_outline_bg)
        mEtPhoneNo.error = text
    }

    fun setError(text: String) {
        mEtPhoneNo.error = text
        setState(mErrorState)
    }

    fun setSuccess() {
        mEtPhoneNo.error = null
        setState(mSuccessState)
    }

    fun setInitial() {
        mEtPhoneNo.error = null
        setState(mInitialState)
    }


    enum class State{
        GRAY,GREEN,RED
    }

    enum class CurrentState {
        INITIAL, ERROR, SUCCESS
    }


}
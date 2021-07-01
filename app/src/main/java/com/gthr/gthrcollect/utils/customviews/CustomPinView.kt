package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.chaos.view.PinView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class CustomPinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr){

    private val mView: View = LayoutInflater.from(context).inflate(R.layout.layout_custom_pin_view, this, true)
    private val mError : AppCompatTextView = mView.findViewById(R.id.et_error)
    val mPinView : PinView = mView.findViewById(R.id.otp)


    fun setInitial(){
        mError.gone()
        mPinView.setLineColor(ResourcesCompat.getColor(resources, R.color.disable,mPinView.context.theme))
    }

    fun setSuccess(){
        mError.gone()
        mPinView.setLineColor(ResourcesCompat.getColor(resources, R.color.green,mPinView.context.theme))
    }

    fun setError(text : String){
        mError.visible()
        mError.requestFocus();
        mError.error = text
        mPinView.setLineColor(ResourcesCompat.getColor(resources, R.color.red,mPinView.context.theme))
    }



}
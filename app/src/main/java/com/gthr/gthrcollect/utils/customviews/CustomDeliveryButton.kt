package com.gthr.gthrcollect.utils.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.getResolvedColor


class CustomDeliveryButton  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr)  {

    var mCurrentType : Type = Type.PRICE

    init{

        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomDeliveryButton)
        val state = Type.values()[attrs.getInt(R.styleable.CustomDeliveryButton_cdb_type, 0)]

        this.textAlignment = View.TEXT_ALIGNMENT_CENTER
        val padding = resources.getDimensionPixelOffset(R.dimen.padding_small)
        val paddingVertical = resources.getDimensionPixelOffset(R.dimen.padding_custom_delivery_button)
        this.setPadding(padding, paddingVertical, padding, paddingVertical)
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_size_semi_small))
        this.maxLines = 1
        this.ellipsize = TextUtils.TruncateAt.END



        attrs.recycle()
        setType(state)
    }


    @SuppressLint("UseCompatTextViewDrawableApis")
    fun setType(type: Type) {
        mCurrentType = type
        when(type){
            Type.PRICE -> {
                this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                this.background = getImageDrawable(R.drawable.bg_btn_price_blue)
                this.setTextColor(getResolvedColor(R.color.white))
            }
            Type.PENDING -> {
                this.text = context.getString(R.string.text_pending)
                val  drawable = getImageDrawable(R.drawable.ic_pending)
                this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable!!, null)
                this.background = getImageDrawable(R.drawable.bg_btn_delivery_yellow)
                this.setTextColor(getResolvedColor(R.color.white))
            }
            Type.DELIVERED -> {
                this.text = context.getString(R.string.text_delivered)
                val  drawable = getImageDrawable(R.drawable.ic_delivered)
                this.background = getImageDrawable(R.drawable.bg_btn_delivery_blue_border)
                this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable!!, null)
                this.setTextColor(getResolvedColor(R.color.blue))
            }
            Type.TRANSIT -> {
                this.text = context.getString(R.string.text_transit)
                val  drawable = getImageDrawable(R.drawable.ic_transit)
                this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable!!, null)
                this.background = getImageDrawable(R.drawable.bg_btn_delivery_green)
                this.setTextColor(getResolvedColor(R.color.white))
            }
        }
    }


    enum class Type{
        PRICE,PENDING,TRANSIT,DELIVERED
    }

}
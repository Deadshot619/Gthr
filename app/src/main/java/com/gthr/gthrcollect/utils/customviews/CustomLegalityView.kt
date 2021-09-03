package com.gthr.gthrcollect.utils.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.getResolvedColor


class CustomLegalityView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr)  {

    var mCurrentType : Type = Type.LEGAL

    init{

        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomLegalityView)
        val state = Type.values()[attrs.getInt(R.styleable.CustomLegalityView_clv_type, 0)]

        this.textAlignment = View.TEXT_ALIGNMENT_CENTER
        val paddingHorizontal = resources.getDimensionPixelOffset(R.dimen.padding_extra_small)
        val paddingVertical = resources.getDimensionPixelOffset(R.dimen.padding_mini)
        this.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
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
            Type.LEGAL -> {
                this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                this.background = getImageDrawable(R.drawable.bg_custum_legality_blue)
                this.setTextColor(getResolvedColor(R.color.white))
                this.typeface = ResourcesCompat.getFont(context, R.font.lato)
                this.text = context.getString(R.string.text_legal_product_detail_mtg)
            }
            Type.NOT_LEGAL -> {
                this.text = context.getString(R.string.text_pending)
                this.background = getImageDrawable(R.drawable.bg_custum_legality_white)
                this.setTextColor(getResolvedColor(R.color.black))
                this.typeface = ResourcesCompat.getFont(context, R.font.lato)
                this.text = context.getString(R.string.text_not_legal_product_detail_mtg)
            }
        }
    }


    enum class Type {
        NOT_LEGAL, LEGAL
    }

}
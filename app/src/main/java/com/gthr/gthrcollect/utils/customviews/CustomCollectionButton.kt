package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.PermissionChecker
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable

class CustomCollectionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    var mCurrentType : Type = Type.COLLECTION

    init{

        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCollectionButton)
        val type = Type.values()[attrs.getInt(
            R.styleable.CustomCollectionButton_ccb_type,
            1
        )]

        this.textAlignment = View.TEXT_ALIGNMENT_CENTER
        val padding = resources.getDimensionPixelOffset(R.dimen.padding_normal)
        val paddingVertical = resources.getDimensionPixelOffset(R.dimen.padding_extra_small)
        this.setPadding(padding, paddingVertical, padding, paddingVertical)
        this.background = getImageDrawable(R.drawable.bg_btn_delivery_light_blue)
        this.compoundDrawablePadding = paddingVertical
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_size_small))

        setType(type)
        attrs.recycle()



    }

    private fun setType(type: Type) {
        mCurrentType = type
        when(type){
             Type.COLLECTION -> {
                 this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_collection, 0, 0, 0)
                 this.text = context.getString(R.string.text_collection)
             }
             Type.FAVORITES -> {
                 this.text = context.getString(R.string.text_favorites)
                 this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorites, 0, 0, 0)
             }
        }
    }


    enum class Type{
        COLLECTION,FAVORITES
    }

}
package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.getResolvedColor

class CustomFollowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    var mCurrentType: Type = Type.FOLLOW

    init {

        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFollowView)
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
        this.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimension(R.dimen.text_size_small)
        )

        setType(type)
        attrs.recycle()
    }

    private fun setType(type: Type) {
        mCurrentType = type
        when (type) {
            Type.FOLLOWING -> {
                this.text = context.getString(R.string.text_following)
                this.background = getImageDrawable(R.drawable.bg_custom_collection_type_blue)
                this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                this.setTextColor(getResolvedColor(R.color.white))
            }
            Type.FOLLOW -> {
                this.text = context.getString(R.string.text_follow)
                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_collection, 0, 0, 0)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.compoundDrawableTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.text_color_dark_grey
                        )
                    )
                }
                this.background = getImageDrawable(R.drawable.bg_follow_gray)

                this.setTextColor(getResolvedColor(R.color.text_color_dark_grey))
            }
        }
    }

    fun setFollow(){
        setType(Type.FOLLOW)
    }

    fun setFollowing(){
        setType(Type.FOLLOWING)
    }

    enum class Type {
        FOLLOW, FOLLOWING
    }

}
package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.model.domain.ShippingAddress
import com.gthr.gthrcollect.utils.extensions.getImageDrawable

class CustomAddressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val view: View =
        LayoutInflater.from(context).inflate(R.layout.layout_custom_address_view, this, true)

    private var mMainLayout: ConstraintLayout? = null
    private var mIvCheck: AppCompatImageView? = null
    private var mIvEdit: AppCompatImageView? = null
    private var mIvDelete: AppCompatImageView? = null
    private var mViewHorizontal: View? = null
    private var mViewVertical: View? = null

    private var mTvFullName: TextView? = null
    private var mTvAddress1: TextView? = null
    private var mTvAddress2: TextView? = null
    private var mTvCity: TextView? = null
    private var mTvStatePinCode: TextView? = null

    var isViewSelected: Boolean = false
        private set

    init {
        initViews(attrs)
    }

    private fun initViews(attrs: AttributeSet?) {
        mMainLayout = view.findViewById(R.id.cl_main_layout)
        mIvCheck = view.findViewById(R.id.iv_check)
        mIvEdit = view.findViewById(R.id.iv_edit)
        mIvDelete = view.findViewById(R.id.iv_delete)
        mViewHorizontal = view.findViewById(R.id.view_horizontal)
        mViewVertical = view.findViewById(R.id.view_vertical)

        mTvFullName = view.findViewById(R.id.tv_full_name)
        mTvAddress1 = view.findViewById(R.id.tv_address_1)
        mTvAddress2 = view.findViewById(R.id.tv_address_2)
        mTvCity = view.findViewById(R.id.tv_city)
        mTvStatePinCode = view.findViewById(R.id.tv_state_pin)
    }

    fun setSelected() {
        mMainLayout?.setBackgroundResource(R.drawable.address_view_selected_bg)
        mIvCheck?.setImageDrawable(getImageDrawable(R.drawable.ic_circular_check_selected))
        mIvEdit?.setImageDrawable(getImageDrawable(R.drawable.ic_pencil_selected))
        mViewHorizontal?.background = getImageDrawable(R.color.light_blue)
        mViewVertical?.background = getImageDrawable(R.color.light_blue)
        isViewSelected = true
    }

    fun setUnselected() {
        mMainLayout?.setBackgroundResource(R.drawable.address_view_unselected_bg)
        mIvCheck?.setImageDrawable(getImageDrawable(R.drawable.ic_circular_check_unselected))
        mIvEdit?.setImageDrawable(getImageDrawable(R.drawable.ic_pencil_unselected))
        mViewHorizontal?.background = getImageDrawable(R.color.view_stroke_color_grey)
        mViewVertical?.background = getImageDrawable(R.color.view_stroke_color_grey)
        isViewSelected = false
    }

    fun updateValues(shippingAddress: ShippingAddress) {
        shippingAddress.run {
            mTvFullName?.text =
                String.format(context.getString(R.string.full_name_builder), firstName, lastName)
            mTvAddress1?.text = addressLine1
            mTvAddress2?.text = addressLine2
            mTvCity?.text = String.format(context.getString(R.string.city_builder), city)
            mTvStatePinCode?.text =
                String.format(context.getString(R.string.state_pincode_builder), state, postalCode)
            if (isSelected)
                setSelected()
            else
                setUnselected()
        }
    }
}
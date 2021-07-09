package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable

class CustomCollectionChartsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr)  {

    private val mView: View = LayoutInflater.from(context).inflate(R.layout.layout_custom_collection_charts, this, true)
    private val mClMain: ConstraintLayout = mView.findViewById(R.id.cl_main)
    private val mTvTitle: AppCompatTextView = mView.findViewById(R.id.tv_title)
    private val mTvAmount: AppCompatTextView = mView.findViewById(R.id.tv_amount)

    var mCurrentType = Type.MARKET_VALUE

    init{
        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCollectionChartsView)
        mCurrentType = Type.values()[attrs.getInt(R.styleable.CustomCollectionChartsView_cccv_type, 0)]

        attrs.getString(R.styleable.CustomCollectionChartsView_cccv_amount)?.let { mTvAmount.text = it }

        setCurrentType(mCurrentType)

    }

    private fun setCurrentType(type: Type) {
        mCurrentType = type
        mClMain.background = when(type){
            Type.MARKET_VALUE -> {
                mTvTitle.text = context.getString(R.string.title_market_value)
                getImageDrawable(R.drawable.ic_market_value)
            }
            Type.COLLECTION_SIZE -> {
                mTvTitle.text = context.getString(R.string.title_collection_size)
                getImageDrawable(R.drawable.ic_collection_size)
            }
            Type.AVERAGE_PRICE -> {
                mTvTitle.text = context.getString(R.string.title_collection_size)
                getImageDrawable(R.drawable.ic_average_price)
            }
        }
    }

    fun setTypeMarketValue(){
        setCurrentType(Type.MARKET_VALUE)
    }

    fun setTypeCollectionSize(){
        setCurrentType(Type.COLLECTION_SIZE)
    }

    fun setAveragePrice(){
        setCurrentType(Type.AVERAGE_PRICE)
    }

    enum class Type{
        MARKET_VALUE,COLLECTION_SIZE,AVERAGE_PRICE
    }

}
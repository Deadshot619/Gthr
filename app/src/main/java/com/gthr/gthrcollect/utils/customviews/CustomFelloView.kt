package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable

class CustomFelloView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr)  {


    private val mView: View = LayoutInflater.from(context).inflate(R.layout.layout_custom_fello, this, true)
    private val mIvMain:AppCompatImageView = mView.findViewById(R.id.iv_main)
    private val mTvTitle:AppCompatTextView = mView.findViewById(R.id.tv_title)
    val mTvCount:AppCompatTextView = mView.findViewById(R.id.tv_count)

    var mCurrentType = Type.FOLLOWERS

    init{
        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFelloView)

        attrs.getString(R.styleable.CustomFelloView_cfv_count)?.let { mTvCount.text = it }

        mCurrentType = Type.values()[attrs.getInt(R.styleable.CustomFelloView_cfv_type, 0)]
        setCurrentType(mCurrentType)
        attrs.recycle()
    }

    private fun setCurrentType(type : Type){
        mCurrentType = type
        when(type){
            Type.FOLLOWERS -> {
                mIvMain.setImageDrawable(getImageDrawable(R.drawable.ic_followers))
                mTvTitle.text = context.getString(R.string.title_followers)
            }
            Type.FOLLOWING -> {
                mIvMain.setImageDrawable(getImageDrawable(R.drawable.ic_following))
                mTvTitle.text = context.getString(R.string.title_following)
            }
            Type.SOLD -> {
                mIvMain.setImageDrawable(getImageDrawable(R.drawable.ic_sold))
                mTvTitle.text = context.getString(R.string.title_sold)
            }
            Type.COLLECTION -> {
                mIvMain.setImageDrawable(getImageDrawable(R.drawable.ic_sold))
                mTvTitle.text = context.getString(R.string.lable_collection)
            }
        }
    }

    fun setTypeFollowers(){
        setCurrentType(mCurrentType)
    }

    fun setTypeFollowing(){
        setCurrentType(mCurrentType)
    }

    fun setTypeSold(){
        setCurrentType(mCurrentType)
    }

    fun setCount(count : String ){
        mTvCount.text = count
    }

    enum class Type{
        FOLLOWERS,FOLLOWING,SOLD,COLLECTION
    }
}
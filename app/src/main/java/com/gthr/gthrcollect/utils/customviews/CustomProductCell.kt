package com.gthr.gthrcollect.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.getImageDrawable
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class CustomProductCell @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val mView: View =
        LayoutInflater.from(context).inflate(R.layout.layout_custom_product_cell, this, true)

    private val mIvMain: AppCompatImageView = mView.findViewById(R.id.iv_main)
    private val mIvIcon: AppCompatImageView = mView.findViewById(R.id.iv_icon)
    private val mIvFav: AppCompatImageView = mView.findViewById(R.id.iv_fav)

    private val mTvCardState: AppCompatTextView = mView.findViewById(R.id.tv_card_state)
    private val mTvProductType: AppCompatTextView = mView.findViewById(R.id.tv_product_type)
    private val mTvTitle: AppCompatTextView = mView.findViewById(R.id.tv_title)
    val mTvPrice: AppCompatTextView = mView.findViewById(R.id.tv_price)

    private val mTvRow1Collum1: AppCompatTextView = mView.findViewById(R.id.tv_row1_collum1)
    private val mTvRow1Collum2: AppCompatTextView = mView.findViewById(R.id.tv_row1_collum2)
    private val mTvRow2Collum1: AppCompatTextView = mView.findViewById(R.id.tv_row2_collum1)
    private val mTvRow2Collum2: AppCompatTextView = mView.findViewById(R.id.tv_row2_collum2)

    val mTvGlob: AppCompatTextView = mView.findViewById(R.id.tv_glob)
    val mTvPsaValue: AppCompatTextView = mView.findViewById(R.id.tv_psa_value)
    val mTvFoil: AppCompatTextView = mView.findViewById(R.id.tv_foil)

    private val mGroupGlob: Group = mView.findViewById(R.id.group_glob)
    private val mGroupPsa: Group = mView.findViewById(R.id.group_psa)
    private val mClBottom: ConstraintLayout = mView.findViewById(R.id.cl_Bottom)

    var mCurrentType = Type.FUNKO
    var mCurrentState = State.FOR_SALE

    init {
        mIvMain.clipToOutline = true

        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomProductCell)
        mCurrentType = Type.values()[attrs.getInt(R.styleable.CustomProductCell_cpc_type, 0)]
        mCurrentState = State.values()[attrs.getInt(R.styleable.CustomProductCell_cpc_card_state, 0)]

        setType(mCurrentType)
        setState(mCurrentState)

        attrs.recycle()
    }

    fun setType(type: Type) {
        mCurrentType = type
        when(type){
            Type.FUNKO -> {
                mTvProductType.text = context.getString(R.string.text_funko)
                mTvRow1Collum1.text = context.getString(R.string.text_product_name)
                mTvRow2Collum1.text = context.getString(R.string.text_hash_colon)
                mGroupGlob.gone()
                mGroupPsa.gone()
            }
            Type.SEALED -> {
                mTvProductType.text = context.getString(R.string.text_sealed)
                mTvRow1Collum1.text = context.getString(R.string.text_product_name)
                mTvRow2Collum1.text = context.getString(R.string.text_product_set)
                mGroupGlob.gone()
                mGroupPsa.gone()
            }
            Type.MYTHIC -> {
                mTvProductType.text = context.getString(R.string.text_mythic)
                mTvRow1Collum1.text = context.getString(R.string.text_product_set)
                mTvRow2Collum1.text = context.getString(R.string.text_hash_colon)
                mGroupGlob.visible()
                mGroupPsa.visible()
            }
            Type.HOLO_RARE -> {
                mTvProductType.text = context.getString(R.string.text_holo_rare)
                mTvRow1Collum1.text = context.getString(R.string.text_product_set)
                mTvRow2Collum1.text = context.getString(R.string.text_hash_colon)
                mGroupGlob.visible()
                mGroupPsa.visible()
            }
            Type.SECRET_RARE -> {
                mTvProductType.text = context.getString(R.string.text_secret_rare)
                mTvRow1Collum1.text = context.getString(R.string.text_product_set)
                mTvRow2Collum1.text = context.getString(R.string.text_hash_colon)
                mGroupGlob.visible()
                mGroupPsa.visible()
            }
        }
    }

    fun setState(state:State){
        mCurrentState = state
        when(state){
            State.FOR_SALE -> {
                mTvTitle.text = context.getString(R.string.text_sale_price)
                mTvCardState.visible()
                mTvCardState.text = context.getString(R.string.text_fore_sale)
                mTvCardState.background = getImageDrawable(R.drawable.bg_product_for_green)
                mClBottom.visible()
                mIvFav.gone()
                mIvIcon.gone()
            }
            State.WANT -> {
                mTvTitle.text = context.getString(R.string.text_buylist_price)
                mTvCardState.visible()
                mTvCardState.text = context.getString(R.string.text_want)
                mTvCardState.background = getImageDrawable(R.drawable.bg_product_for_red)
                mClBottom.visible()
                mIvFav.gone()
                mIvIcon.gone()
            }
            State.NORMAL -> {
                mTvTitle.text = context.getString(R.string.text_market_price)
                mTvCardState.gone()
                mClBottom.gone()
                mIvFav.gone()
                mIvIcon.gone()
            }
            State.FAVORITE -> {
                mTvTitle.text = context.getString(R.string.text_market_price)
                mTvCardState.gone()
                mClBottom.gone()
                mIvFav.visible()
                mIvIcon.gone()
            }
            State.OFFER -> {
                mTvTitle.text = context.getString(R.string.text_offer_price)
                mTvCardState.visible()
                mTvCardState.text = context.getString(R.string.text_offer)
                mTvCardState.background = getImageDrawable(R.drawable.bg_product_for_green)
                mClBottom.visible()
                mIvFav.gone()
                mIvIcon.visible()
            }
            State.SOLD -> {
                mTvTitle.text = context.getString(R.string.text_sale_price)
                mTvCardState.visible()
                mTvCardState.text = context.getString(R.string.text_sold)
                mTvCardState.background = getImageDrawable(R.drawable.bg_product_for_green)
                mClBottom.visible()
                mIvFav.gone()
                mIvIcon.gone()
            }
        }
    }

    enum class Type {
        FUNKO, SEALED, MYTHIC, HOLO_RARE, SECRET_RARE
    }

    enum class State {
        FOR_SALE, WANT, NORMAL, FAVORITE, OFFER, SOLD
    }
}
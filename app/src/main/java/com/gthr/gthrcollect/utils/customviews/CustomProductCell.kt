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
import com.gthr.gthrcollect.model.domain.ForSaleItemDomainModel
import com.gthr.gthrcollect.model.domain.ItemDisplayDomainModel
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.model.domain.SearchBidsDomainModel
import com.gthr.gthrcollect.utils.enums.ConditionType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.*
import com.gthr.gthrcollect.utils.helper.getConditionTitle

class CustomProductCell @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val mView: View =
        LayoutInflater.from(context).inflate(R.layout.layout_custom_product_cell, this, true)

    val mIvMain: AppCompatImageView = mView.findViewById(R.id.iv_main)
    private val mIvIcon: AppCompatImageView = mView.findViewById(R.id.iv_icon)
    private val mIvFav: AppCompatImageView = mView.findViewById(R.id.iv_fav)

    private val mTvCardState: AppCompatTextView = mView.findViewById(R.id.tv_card_state)
    private val mTvProductRarity: AppCompatTextView = mView.findViewById(R.id.tv_product_rarity)
    private val mTvTitle: AppCompatTextView = mView.findViewById(R.id.tv_title)
    val mTvPrice: AppCompatTextView = mView.findViewById(R.id.tv_price)

    private val mTvRow1Collum1: AppCompatTextView = mView.findViewById(R.id.tv_row1_collum1)
    private val mTvRow1Collum2: AppCompatTextView = mView.findViewById(R.id.tv_row1_collum2)
    private val mTvRow2Collum1: AppCompatTextView = mView.findViewById(R.id.tv_row2_collum1)
    private val mTvRow2Collum2: AppCompatTextView = mView.findViewById(R.id.tv_row2_collum2)

    val mTvLanguage: AppCompatTextView = mView.findViewById(R.id.tv_language)
    val mTvConditionTitle: AppCompatTextView = mView.findViewById(R.id.tv_condition)
    val mTvConditionValue: AppCompatTextView = mView.findViewById(R.id.tv_condition_value)
    val mTvEdition: AppCompatTextView = mView.findViewById(R.id.tv_edition)

    private val mGroupLanguage: Group = mView.findViewById(R.id.group_language)
    private val mGroupCondition: Group = mView.findViewById(R.id.group_condition)
    private val mClBottom: ConstraintLayout = mView.findViewById(R.id.cl_Bottom)

    var mCurrentType = Type.FUNKO
    var mCurrentState = State.FOR_SALE

    init {
        mIvMain.clipToOutline = true

        val attrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomProductCell)
        mCurrentType = Type.values()[attrs.getInt(R.styleable.CustomProductCell_cpc_type, 0)]
        mCurrentState =
            State.values()[attrs.getInt(R.styleable.CustomProductCell_cpc_card_state, 0)]

        setType(mCurrentType)
        setState(mCurrentState)

        attrs.recycle()
    }

    fun setType(type: Type) {
        mCurrentType = type
        when (type) {
            Type.FUNKO -> {
                mTvRow1Collum1.text = context.getString(R.string.text_product_name)
                mTvRow2Collum1.text = context.getString(R.string.text_hash_colon)
                mGroupLanguage.gone()
                mGroupCondition.gone()
                //Note : Condition has been set in Edition field because it is currently easier
                // to manage.
                setEdition(context.getString(R.string.text_new))
            }
            Type.SEALED -> {
                mTvRow1Collum1.text = context.getString(R.string.text_product_name)
                mTvRow2Collum1.text = context.getString(R.string.text_product_set)
                mGroupLanguage.gone()
                mGroupCondition.gone()
                setEdition(context.getString(R.string.text_new))
            }
            Type.CARDS -> {
                mTvRow1Collum1.text = context.getString(R.string.text_product_set)
                mTvRow2Collum1.text = context.getString(R.string.text_hash_colon)
                mGroupLanguage.visible()
                mGroupCondition.visible()
            }
        }
    }

    fun setValue(model: ItemDisplayDomainModel) {
        when (model.productType ?: model.productCategory) {
            ProductType.MAGIC_THE_GATHERING,
            ProductType.POKEMON,
            ProductType.YUGIOH,
            ProductCategory.CARDS -> {
                setType(Type.CARDS)
                setProductName(model.description)
                setProductNumber(model.productNumber ?: "-")
                setEdition(model.editionType?.title ?: "-")
                setLanguage(model.language?.abbreviatedName ?: "-")
                setConditionTitle(model.condition?.type?.title ?: "-")
                setConditionValue(model.condition?.abbreviatedName ?: "-")
            }
            ProductType.FUNKO, ProductCategory.TOYS -> {
                setType(Type.FUNKO)
                setProductName(model.name)
                setProductNumber(model.productNumber ?: "-")
            }
            ProductType.SEALED_YUGIOH,
            ProductType.SEALED_POKEMON,
            ProductType.SEALED_MTG,
            ProductCategory.SEALED -> {
                setType(Type.SEALED)
                setProductName(model.name)
                setProductNumber(model.description ?: "-")
            }
        }
        setProductRarity(model.rarity ?: "-")
        setPrice(model.price.toString())
        setImage(model.firImageURL.toString())
    }

    fun setValue(productDisplayModel: ProductDisplayModel) {
        when {
            productDisplayModel.forsaleItemNodel != null ->
                setValue(productDisplayModel.forsaleItemNodel)
            productDisplayModel.searchBidsDomainModel != null ->
                setValue(productDisplayModel.searchBidsDomainModel)
            else -> {
                when (productDisplayModel.productType) {
                    ProductType.MAGIC_THE_GATHERING,
                    ProductType.POKEMON,
                    ProductType.YUGIOH -> {
                        setType(Type.CARDS)
                        setProductName(productDisplayModel.description)
                        setProductNumber(productDisplayModel.productNumber.toString())
                    }
                    ProductType.FUNKO -> {
                        setType(Type.FUNKO)
                        setProductName(productDisplayModel.name.toString())
                        setProductNumber(productDisplayModel.productNumber.toString())
                    }
                    ProductType.SEALED_YUGIOH,
                    ProductType.SEALED_POKEMON,
                    ProductType.SEALED_MTG -> {
                        setType(Type.SEALED)
                        setProductName(productDisplayModel.name.toString())
                        setProductNumber(productDisplayModel.description.toString())
                    }
                }
                setProductRarity(productDisplayModel.rarity.toString())
                setPrice(productDisplayModel.lowestAskCost.toString())
                setImage(productDisplayModel.firImageURL.toString())
            }
        }
    }

    fun setValue(model: ForSaleItemDomainModel) {
        when (model.productType ?: model.productCategory) {
            ProductType.MAGIC_THE_GATHERING,
            ProductType.POKEMON,
            ProductType.YUGIOH,
            ProductCategory.CARDS -> {
                setType(Type.CARDS)
                setProductName(model.productGroup)
                setProductNumber(model.productProductNumber ?: "-")
                setEdition(model.edition ?: "-")
                setLanguage(model.language?.abbreviatedName ?: "-")
                setConditionTitle(context.getConditionTitle(model.condition?.type!!) ?: "-")
                setConditionValue(model.condition?.abbreviatedName ?: "-")
            }
            ProductType.FUNKO, ProductCategory.TOYS -> {
                setType(Type.FUNKO)
                setProductName(model.productProductName)
                setProductNumber(model.productProductNumber ?: "-")
            }
            ProductType.SEALED_YUGIOH,
            ProductType.SEALED_POKEMON,
            ProductType.SEALED_MTG,
            ProductCategory.SEALED -> {
                setType(Type.SEALED)
                setProductName(model.productProductName)
                setProductNumber(model.productGroup ?: "-")
            }
        }
        setProductRarity(model.productRarity ?: "-")
        setPrice(model.price.toString())
        setImage(
            if (model.frontImageURL.isNullOrEmpty())
                model.productFirImageURL.toString()
            else
                model.frontImageURL.toString()
        )
    }

    fun setValue(model: SearchBidsDomainModel) {
        when (model.productType ?: model.productCategory) {
            ProductType.MAGIC_THE_GATHERING,
            ProductType.POKEMON,
            ProductType.YUGIOH,
            ProductCategory.CARDS -> {
                setType(Type.CARDS)
                setProductName(model.product_productName)
                setProductNumber(model.product_productNumber.toString() ?: "-")
                setEdition(model.edition?.title ?: "-")
                setLanguage(model.language?.abbreviatedName ?: "-")
                setConditionTitle(context.getConditionTitle(model.condition?.type!!) ?: "-")
                setConditionValue(model.condition?.abbreviatedName ?: "-")
            }
            ProductType.FUNKO, ProductCategory.TOYS -> {
                setType(Type.FUNKO)
                setProductName(model.product_productName ?: "-")
                setProductNumber(model.product_productNumber?.toString() ?: "-")
            }
            ProductType.SEALED_YUGIOH,
            ProductType.SEALED_POKEMON,
            ProductType.SEALED_MTG,
            ProductCategory.SEALED -> {
                setType(Type.SEALED)
                setProductName(model.product_productName ?: "-")
                setProductNumber(model.product_group ?: "-")
            }
        }
        setProductRarity(model.product_rarity ?: "-")
        setPrice(model.bidPrice.toString())
        setImage(model.product_firImageURL.toString())
    }

    fun setState(state: State) {
        mCurrentState = state
        when (state) {
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

    fun setLabelVisibility(isVisible: Boolean) {
        if (isVisible)
            mTvCardState.visible()
        else
            mTvCardState.gone()
    }

    fun setImage(url: String) {
        mIvMain.setProductImage(url)
    }

    fun setPrice(value: String) {
        mTvPrice.text = String.format(
            context.getString(R.string.text_price_value),
            value.isValidPrice()
        )
    }

    fun setProductNumber(value: String?) {
        mTvRow2Collum2.text = value ?: "-"
    }


    fun setProductName(value: String?) {
        mTvRow1Collum2.text = value ?: "-"
    }

    fun setLanguage(language: String) {
        mTvLanguage.text = language
    }

    fun setConditionTitle(value: String) {
        mTvConditionTitle.text = if (value.equals(ConditionType.UG.title, ignoreCase = true))
            context.getString(R.string.raw)
        else
            value
    }

    fun setConditionValue(value: String) {
        mTvConditionValue.text = value
    }

    fun setEdition(value: String) {
        mTvEdition.text = value
    }

    fun setProductRarity(value: String) {
        mTvProductRarity.text = value
    }

    enum class Type {
        FUNKO, SEALED, CARDS
    }

    enum class State {
        FOR_SALE, WANT, NORMAL, FAVORITE, OFFER, SOLD
    }
}
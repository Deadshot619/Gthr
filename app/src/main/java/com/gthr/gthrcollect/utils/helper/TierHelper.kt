package com.gthr.gthrcollect.utils.helper

import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.utils.enums.ProductCategory

private fun getCardTier(cost: Double): String {
    return if (cost >= 50.0) "1" else "0"
}

fun getTier(productModel: ProductDisplayModel, bidAmount: Double): String? {
    return if (productModel.productCategory == ProductCategory.CARDS)
        getCardTier(bidAmount)
    else
        productModel?.tier
}
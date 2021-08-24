package com.gthr.gthrcollect.utils

import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.enums.product

private val ListProductTypes = ProductType.values()
private val ListProductCategories = ProductCategory.values()

fun getProductType(rawProductType: String): ProductType? =
    ListProductTypes.find { it.title == rawProductType }

fun getProductCategory(productType: ProductType): ProductCategory? {
    product.onEach {
        if (it.value.contains(productType)) return it.key
    }
    return null
}
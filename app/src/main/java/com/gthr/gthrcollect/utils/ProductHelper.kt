package com.gthr.gthrcollect.utils

import com.gthr.gthrcollect.utils.enums.FeedType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.enums.product

private val ListFeedType = FeedType.values()
private val ListProductTypes = ProductType.values()
private val ListProductCategories = ProductCategory.values()

fun getFeedType(rawProductType: String): FeedType? =
    ListFeedType.find { it.title == rawProductType }

fun getProductType(rawProductType: String): ProductType? =
    ListProductTypes.find { it.title == rawProductType }

fun getProductCategory(productType: ProductType): ProductCategory? {
    product.onEach {
        if (it.value.contains(productType)) return it.key
    }
    return null
}

fun getProductTypeFromObjectId(objectId: String): ProductType {
    return when{
        //Since all sealed models are under 1 sealedModel, we can return any Sealed Type
        objectId.contains("sealedPokemon", ignoreCase = true) -> ProductType.SEALED_POKEMON
        objectId.contains("sealedYugioh", ignoreCase = true) -> ProductType.SEALED_YUGIOH
        objectId.contains("sealedMTG", ignoreCase = true) -> ProductType.SEALED_MTG
        objectId.contains(ProductType.POKEMON.title, ignoreCase = true) -> ProductType.POKEMON
        objectId.contains(ProductType.MAGIC_THE_GATHERING.title, ignoreCase = true) -> ProductType.MAGIC_THE_GATHERING
        objectId.contains(ProductType.YUGIOH.title, ignoreCase = true) -> ProductType.YUGIOH
        else -> ProductType.FUNKO
    }
}

fun getProductCategoryFromRaw(rawString: String): ProductCategory? =
    ListProductCategories.find { it.title.equals(rawString, ignoreCase = true) }

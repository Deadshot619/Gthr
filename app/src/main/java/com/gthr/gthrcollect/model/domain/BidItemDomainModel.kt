package com.gthr.gthrcollect.model.domain

import com.gthr.gthrcollect.utils.enums.EditionType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType


data class BidItemDomainModel(
    var bidPrice: String? = "",
    var creatorUID: String? = "",
    var itemObjectID: String? = "",
    var itemRefKey: String? = "",
    var productCategory: ProductCategory? = null,
    var productType: ProductType? = null,
    var totalCost: String? = "",
    var language: LanguageDomainModel? = null,
    var condition: ConditionDomainModel? = null,
    var edition: EditionType? = null
)
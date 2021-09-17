package com.gthr.gthrcollect.model.domain

import com.gthr.gthrcollect.utils.enums.EditionType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType

data class AskItemDomainModel(
    var refKey: String?,
    var creatorUID: String,
    var duration: String?,
    var askPrice: String?,
    var totalPayout: String?,
    var itemRefKey: String,
    var itemObjectID: String,
    var productType: ProductType?,
    var productCategory: ProductCategory?,
    var edition: EditionType?,
    var condition: ConditionDomainModel?,
    var language: LanguageDomainModel?,
    var returnName: String?,
    var returnAddressLine1: String?,
    var returnAddressLine2: String?,
    var returnCity: String?,
    var returnState: String?,
    var returnZipCode: String?,
    var returnCountry: String?,
    var frontImageURL: String?,
    var backImageURL: String?,
    var addresskey: Int?,
)

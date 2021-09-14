package com.gthr.gthrcollect.model.network.firebaserealtimedb

import com.gthr.gthrcollect.model.domain.ConditionDomainModel
import com.gthr.gthrcollect.model.domain.LanguageDomainModel

data class AskItemModel(
    var refKey: String?,
    var creatorUID: String,
    var duration: String?,
    var askPrice: String?,
    var totalPayout: String?,
    var itemRefKey: String,
    var itemObjectID: String,
    var productType: String?,
    var productCategory: String?,
    var edition: String?,
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
)
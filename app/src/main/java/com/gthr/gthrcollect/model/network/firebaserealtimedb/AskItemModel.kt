package com.gthr.gthrcollect.model.network.firebaserealtimedb

import com.google.firebase.database.PropertyName
import com.gthr.gthrcollect.model.domain.ConditionDomainModel
import com.gthr.gthrcollect.model.domain.LanguageDomainModel
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase

data class AskItemModel(
    @set:PropertyName(FirebaseRealtimeDatabase.REF_KEY)
    @get:PropertyName(FirebaseRealtimeDatabase.REF_KEY)
    var refKey: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.CREATOR_UID)
    @get:PropertyName(FirebaseRealtimeDatabase.CREATOR_UID)
    var creatorUID: String,
    @set:PropertyName(FirebaseRealtimeDatabase.DURATION)
    @get:PropertyName(FirebaseRealtimeDatabase.DURATION)
    var duration: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.ASK_PRICE)
    @get:PropertyName(FirebaseRealtimeDatabase.ASK_PRICE)
    var askPrice: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.TOTAL_PAYOUT)
    @get:PropertyName(FirebaseRealtimeDatabase.TOTAL_PAYOUT)
    var totalPayout: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.ITEM_REF_KEY)
    @get:PropertyName(FirebaseRealtimeDatabase.ITEM_REF_KEY)
    var itemRefKey: String,
    @set:PropertyName(FirebaseRealtimeDatabase.ITEM_OBJECT_ID)
    @get:PropertyName(FirebaseRealtimeDatabase.ITEM_OBJECT_ID)
    var itemObjectID: String,
    @set:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    @get:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    var productType: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.PRODUCT_CATEGORY)
    @get:PropertyName(FirebaseRealtimeDatabase.PRODUCT_CATEGORY)
    var productCategory: String?,
    @set:PropertyName(FirebaseRealtimeDatabase._EDITION)
    @get:PropertyName(FirebaseRealtimeDatabase._EDITION)
    var edition: String?,
    @set:PropertyName(FirebaseRealtimeDatabase._CONDITION)
    @get:PropertyName(FirebaseRealtimeDatabase._CONDITION)
    var condition: ConditionModel?,
    @set:PropertyName(FirebaseRealtimeDatabase._LANGUAGE)
    @get:PropertyName(FirebaseRealtimeDatabase._LANGUAGE)
    var language: LanguageModel?,
    @set:PropertyName(FirebaseRealtimeDatabase.RETURN_NAME)
    @get:PropertyName(FirebaseRealtimeDatabase.RETURN_NAME)
    var returnName: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.RETURN_ADDRESSLINE1)
    @get:PropertyName(FirebaseRealtimeDatabase.RETURN_ADDRESSLINE1)
    var returnAddressLine1: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.RETURN_ADDRESSLINE2)
    @get:PropertyName(FirebaseRealtimeDatabase.RETURN_ADDRESSLINE2)
    var returnAddressLine2: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.RETURN_CITY)
    @get:PropertyName(FirebaseRealtimeDatabase.RETURN_CITY)
    var returnCity: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.RETURN_STATE)
    @get:PropertyName(FirebaseRealtimeDatabase.RETURN_STATE)
    var returnState: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.RETURN_ZIPCODE)
    @get:PropertyName(FirebaseRealtimeDatabase.RETURN_ZIPCODE)
    var returnZipCode: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.RETURN_COUNTRY)
    @get:PropertyName(FirebaseRealtimeDatabase.RETURN_COUNTRY)
    var returnCountry: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.FRONT_IMAGE_URL)
    @get:PropertyName(FirebaseRealtimeDatabase.FRONT_IMAGE_URL)
    var frontImageURL: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.BACK_IMAGE_URL)
    @get:PropertyName(FirebaseRealtimeDatabase.BACK_IMAGE_URL)
    var backImageURL: String?,
    @set:PropertyName(FirebaseRealtimeDatabase.ADDRESS_KEY)
    @get:PropertyName(FirebaseRealtimeDatabase.ADDRESS_KEY)
    var addresskey: Int?

)
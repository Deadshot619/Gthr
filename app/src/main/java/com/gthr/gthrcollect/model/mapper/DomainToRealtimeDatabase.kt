package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.utils.getProductCategory
import com.gthr.gthrcollect.utils.getProductType

fun UserInfoDomainModel.toRealtimeDatabaseModel() = CollectionInfoModel(
    collectionDisplayName = displayName,
    about = bio,
    userRefKey = GthrCollect.prefs?.signedInUser?.uid!!,
    collectionRawName = displayName,
    buyList = null,
    favoriteCollectionList = null,
    favoriteProductList = null,
    followersList = null,
    sellList = null,
    collectionList = null,
)

fun CollectionItemDomainModel.toRealtimeDatabaseModel() = CollectionItemModel(
    id = id,
    itemRefKey = itemRefKey,
    marketCost = marketCost,
    objectID = objectID,
    askRefKey = askRefKey,
    backImageURL = backImageURL,
    frontImageURL = frontImageURL,
    edition = edition?.title,
    productType = productType?.title,
    language = language?.toRealtimeDatabaseModel(),
    condition = condition?.toRealtimeDatabaseModel()
)

fun LanguageDomainModel.toRealtimeDatabaseModel() = LanguageModel(
    key = key,
    displayName = displayName,
    abbreviatedName = abbreviatedName
)

fun ConditionDomainModel.toRealtimeDatabaseModel() = ConditionModel(
        key = key,
        displayName = displayName,
        type = type.title,
        abbreviatedName = abbreviatedName
)

fun AskItemDomainModel.toRealtimeDatabaseModel() = AskItemModel(
    refKey = refKey,
    creatorUID = creatorUID,
    duration = duration,
    askPrice = askPrice,
    totalPayout = totalPayout,
    itemRefKey = itemRefKey,
    itemObjectID = itemObjectID,
    productType = productType?.title,
    productCategory = getProductCategory(getProductType(productType?.title!!)!!)?.title,
    edition = edition?.title,
    condition = condition?.toRealtimeDatabaseModel(),
    language = language?.toRealtimeDatabaseModel(),
    returnName = returnName,
    returnAddressLine1 = returnAddressLine1,
    returnAddressLine2 = returnAddressLine2,
    returnCity = returnCity,
    returnState = returnState,
    returnZipCode = returnZipCode,
    returnCountry = returnCountry,
    frontImageURL = frontImageURL,
    backImageURL = backImageURL,
    addresskey=addresskey
)

fun BidItemDomainModel.toRealtimeDatabaseModel() = BidItemModel(
    bidPrice = bidPrice,
    creatorUID = creatorUID,
    itemObjectID = itemObjectID,
    itemRefKey = itemRefKey,
    productCategory = getProductCategory(getProductType(productType?.title!!)!!)?.title,
    productType = productType?.title,
    totalCost = totalCost,
    edition = edition?.title,
    condition = condition?.toRealtimeDatabaseModel(),
    language = language?.toRealtimeDatabaseModel(),
)

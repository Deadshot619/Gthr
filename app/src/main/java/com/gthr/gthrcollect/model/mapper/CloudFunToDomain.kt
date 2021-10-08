package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.model.network.cloudfunction.ForSaleItemModel
import com.gthr.gthrcollect.model.network.cloudfunction.SearchBidsModel
import com.gthr.gthrcollect.model.network.cloudfunction.SearchProductModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.FeedModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.ReceiptModel
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.getFeedType
import com.gthr.gthrcollect.utils.getProductCategory
import com.gthr.gthrcollect.utils.getProductCategoryFromRaw
import com.gthr.gthrcollect.utils.getProductType
import com.gthr.gthrcollect.utils.helper.*

fun SearchProductModel.toSearchProductDomainModel() =
    SearchProductDomainModel(
        title = title,
        productType = getProductType(productType!!),
        objectID = objectID,
        numberOfFavorites = numberOfFavorites,

    )
fun ForSaleItemModel.toDomainModel()=ForSaleItemDomainModel(
        creatorProfileURL = creatorProfileURL,
        productProductName = productProductName,
        edition = edition,
        productFirebaseRef = productFirebaseRef,
        language = language?.toLanguageDomainModel(),
        collectionFirebaseRef = collectionFirebaseRef,
        productCategory = getProductCategoryFromRaw(productCategory.toString()),
        collectionId = collectionId,
        createdAt = createdAt,
        collectionUserRefKey = collectionUserRefKey,
        itemObjectID = itemObjectID,
        itemRefKey = itemRefKey,
        price = price,
        productId = productId,
        askRefKey = askRefKey,
  productProductNumber = productProductNumber,
  id = id,
  productGroup=productGroup,
  backImageURL=backImageURL,
  productType=getProductType(productType.toString()),
        updatedAt = updatedAt,
        productFirImageURL = productFirImageURL,
        productRefId = productRefId,
        collectionRefId = collectionRefId,
        productNumberOfFavorites = productNumberOfFavorites,
        collectionCollectionDisplayName = collectionCollectionDisplayName,
        productRarity = productRarity,
        collectionProfileImageURL = collectionProfileImageURL,
        productProdObjectID = productProdObjectID,
        creatorUID = creatorUID,
        condition = condition?.toConditionDomainModel(),
        frontImageURL = frontImageURL,
        firebaseRef = firebaseRef,
        collectionCollectionRawName = collectionCollectionRawName
)

fun SearchBidsModel.toSearchBidsDomainModel() = SearchBidsDomainModel(
    bidPrice = bidPrice,
    bidRefKey = bidRefKey,
    collectionRefId = collectionRefId,
    collection_collectionDisplayName = collection_collectionDisplayName,
    collection_collectionRawName = collection_collectionRawName,
    collection_firebaseRef = collection_firebaseRef,
    collection_id = collection_id,
    collection_profileImageURL = collection_profileImageURL,
    collection_userRefKey = collection_userRefKey,
    condition = condition?.toConditionDomainModel(),
    createdAt = createdAt,
    creatorUID = creatorUID,
    edition = edition?.let { getEditionTypeFromRowType(it) },
    firebaseRef = firebaseRef,
    id = id,
    itemObjectID = itemObjectID,
    itemRefKey = itemRefKey,
    language = language?.toLanguageDomainModel(),
    productCategory = getProductCategoryFromRaw(productCategory.toString()),
    productRefId = productRefId,
    productType = getProductType(productType.toString()),
    product_firImageURL = product_firImageURL,
    product_firebaseRef = product_firebaseRef,
    product_group = product_group,
    product_id = product_id,
    product_numberOfFavorites = product_numberOfFavorites,
    product_prodObjectID = product_prodObjectID,
    product_productName = product_productName,
    product_productNumber = product_productNumber,
    product_rarity = product_rarity,
    updatedAt = updatedAt,
)

fun FeedModel.toFeedDomainModel() = FeedDomainModel(
    id = id?:-1,
    collectionRefId = collectionRefId?:"",
    bidPrice = bidPrice?:-1,
    price = price?:0,
    product_numberOfFavorites = product_numberOfFavorites?:-1,
    productRefId = productRefId?:-1,

    askRefKey = askRefKey?:"",
    backImageURL = backImageURL?:"",
    condition = condition?.toConditionDomainModel(),
    language = language?.toLanguageDomainModel(),
    createdAt = createdAt?:"",
    creatorProfileURL = creatorProfileURL?:"",
    creatorUID = creatorUID?:"",
    edition = edition?:"",
    collection_firebaseRef = collection_firebaseRef?:"",
    itemRefKey =  itemRefKey?:"",
    itemObjectID =  itemObjectID?:"",
    frontImageURL =  frontImageURL?:"",

    collection_collectionDisplayName =  collection_collectionDisplayName?:"",
    collection_collectionRawName = collection_collectionRawName?:"",
    collection_id = collection_id?:"",
    collection_profileImageURL = collection_profileImageURL?:"",
    collection_userRefKey = collection_userRefKey?:"",
    collectionDisplayName = collection_collectionDisplayName?:"",
    collectionID = collectionID?:"",
    collectionRawName = collectionRawName?:"",
    cretorUID = cretorUID?:"",
    firebaseRef = firebaseRef?:"",
    product_firebaseRef = product_firebaseRef?:"",
    product_firImageURL = product_firImageURL?:"",
    product_group = product_group?:"",
    product_id = product_id?:"",
    product_prodObjectID = product_prodObjectID?:"",
    product_productCategory = product_productCategory?:"",
    product_productName = product_productName?:"",
    product_productNumber = product_productNumber?:"",
    product_rarity = product_rarity?:"",
    profileImageURL = profileImageURL?:"",
    updatedAt = updatedAt?:"",
    userRefKey = userRefKey?:"",
    product_productType = product_productType,
    productCategory = if (!productCategory.isNullOrEmpty()) getProductCategoryFromRaw(productCategory) else getProductCategory(getProductType(product_productType.toString())!!),
    productType = if(productType!=null&&productType.isNotEmpty()) getProductType(productType) else null,
    feedType = getFeedType(feedType?:""),

)



fun ReceiptModel.toReceiptDomainModel() =
    ReceiptDomainModel(
        date = date,
        appFee = appFee.toString(),
        objectID = objectID,
        shippingTierKey = shippingTierKey.toString(),
        buyerCharge = buyerCharge,
        edition = getEditionDomainModelFromKey(if (edition.isNullOrEmpty()) 0 else edition?.toInt()!! ,getProductType(productType.toString())?:ProductType.POKEMON),
        buyerShippingState = buyerShippingState,
        buyerShippingCity = buyerShippingCity,
        itemRefKey = itemRefKey,
        sellerUID = sellerUID,
        sellerPayout = sellerPayout,
        trackingNumber = trackingNumber,
        lang = getLanguageDomainModelFromKey(if (lang.isNullOrEmpty()) 0 else lang?.toInt()!! ,getProductType(productType.toString())?:ProductType.POKEMON),
        sellerShippingAddressLine1 = sellerShippingAddressLine1,
        sellerShippingCity = sellerShippingCity,
        sellerShippingAddressLine2 = sellerShippingAddressLine2,
        trackingLink = trackingLink,
        productType = getProductType(productType.toString()),
        sellerShippingZipCode = sellerShippingZipCode,
        buyerShippingAddressLine2 = buyerShippingAddressLine2,
        buyerShippingAddressLine1 = buyerShippingAddressLine1,
        buyerShippingCountry = buyerShippingCountry,
        buyerShippingName = buyerShippingName,
        buyerShippingZipCode = buyerShippingZipCode,
        saleID = saleID,
        sellerShippingCountry = sellerShippingCountry,
        sellerShippingName = sellerShippingName,
        condition = getCondition(if(condition.isNullOrEmpty()) -1 else condition?.toInt()!! ),
        buyerUID = buyerUID,
        paymentID = paymentID,
        sellerShippingState = sellerShippingState,
        orderStatus = getOrderStatusFromRaw(orderStatus?:"")
        )
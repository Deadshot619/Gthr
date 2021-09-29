package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.model.domain.ForSaleItemDomainModel
import com.gthr.gthrcollect.model.domain.SearchBidsDomainModel
import com.gthr.gthrcollect.model.domain.SearchProductDomainModel
import com.gthr.gthrcollect.model.network.cloudfunction.ForSaleItemModel
import com.gthr.gthrcollect.model.network.cloudfunction.SearchBidsModel
import com.gthr.gthrcollect.model.network.cloudfunction.SearchProductModel
import com.gthr.gthrcollect.utils.getProductCategoryFromRaw
import com.gthr.gthrcollect.utils.getProductType
import com.gthr.gthrcollect.utils.helper.getEditionTypeFromRowType

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
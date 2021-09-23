package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.model.domain.ForSaleItemDomainModel
import com.gthr.gthrcollect.model.domain.SearchProductDomainModel
import com.gthr.gthrcollect.model.network.cloudfunction.ForSaleItemModel
import com.gthr.gthrcollect.model.network.cloudfunction.SearchProductModel
import com.gthr.gthrcollect.utils.getProductCategoryFromRaw
import com.gthr.gthrcollect.utils.getProductType

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

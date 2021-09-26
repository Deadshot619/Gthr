package com.gthr.gthrcollect.model.domain

import android.os.Parcelable
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.getProductCategory
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForSaleItemDomainModel(
    var creatorProfileURL: String? = null,
    var productProductName: String? = null,
    var edition: String? = null,
    var productFirebaseRef: String? = null,
    var language: LanguageDomainModel? = null,
    var collectionFirebaseRef: String? = null,
    var productCategory: ProductCategory? = null,
    var collectionId: String? = null,
    var createdAt: String? = null,
    var collectionUserRefKey: String? = null,
    var itemObjectID: String? = null,
    var itemRefKey: String? = null,
    var price: Double? = null,
    var productId: String? = null,
    var askRefKey: String? = null,
    var productProductNumber: String? = null,
    var id: Int? = null,
    var productGroup: String? = null,
    var backImageURL: String? = null,
    var productType: ProductType? = null,
    var updatedAt: String? = null,
    var productFirImageURL: String? = null,
    var productRefId: String? = null,
    var collectionRefId: String? = null,
    var productNumberOfFavorites: Int? = null,
    var collectionCollectionDisplayName: String? = null,
    var productRarity: String? = null,
    var collectionProfileImageURL: String? = null,
    var productProdObjectID: String? = null,
    var creatorUID: String? = null,
    var condition: ConditionDomainModel? = null,
    var frontImageURL: String? = null,
    var firebaseRef: String? = null,
    var collectionCollectionRawName: String? = null
) : Parcelable {

    constructor(model: SealedDomainModel, collectionItem: CollectionItemDomainModel, price: Double) : this(
        price = price,
        itemObjectID = model.objectID,
        language = collectionItem.language,
        condition = collectionItem.condition,
        edition = collectionItem.edition?.title,
        productFirImageURL = if (collectionItem.frontImageURL.isNullOrEmpty()) model.firImageURL else collectionItem.frontImageURL,
        frontImageURL = if (collectionItem.frontImageURL.isNullOrEmpty()) model.firImageURL else collectionItem.frontImageURL,
        backImageURL = collectionItem.backImageURL,
        askRefKey = collectionItem.askRefKey,

        productGroup = null,
        productProductNumber = null,

        productProductName = model.name,
        productFirebaseRef = model.refKey,
        productCategory = getProductCategory(model.productType),
        productType = model.productType,
        productNumberOfFavorites = model.numberOfFavorites,
        productRarity = model.rarity,
        productProdObjectID = model.objectID,
    )

    constructor(model: PokemonDomainModel, collectionItem: CollectionItemDomainModel, price: Double) : this(
        price = price,
        itemObjectID = model.objectID,
        productFirImageURL = if (collectionItem.frontImageURL.isNullOrEmpty()) model.firImageURL else collectionItem.frontImageURL,
        frontImageURL = if (collectionItem.frontImageURL.isNullOrEmpty()) model.firImageURL else collectionItem.frontImageURL,
        backImageURL = collectionItem.backImageURL,
        language = collectionItem.language,
        condition = collectionItem.condition,
        edition = collectionItem.edition?.title,
        askRefKey = collectionItem.askRefKey,

        productGroup = null,
        productProductNumber = model.number,
        productProductName = model.name,
        productFirebaseRef = model.refKey,
        productCategory = getProductCategory(model.productType),
        productType = model.productType,
        productNumberOfFavorites = model.numberOfFavorites,
        productRarity = model.rarity,
        productProdObjectID = model.objectID,
    )

    constructor(model: MTGDomainModel, collectionItem: CollectionItemDomainModel, price: Double) : this(
        price = price,
        itemObjectID = model.objectID,
        productFirImageURL = if (collectionItem.frontImageURL.isNullOrEmpty()) model.firImageURL else collectionItem.frontImageURL,
        frontImageURL = if (collectionItem.frontImageURL.isNullOrEmpty()) model.firImageURL else collectionItem.frontImageURL,
        backImageURL = collectionItem.backImageURL,
        language = collectionItem.language,
        condition = collectionItem.condition,
        edition = collectionItem.edition?.title,
        askRefKey = collectionItem.askRefKey,


        productGroup = null,
        productProductNumber = model.collectorNumber,
        productProductName = model.name,
        productFirebaseRef = model.refKey,
        productCategory = getProductCategory(model.productType),
        productType = model.productType,
        productNumberOfFavorites = model.numberOfFavorites,
        productRarity = model.rarity,
        productProdObjectID = model.objectID,
    )

    constructor(model: YugiohDomainModel, collectionItem: CollectionItemDomainModel, price: Double) : this(
        price = price,
        itemObjectID = model.objectID,
        productFirImageURL = if (collectionItem.frontImageURL.isNullOrEmpty()) model.firImageURL else collectionItem.frontImageURL,
        frontImageURL = if (collectionItem.frontImageURL.isNullOrEmpty()) model.firImageURL else collectionItem.frontImageURL,
        backImageURL = collectionItem.backImageURL,
        language = collectionItem.language,
        condition = collectionItem.condition,
        edition = collectionItem.edition?.title,
        askRefKey = collectionItem.askRefKey,


        productGroup = null,
        productProductNumber = model.number,
        productProductName = model.name,
        productFirebaseRef = model.refKey,
        productCategory = getProductCategory(model.productType),
        productType = model.productType,
        productNumberOfFavorites = model.numberOfFavorites,
        productRarity = model.rarity,
        productProdObjectID = model.objectID,
    )

    constructor(model: FunkoDomainModel, collectionItem: CollectionItemDomainModel, price: Double) : this(
        price = price,
        itemObjectID = model.objectID,
        productFirImageURL = if (collectionItem.frontImageURL.isNullOrEmpty()) model.firImageURL else collectionItem.frontImageURL,
        frontImageURL = if (collectionItem.frontImageURL.isNullOrEmpty()) model.firImageURL else collectionItem.frontImageURL,
        backImageURL = collectionItem.backImageURL,
        language = collectionItem.language,
        condition = collectionItem.condition,
        edition = collectionItem.edition?.title,
        askRefKey = collectionItem.askRefKey,


        productGroup = null,
        productProductNumber = model.itemNumber,
        productProductName = model.name,
        productFirebaseRef = model.refKey,
        productCategory = getProductCategory(model.productType),
        productType = model.productType,
        productNumberOfFavorites = model.numberOfFavorites,
        productRarity = ProductType.FUNKO.title.capitalize(),
        productProdObjectID = model.objectID,
    )
}

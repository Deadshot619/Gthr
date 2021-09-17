package com.gthr.gthrcollect.model.domain

import android.os.Parcelable
import com.gthr.gthrcollect.utils.enums.ProductType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ForSaleItemDomainModel(
	val creatorProfileURL: String? = null,
	val productProductName: String? = null,
	val edition: String? = null,
	val productFirebaseRef: String? = null,
	val language: String? = null,
	val collectionFirebaseRef: String? = null,
	val productCategory: String? = null,
	val collectionId: String? = null,
	val createdAt: String? = null,
	val collectionUserRefKey: String? = null,
	val itemObjectID: String? = null,
	val itemRefKey: String? = null,
	val price: Int? = null,
	val productId: String? = null,
	val askRefKey: String? = null,
	val productProductNumber: String? = null,
	val id: Int? = null,
	val productGroup: String? = null,
	val backImageURL: String? = null,
	val productType: ProductType? = null,
	val updatedAt: String? = null,
	val productFirImageURL: String? = null,
	val productRefId: String? = null,
	val collectionRefId: String? = null,
	val productNumberOfFavorites: Int? = null,
	val collectionCollectionDisplayName: String? = null,
	val productRarity: String? = null,
	val collectionProfileImageURL: String? = null,
	val productProdObjectID: String? = null,
	val creatorUID: String? = null,
	val condition: String? = null,
	val frontImageURL: String? = null,
	val firebaseRef: String? = null,
	val collectionCollectionRawName: String? = null
):Parcelable

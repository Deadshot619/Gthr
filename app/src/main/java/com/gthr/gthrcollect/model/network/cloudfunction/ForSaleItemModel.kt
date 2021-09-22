package com.gthr.gthrcollect.model.network.cloudfunction

import com.google.gson.annotations.SerializedName

data class ForSaleItemModel(

	@SerializedName("creatorProfileURL")
	val creatorProfileURL: String? = null,

	@SerializedName("product_productName")
	val productProductName: String? = null,

	@SerializedName("edition")
	val edition: String? = null,

	@SerializedName("product_firebaseRef")
	val productFirebaseRef: String? = null,

	@SerializedName("language")
//	val language: LanguageModel? = null,
	val language: String? = null,

	@SerializedName("collection_firebaseRef")
	val collectionFirebaseRef: String? = null,

	@SerializedName("productCategory")
	val productCategory: String? = null,

	@SerializedName("collection_id")
	val collectionId: String? = null,

	@SerializedName("createdAt")
	val createdAt: String? = null,

	@SerializedName("collection_userRefKey")
	val collectionUserRefKey: String? = null,

	@SerializedName("itemObjectID")
	val itemObjectID: String? = null,

	@SerializedName("itemRefKey")
	val itemRefKey: String? = null,

	@SerializedName("price")
	val price: Int? = null,

	@SerializedName("product_id")
	val productId: String? = null,

	@SerializedName("askRefKey")
	val askRefKey: String? = null,

	@SerializedName("product_productNumber")
	val productProductNumber: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("product_group")
	val productGroup: String? = null,

	@SerializedName("backImageURL")
	val backImageURL: String? = null,

	@SerializedName("productType")
	val productType: String? = null,

	@SerializedName("updatedAt")
	val updatedAt: String? = null,

	@SerializedName("product_firImageURL")
	val productFirImageURL: String? = null,

	@SerializedName("productRefId")
	val productRefId: String? = null,

	@SerializedName("collectionRefId")
	val collectionRefId: String? = null,

	@SerializedName("product_numberOfFavorites")
	val productNumberOfFavorites: Int? = null,

	@SerializedName("collection_collectionDisplayName")
	val collectionCollectionDisplayName: String? = null,

	@SerializedName("product_rarity")
	val productRarity: String? = null,

	@SerializedName("collection_profileImageURL")
	val collectionProfileImageURL: String? = null,

	@SerializedName("product_prodObjectID")
	val productProdObjectID: String? = null,

	@SerializedName("creatorUID")
	val creatorUID: String? = null,

	@SerializedName("condition")
//	val condition: ConditionModel? = null,
	val condition: String? = null,

	@SerializedName("frontImageURL")
	val frontImageURL: String? = null,

	@SerializedName("firebaseRef")
	val firebaseRef: String? = null,

	@SerializedName("collection_collectionRawName")
	val collectionCollectionRawName: String? = null
)

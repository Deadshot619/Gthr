package com.gthr.gthrcollect.model.network.cloudfunction

import com.google.gson.annotations.SerializedName

data class ForSaleItemModel(

	@field:SerializedName("creatorProfileURL")
	val creatorProfileURL: String? = null,

	@field:SerializedName("product_productName")
	val productProductName: String? = null,

	@field:SerializedName("edition")
	val edition: String? = null,

	@field:SerializedName("product_firebaseRef")
	val productFirebaseRef: String? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("collection_firebaseRef")
	val collectionFirebaseRef: String? = null,

	@field:SerializedName("productCategory")
	val productCategory: String? = null,

	@field:SerializedName("collection_id")
	val collectionId: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("collection_userRefKey")
	val collectionUserRefKey: String? = null,

	@field:SerializedName("itemObjectID")
	val itemObjectID: String? = null,

	@field:SerializedName("itemRefKey")
	val itemRefKey: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("product_id")
	val productId: String? = null,

	@field:SerializedName("askRefKey")
	val askRefKey: String? = null,

	@field:SerializedName("product_productNumber")
	val productProductNumber: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("product_group")
	val productGroup: String? = null,

	@field:SerializedName("backImageURL")
	val backImageURL: String? = null,

	@field:SerializedName("productType")
	val productType: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("product_firImageURL")
	val productFirImageURL: String? = null,

	@field:SerializedName("productRefId")
	val productRefId: String? = null,

	@field:SerializedName("collectionRefId")
	val collectionRefId: String? = null,

	@field:SerializedName("product_numberOfFavorites")
	val productNumberOfFavorites: Int? = null,

	@field:SerializedName("collection_collectionDisplayName")
	val collectionCollectionDisplayName: String? = null,

	@field:SerializedName("product_rarity")
	val productRarity: String? = null,

	@field:SerializedName("collection_profileImageURL")
	val collectionProfileImageURL: String? = null,

	@field:SerializedName("product_prodObjectID")
	val productProdObjectID: String? = null,

	@field:SerializedName("creatorUID")
	val creatorUID: String? = null,

	@field:SerializedName("condition")
	val condition: String? = null,

	@field:SerializedName("frontImageURL")
	val frontImageURL: String? = null,

	@field:SerializedName("firebaseRef")
	val firebaseRef: String? = null,

	@field:SerializedName("collection_collectionRawName")
	val collectionCollectionRawName: String? = null
)

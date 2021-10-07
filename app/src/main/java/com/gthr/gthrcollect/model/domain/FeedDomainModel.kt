package com.gthr.gthrcollect.model.domain

import com.gthr.gthrcollect.utils.enums.AdapterViewType
import com.gthr.gthrcollect.utils.enums.FeedType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType

data class FeedDomainModel(
    //ask
    val askRefKey: String? = null,
    val backImageURL: String? = null,
    val collectionRefId: String? = "",
    val condition: ConditionDomainModel? = null,
    val language: LanguageDomainModel? = null,
    val createdAt: String? = null,
    val creatorProfileURL: String? = null,
    val creatorUID: String? = null,
    val edition: String? = null,
    val firebaseRef: String? = null,
    val frontImageURL: String? = null,
    val id: Int? = -1,
    val itemObjectID: String? = null,
    val itemRefKey: String? = null,
    val product_productType: String? = null,

    val price: Int? = 0,

    val productRefId: Int? = -1,

    val updatedAt: String? = null,

    //Bid
    val collection_id : String? = "",
    val collection_firebaseRef : String? = "",
    val collection_collectionDisplayName : String? = "",
    val collection_collectionRawName : String? = "",
    val collection_profileImageURL : String? = "",
    val collection_userRefKey : String? = "",
    val product_id : String? = "",
    val product_firebaseRef : String? = "",
    val product_firImageURL : String? = "",
    val product_productNumber : String? = "",
    val product_productName : String? = "",
    val product_rarity : String? = "",
    val product_prodObjectID : String? = "",
    val product_group : String? = "",
    val product_numberOfFavorites : Int? = -1,
    val bidPrice : Int? = -1,

    //Collection
    val product_productCategory : String? = "",
    val profileImageURL : String? = "",
    val collectionDisplayName : String? = "",
    val collectionRawName : String? = "",
    val userRefKey : String? = "",
    val cretorUID : String? = "",
    val collectionID : String? = "",

    val productType: ProductType? = null,
    val feedType : FeedType? = FeedType.ASK,
    val productCategory: ProductCategory? = null,
    val viewType: AdapterViewType = AdapterViewType.VIEW_TYPE_ITEM

) {
}
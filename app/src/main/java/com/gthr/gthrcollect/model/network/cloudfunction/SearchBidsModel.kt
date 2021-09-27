package com.gthr.gthrcollect.model.network.cloudfunction

import com.gthr.gthrcollect.model.network.firebaserealtimedb.ConditionModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.LanguageModel

data class SearchBidsModel(
    val bidPrice: Double? = null,
    val bidRefKey: String? = null,
    val collectionRefId: Int? = null,
    val collection_collectionDisplayName: String? = null,
    val collection_collectionRawName: String? = null,
    val collection_firebaseRef: String? = null,
    val collection_id: Int? = null,
    val collection_profileImageURL: String? = null,
    val collection_userRefKey: String? = null,
    val condition: ConditionModel? = null,
    val createdAt: String? = null,
    val creatorUID: String? = null,
    val edition: String? = null,
    val firebaseRef: String? = null,
    val id: Int? = null,
    val itemObjectID: String? = null,
    val itemRefKey: String? = null,
    val language: LanguageModel? = null,
    val productCategory: String? = null,
    val productRefId: Int? = null,
    val productType: String? = null,
    val product_firImageURL: String? = null,
    val product_firebaseRef: String? = null,
    val product_group: String? = null,
    val product_id: Int? = null,
    val product_numberOfFavorites: Int? = null,
    val product_prodObjectID: String? = null,
    val product_productName: String? = null,
    val product_productNumber: Int? = null,
    val product_rarity: String? = null,
    val updatedAt: String? = null
)

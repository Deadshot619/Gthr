package com.gthr.gthrcollect.model.domain

import android.os.Parcelable
import com.gthr.gthrcollect.utils.enums.EditionType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchBidsDomainModel(
    val bidPrice: Double? = null,
    val bidRefKey: String? = null,
    val collectionRefId: Int? = null,
    val collection_collectionDisplayName: String? = null,
    val collection_collectionRawName: String? = null,
    val collection_firebaseRef: String? = null,
    val collection_id: Int? = null,
    val collection_profileImageURL: String? = null,
    val collection_userRefKey: String? = null,
    val condition: ConditionDomainModel? = null,
    val createdAt: String? = null,
    val creatorUID: String? = null,
    val edition: EditionType? = null,
    val firebaseRef: String? = null,
    val id: Int? = null,
    val itemObjectID: String? = null,
    val itemRefKey: String? = null,
    val language: LanguageDomainModel? = null,
    val productCategory: ProductCategory? = null,
    val productRefId: Int? = null,
    val productType: ProductType? = null,
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
) : Parcelable
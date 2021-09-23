package com.gthr.gthrcollect.model.domain

import com.gthr.gthrcollect.utils.enums.ProductType

data class SealedDomainModel(
        val firImageURL: String,
        val cardText: String,
        val highestBidCost: Int,
        val highestBidID: String,
        val imageID: String,
        val imageURL: String,
        val lowestAskCost: Double,
        val lowestAskID: String,
//    val marketPrice: String,
        val name: String,
        val numberOfFavorites: Int,
        val objectID: String,
        val productType: ProductType,
        val rarity: String,
        val `set`: String,
        val tier: Int,
        val description: String,
    val refKey: String
)
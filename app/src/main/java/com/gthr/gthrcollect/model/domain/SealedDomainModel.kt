package com.gthr.gthrcollect.model.domain


import kotlinx.serialization.Serializable

@Serializable
data class SealedDomainModel(
    val cardText: String,
    val highestBidCost: Double,
    val highestBidID: String,
    val imageID: String,
    val imageURL: String,
    val lowestAskCost: Double,
    val lowestAskID: String,
    val marketPrice: String,
    val name: String,
    val numberOfFavorites: Int,
    val objectID: String,
    val productType: String,
    val rarity: String,
    val `set`: String,
    val tier: Int
)
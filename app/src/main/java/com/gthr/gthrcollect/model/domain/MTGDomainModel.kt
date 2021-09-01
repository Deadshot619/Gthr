package com.gthr.gthrcollect.model.domain

import com.gthr.gthrcollect.utils.enums.ProductType

data class MTGDomainModel(
    val firImageURL: String,
    val cardBackId: String,
    val colors: String,
    val flavorText: String,
    val foil: Boolean,
    val highestBidCost: Int,
    val highestBidID: String,
    val id: String,
    val imageID: String,
    val imageUris: String,
    val lang: String,
    val lowestAskCost: Int,
    val lowestAskID: String,
    val mtgoFoilId: String,
    val mtgoId: String,
    val name: String,
    val numberOfFavorites: Int,
    val objectID: String,
    val oracleId: String,
    val productType: ProductType,
    val rarity: String,
    val releasedAt: String,
    val setName: String,
    val setType: String,
    val typeLine: String,
    val refKey: String
)
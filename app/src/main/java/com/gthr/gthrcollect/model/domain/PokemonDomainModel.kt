package com.gthr.gthrcollect.model.domain

import com.gthr.gthrcollect.utils.enums.ProductType

data class PokemonDomainModel(
    val firImageURL: String,
    val highestBid: Int,
    val japanese: Int,
    val english: Int,
    val cardType: String,
    val highestBidID: String,
    val hp: String,
    val imageID: String,
    val imageURL: String,
    val japaneseNumber: String,
    val japaneseSet: String,
    val lowestAskCost: Int,
    val lowestAskID: String,
    val name: String,
    val noLanguagenoEdition: Int,
    val number: String,
    val numberOfFavorites: Int,
    val objectID: String,
    val priceFoil: String,
    val priceNonFoil: String,
    val productType: ProductType,
    val rarity: String,
    val resistance: String,
    val retreatCost: String,
    val `set`: String,
    val stage: String,
    val weakness: String,
    val refKey: String
)
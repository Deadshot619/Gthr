package com.gthr.gthrcollect.model.domain


import kotlinx.serialization.Serializable

@Serializable
data class PokemonDomainModel(
    val japanese: Int,
    val english: Int,
    val cardType: String,
    val highestBidCost: Double,
    val highestBidID: String,
    val hp: Int,
    val imageID: String,
    val imageURL: String,
    val japaneseNumber: String,
    val japaneseSet: String,
    val lowestAskCost: Double,
    val lowestAskID: String,
    val name: String,
    val noLanguagenoEdition: Int,
    val number: Int,
    val numberOfFavorites: Int,
    val objectID: String,
    val priceFoil: String,
    val priceNonFoil: String,
    val productType: String,
    val rarity: String,
    val resistance: String,
    val retreatCost: String,
    val `set`: String,
    val stage: String,
    val weakness: String
)
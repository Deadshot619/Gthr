package com.gthr.gthrcollect.model.domain


import kotlinx.serialization.Serializable

@Serializable
data class YugiohDomainModel(
    val asianEnglish: Int,
    val asianEnglishOG: Int,
    val cardType: String,
    val detailPrice: Double,
    val english: Int,
    val englishOG: Int,
    val euroEnglish: Int,
    val firstDescription: String,
    val french: Int,
    val frenchCanadian: Int,
    val frenchOG: Int,
    val german: Int,
    val germanOG: Int,
    val highestBidCost: Double,
    val highestBidID: String,
    val imageID: String,
    val imageURL: String,
    val italian: Int,
    val italianOG: Int,
    val japanese: Int,
    val korean: Int,
    val koreanOG: Int,
    val lowestAskCost: Double,
    val lowestAskID: String,
    val name: String,
    val number: String,
    val numberOfFavorites: Int,
    val objectID: String,
    val oceanicEnglish: Int,
    val portuguese: Int,
    val portugueseOG: Int,
    val productType: String,
    val rarity: String,
    val `set`: String,
    val spanish: Int,
    val spanishOG: Int,
    val stats: String
)
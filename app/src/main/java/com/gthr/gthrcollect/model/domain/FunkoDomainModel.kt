package com.gthr.gthrcollect.model.domain

data class FunkoDomainModel(
    val category: String,
    val exclusivity: String,
    val funkoType: String,
    val highestBidCost: Double,
    val highestBidID: String,
    val imageURL: String,
    val itemNumber: Int,
    val license: String,
    val lowestAskCost: Double,
    val lowestAskID: String,
    val marketValue: String,
    val name: String,
    val numberOfFavorites: Int,
    val objectID: String,
    val productType: String,
    val releaseDate: Int,
    val status: String,
    val tier: Int,
    val refKey: String
)
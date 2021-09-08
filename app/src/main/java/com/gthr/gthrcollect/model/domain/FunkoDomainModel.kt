package com.gthr.gthrcollect.model.domain

import com.gthr.gthrcollect.utils.enums.ProductType

data class FunkoDomainModel(
    val exclusive: String,
    val funkoID: String,
    val category: String,
    val funkoType: String,
    val highestBidCost: Int,
    val highestBidID: String,
    val imageURL: String,
    val itemNumber: String,
    val license: String,
    val lowestAskCost: Int,
    val lowestAskID: String,
//    val marketValue: String,
    val name: String,
    val numberOfFavorites: Int,
    val objectID: String,
    val productType: ProductType,
    val releaseDate: String,
    val status: String,
    val tier: String,
    val refKey: String,
    var exclusivity: String,
    var firImageURL: String,
    var imageID: String
)
package com.gthr.gthrcollect.model.domain

data class OffersDomainModel(
    val id:Int,
    val name: String?,
    val image_URL: String,
    val model: String,
    val price: String,
    val type: String,
    val desc: String
)
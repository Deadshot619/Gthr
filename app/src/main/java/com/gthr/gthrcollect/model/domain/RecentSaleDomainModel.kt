package com.gthr.gthrcollect.model.domain

data class RecentSaleDomainModel(
    val key : String,
    val condition : Int,
    val date : String,
    val edition :  Int,
    val language : Int,
    val objectId : String,
    val price : String,
    val customization : String,
)


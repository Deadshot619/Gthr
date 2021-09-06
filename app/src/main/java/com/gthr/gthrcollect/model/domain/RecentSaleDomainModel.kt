package com.gthr.gthrcollect.model.domain

data class RecentSaleDomainModel(
    val key : String,
    val condition : String,
    val date : String,
    val edition : String,
    val language : Int,
    val objectId : String,
    val price : String,
    val customization : String,
)


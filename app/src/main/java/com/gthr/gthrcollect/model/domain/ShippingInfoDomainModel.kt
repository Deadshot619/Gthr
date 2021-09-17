package com.gthr.gthrcollect.model.domain

data class ShippingInfoDomainModel(
    var billing: Long,
    var frontEndShippingProcessing: String,
    var service: String,
    var refKey: String?,
    val tierLevel : Int? = null
)

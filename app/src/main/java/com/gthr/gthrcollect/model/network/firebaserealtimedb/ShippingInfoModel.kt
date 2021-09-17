package com.gthr.gthrcollect.model.network.firebaserealtimedb

data class ShippingInfoModel(
    var billing: Long = -1,
    var frontEndShippingProcessing: String = "",
    var service: String = "",
    var refKey: String? = "",
    val tierLevel : Int? = null
)

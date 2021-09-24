package com.gthr.gthrcollect.model.domain

import java.util.*

data class SaleHistoryDomainModel(
    var refKey: String? = null,
    var buyerID: String? = null,
    var sellerID: String? = null,
    var price: Double? = null,
    var objectID: String? = null,
    var edition: Int? = null,
    var language: Int? = null,
    var condition: Int? = null,
    var saleDate: Date? = null,
    var firURLImage: String? = null,
)

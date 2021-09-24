package com.gthr.gthrcollect.model.network.firebaserealtimedb

import com.google.firebase.database.PropertyName
import kotlinx.serialization.Serializable

@Serializable
data class SaleHistoryModel(
    @get:PropertyName("refKey")
    @set:PropertyName("refKey")
    var refKey: String?,
    @get:PropertyName("buyerID")
    @set:PropertyName("buyerID")
    var buyerID: String?,
    @get:PropertyName("sellerID")
    @set:PropertyName("sellerID")
    var sellerID: String?,
    @get:PropertyName("price")
    @set:PropertyName("price")
    var price: Double?,
    @get:PropertyName("objectID")
    @set:PropertyName("objectID")
    var objectID: String?,
    @get:PropertyName("edition")
    @set:PropertyName("edition")
    var edition: Int?,
    @get:PropertyName("language")
    @set:PropertyName("language")
    var language: Int?,
    @get:PropertyName("condition")
    @set:PropertyName("condition")
    var condition: Int?,
    @get:PropertyName("saleDate")
    @set:PropertyName("saleDate")
    var saleDate: String?,
    @get:PropertyName("firURLImage")
    @set:PropertyName("firURLImage")
    var firURLImage: String?,
)

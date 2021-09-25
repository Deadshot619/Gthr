package com.gthr.gthrcollect.model.network.firebaserealtimedb

import com.google.firebase.database.PropertyName
import kotlinx.serialization.Serializable

@Serializable
data class SaleHistoryModel(
    @get:PropertyName("refKey")
    @set:PropertyName("refKey")
    var refKey: String?=null,
    @get:PropertyName("buyerID")
    @set:PropertyName("buyerID")
    var buyerID: String?=null,
    @get:PropertyName("sellerID")
    @set:PropertyName("sellerID")
    var sellerID: String?=null,
    @get:PropertyName("price")
    @set:PropertyName("price")
    var price: Double?=null,
    @get:PropertyName("objectID")
    @set:PropertyName("objectID")
    var objectID: String?=null,
    @get:PropertyName("Edition")
    @set:PropertyName("Edition")
    var edition: Int?=null,
    @get:PropertyName("Language")
    @set:PropertyName("Language")
    var language: Int?=null,
    @get:PropertyName("Condition")
    @set:PropertyName("Condition")
    var condition: Int?=null,
    @get:PropertyName("Date")
    @set:PropertyName("Date")
    var saleDate: String?=null,
    @get:PropertyName("firURLImage")
    @set:PropertyName("firURLImage")
    var firURLImage: String?=null,
)

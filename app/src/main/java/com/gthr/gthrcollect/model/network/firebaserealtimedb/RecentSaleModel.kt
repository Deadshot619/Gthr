package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.google.firebase.database.PropertyName
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase

data class RecentSaleModel(
    @get:PropertyName(FirebaseRealtimeDatabase.CONDITION)
    @set:PropertyName(FirebaseRealtimeDatabase.CONDITION)
    var condition: Int? = -1,
    @get:PropertyName(FirebaseRealtimeDatabase.DATE)
    @set:PropertyName(FirebaseRealtimeDatabase.DATE)
    var date: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.EDITION)
    @set:PropertyName(FirebaseRealtimeDatabase.EDITION)
    var edition: Int? = -1,
    @get:PropertyName(FirebaseRealtimeDatabase.LANGUAGE)
    @set:PropertyName(FirebaseRealtimeDatabase.LANGUAGE)
    var language: Int? = -1,
    @get:PropertyName(FirebaseRealtimeDatabase.OBJECT_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.OBJECT_ID)
    var objectId: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.PRICE)
    @set:PropertyName(FirebaseRealtimeDatabase.PRICE)
    var price: Double? = -1.0
)

data class RecentSaleModel1(
    val key : String,
    var recentSellModel : RecentSaleModel)
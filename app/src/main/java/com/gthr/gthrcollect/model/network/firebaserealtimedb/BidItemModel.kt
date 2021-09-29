package com.gthr.gthrcollect.model.network.firebaserealtimedb

import com.google.firebase.database.PropertyName
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase

data class BidItemModel(
    @set:PropertyName(FirebaseRealtimeDatabase.BID_PRICE)
    @get:PropertyName(FirebaseRealtimeDatabase.BID_PRICE)
    var bidPrice : String? = "",
    @set:PropertyName(FirebaseRealtimeDatabase.CREATOR_UID)
    @get:PropertyName(FirebaseRealtimeDatabase.CREATOR_UID)
    var creatorUID : String ? = "",
    @set:PropertyName(FirebaseRealtimeDatabase.ITEM_OBJECT_ID)
    @get:PropertyName(FirebaseRealtimeDatabase.ITEM_OBJECT_ID)
    var itemObjectID : String ? = "",
    @set:PropertyName(FirebaseRealtimeDatabase.ITEM_REF_KEY)
    @get:PropertyName(FirebaseRealtimeDatabase.ITEM_REF_KEY)
    var itemRefKey: String? = "",
    @set:PropertyName(FirebaseRealtimeDatabase.PRODUCT_CATEGORY)
    @get:PropertyName(FirebaseRealtimeDatabase.PRODUCT_CATEGORY)
    var productCategory: String? = "",
    @set:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    @get:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    var productType: String? = "",
    @set:PropertyName(FirebaseRealtimeDatabase.TOTAL_COST)
    @get:PropertyName(FirebaseRealtimeDatabase.TOTAL_COST)
    var totalCost: String? = "",
    @set:PropertyName(FirebaseRealtimeDatabase._EDITION)
    @get:PropertyName(FirebaseRealtimeDatabase._EDITION)
    var edition: String?,
    @set:PropertyName(FirebaseRealtimeDatabase._CONDITION)
    @get:PropertyName(FirebaseRealtimeDatabase._CONDITION)
    var condition: ConditionModel?,
    @set:PropertyName(FirebaseRealtimeDatabase._LANGUAGE)
    @get:PropertyName(FirebaseRealtimeDatabase._LANGUAGE)
    var language: LanguageModel?,
)

package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.google.firebase.database.PropertyName
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.Serializable

@Serializable
data class FunkoModel(
    @get:PropertyName(FirebaseRealtimeDatabase.EXCLUSIVE)
    @set:PropertyName(FirebaseRealtimeDatabase.EXCLUSIVE)
    var exclusive: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.FUNKO_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.FUNKO_ID)
    var funkoID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.CATEGORY)
    @set:PropertyName(FirebaseRealtimeDatabase.CATEGORY)
    var category: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.FUNKO_TYPE)
    @set:PropertyName(FirebaseRealtimeDatabase.FUNKO_TYPE)
    var funkoType: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    @set:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    var highestBidCost: Int? = -1,
    @get:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    var highestBidID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.IMAGE_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.IMAGE_ID)
    var imageID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.IMAGE_URL)
    @set:PropertyName(FirebaseRealtimeDatabase.IMAGE_URL)
    var imageURL: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.ITEM_NUMBER)
    @set:PropertyName(FirebaseRealtimeDatabase.ITEM_NUMBER)
    var itemNumber: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.LICENSE)
    @set:PropertyName(FirebaseRealtimeDatabase.LICENSE)
    var license: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    @set:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    var lowestAskCost: Double? = -1.0,
    @get:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    var lowestAskID: String? = "",
/*    @get:PropertyName(FirebaseRealtimeDatabase.MARKET_VALUE)
    @set:PropertyName(FirebaseRealtimeDatabase.MARKET_VALUE)
    var marketValue: String? = "",*/
    @get:PropertyName(FirebaseRealtimeDatabase.NAME)
    @set:PropertyName(FirebaseRealtimeDatabase.NAME)
    var name: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    @set:PropertyName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    var numberOfFavorites: Int? = -1,
    @get:PropertyName(FirebaseRealtimeDatabase.OBJECT_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.OBJECT_ID)
    var objectID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    @set:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    var productType: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.RELEASE_DATE)
    @set:PropertyName(FirebaseRealtimeDatabase.RELEASE_DATE)
    var releaseDate: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.STATUS)
    @set:PropertyName(FirebaseRealtimeDatabase.STATUS)
    var status: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.TIER)
    @set:PropertyName(FirebaseRealtimeDatabase.TIER)
    var tier: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.EXCLUSIVITY)
    @set:PropertyName(FirebaseRealtimeDatabase.EXCLUSIVITY)
    var exclusivity: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    @set:PropertyName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    var firImageURL: String? = "",
)
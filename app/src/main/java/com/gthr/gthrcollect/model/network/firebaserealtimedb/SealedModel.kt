package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.google.firebase.database.PropertyName
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.Serializable

@Serializable
data class SealedModel(
    @get:PropertyName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    @set:PropertyName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    var firImageURL: String? = "",

    @get:PropertyName(FirebaseRealtimeDatabase.DESCRIPTION)
    @set:PropertyName(FirebaseRealtimeDatabase.DESCRIPTION)
    var description: String? = "",

    @get:PropertyName(FirebaseRealtimeDatabase.CARD_TEXT)
    @set:PropertyName(FirebaseRealtimeDatabase.CARD_TEXT)
    var cardText: String? = "",
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
    @get:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    @set:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    var lowestAskCost: Double? = -1.0,
    @get:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    var lowestAskID: String? = "",
/*    @get:PropertyName(FirebaseRealtimeDatabase.MARKET_PRICE)
    @set:PropertyName(FirebaseRealtimeDatabase.MARKET_PRICE)
    var marketPrice: String? = "",*/
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
    @get:PropertyName(FirebaseRealtimeDatabase.RARITY)
    @set:PropertyName(FirebaseRealtimeDatabase.RARITY)
    var rarity: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.SET)
    @set:PropertyName(FirebaseRealtimeDatabase.SET)
    var `set`: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.TIER)
    @set:PropertyName(FirebaseRealtimeDatabase.TIER)
    var tier: Int? = -1
)
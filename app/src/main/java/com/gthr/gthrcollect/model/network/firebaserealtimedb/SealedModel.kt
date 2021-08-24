package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SealedModel(
    @SerialName(FirebaseRealtimeDatabase.CARD_TEXT)
    val cardText: String,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    val highestBidCost: Double,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    val highestBidID: String,
    @SerialName(FirebaseRealtimeDatabase.IMAGE_ID)
    val imageID: String,
    @SerialName(FirebaseRealtimeDatabase.IMAGE_URL)
    val imageURL: String,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    val lowestAskCost: Double,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    val lowestAskID: String,
    @SerialName(FirebaseRealtimeDatabase.MARKET_PRICE)
    val marketPrice: String,
    @SerialName(FirebaseRealtimeDatabase.NAME)
    val name: String,
    @SerialName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    val numberOfFavorites: Int,
    @SerialName(FirebaseRealtimeDatabase.OBJECT_ID)
    val objectID: String,
    @SerialName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    val productType: String,
    @SerialName(FirebaseRealtimeDatabase.RARITY)
    val rarity: String,
    @SerialName(FirebaseRealtimeDatabase.SET)
    val `set`: String,
    @SerialName(FirebaseRealtimeDatabase.TIER)
    val tier: Int
)
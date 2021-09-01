package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SealedModel(
    @SerialName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    val firImageURL: String? = "",

    @SerialName(FirebaseRealtimeDatabase.CARD_TEXT)
    val cardText: String? = "",
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    val highestBidCost: Int? = -1,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    val highestBidID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.IMAGE_ID)
    val imageID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.IMAGE_URL)
    val imageURL: String? = "",
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    val lowestAskCost: Int? = -1,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    val lowestAskID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.MARKET_PRICE)
    val marketPrice: Double? = -1.0,
    @SerialName(FirebaseRealtimeDatabase.NAME)
    val name: String? = "",
    @SerialName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    val numberOfFavorites: Int? = -1,
    @SerialName(FirebaseRealtimeDatabase.OBJECT_ID)
    val objectID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    val productType: String? = "",
    @SerialName(FirebaseRealtimeDatabase.RARITY)
    val rarity: String? = "",
    @SerialName(FirebaseRealtimeDatabase.SET)
    val `set`: String? = "",
    @SerialName(FirebaseRealtimeDatabase.TIER)
    val tier: Int? = -1
)
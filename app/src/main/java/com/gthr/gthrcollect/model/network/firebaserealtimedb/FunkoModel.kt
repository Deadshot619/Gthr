package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FunkoModel(
    @SerialName(FirebaseRealtimeDatabase.CATEGORY)
    val category: String? = "",
    @SerialName(FirebaseRealtimeDatabase.EXCLUSIVITY)
    val exclusivity: String? = "",
    @SerialName(FirebaseRealtimeDatabase.FUNKO_TYPE)
    val funkoType: String? = "",
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    val highestBidCost: Double? = 0.0,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    val highestBidID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.IMAGE_URL)
    val imageURL: String? = "",
    @SerialName(FirebaseRealtimeDatabase.ITEM_NUMBER)
    val itemNumber: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.LICENSE)
    val license: String? = "",
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    val lowestAskCost: Double? = 0.0,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    val lowestAskID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.MARKET_VALUE)
    val marketValue: String? = "",
    @SerialName(FirebaseRealtimeDatabase.NAME)
    val name: String? = "",
    @SerialName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    val numberOfFavorites: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.OBJECT_ID)
    val objectID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    val productType: String? = "",
    @SerialName(FirebaseRealtimeDatabase.RELEASE_DATE)
    val releaseDate: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.STATUS)
    val status: String? = "",
    @SerialName(FirebaseRealtimeDatabase.TIER)
    val tier: Int? = 0
)
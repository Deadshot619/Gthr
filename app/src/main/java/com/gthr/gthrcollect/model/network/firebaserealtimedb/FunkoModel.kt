package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FunkoModel(
    @SerialName(FirebaseRealtimeDatabase.EXCLUSIVE)
    val exclusive: String? = "",
    @SerialName(FirebaseRealtimeDatabase.FUNKO_ID)
    val funkoID: String? = "",

    @SerialName(FirebaseRealtimeDatabase.CATEGORY)
    val category: String? = "",
    @SerialName(FirebaseRealtimeDatabase.FUNKO_TYPE)
    val funkoType: String? = "",
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    val highestBidCost: Int? = -1,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    val highestBidID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.IMAGE_ID)
    val imageID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.IMAGE_URL)
    val imageURL: String? = "",
    @SerialName(FirebaseRealtimeDatabase.ITEM_NUMBER)
    val itemNumber: String? = "",
    @SerialName(FirebaseRealtimeDatabase.LICENSE)
    val license: String? = "",
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    val lowestAskCost: Int? = -1,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    val lowestAskID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.MARKET_VALUE)
    val marketValue: String? = "",
    @SerialName(FirebaseRealtimeDatabase.NAME)
    val name: String? = "",
    @SerialName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    val numberOfFavorites: Int? = -1,
    @SerialName(FirebaseRealtimeDatabase.OBJECT_ID)
    val objectID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    val productType: String? = "",
    @SerialName(FirebaseRealtimeDatabase.RELEASE_DATE)
    val releaseDate: String? = "",
    @SerialName(FirebaseRealtimeDatabase.STATUS)
    val status: String? = "",
    @SerialName(FirebaseRealtimeDatabase.TIER)
    val tier: String? = ""
)
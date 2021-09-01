package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MTGModel(
    @SerialName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    val firImageURL: String? = "",

    @SerialName(FirebaseRealtimeDatabase.CARD_BACK_ID)
    val cardBackId: String? = "",
    @SerialName(FirebaseRealtimeDatabase.COLORS)
    val colors: String? = "",
    @SerialName(FirebaseRealtimeDatabase.FLAVOR_TEXT)
    val flavorText: String? = "",
    @SerialName(FirebaseRealtimeDatabase.FOIL)
    val foil: Boolean? = false,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    val highestBidCost: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    val highestBidID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.ID)
    val id: String? = "",
    @SerialName(FirebaseRealtimeDatabase.IMAGE_ID)
    val imageID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.IMAGE_URIS)
    val imageUris: String? = "",
    @SerialName(FirebaseRealtimeDatabase.LANG)
    val lang: String? = "",
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    val lowestAskCost: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    val lowestAskID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.MTGO_FOIL_ID)
    val mtgoFoilId: String? = "",
    @SerialName(FirebaseRealtimeDatabase.MTGO_ID)
    val mtgoId: String? = "",
    @SerialName(FirebaseRealtimeDatabase.NAME)
    val name: String? = "",
    @SerialName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    val numberOfFavorites: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.OBJECT_ID)
    val objectID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.ORACLE_ID)
    val oracleId: String? = "",
    @SerialName(FirebaseRealtimeDatabase.OVER_SIZED)
    val overSized: Boolean? = false,
    @SerialName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    val productType: String? = "",
    @SerialName(FirebaseRealtimeDatabase.RARITY)
    val rarity: String? = "",
    @SerialName(FirebaseRealtimeDatabase.RELEASED_AT)
    val releasedAt: String? = "",
    @SerialName(FirebaseRealtimeDatabase.SET_NAME)
    val setName: String? = "",
    @SerialName(FirebaseRealtimeDatabase.SET_TYPE)
    val setType: String? = "",
    @SerialName(FirebaseRealtimeDatabase.TYPE_LINE)
    val typeLine: String? = ""
)
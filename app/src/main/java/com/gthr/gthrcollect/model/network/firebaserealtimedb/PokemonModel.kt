package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonModel(
    @SerialName(FirebaseRealtimeDatabase.JAPANESE)
    val japanese: Int,
    @SerialName(FirebaseRealtimeDatabase.ENGLISH)
    val english: Int,
    @SerialName(FirebaseRealtimeDatabase.CARD_TYPE)
    val cardType: String,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    val highestBidCost: Double,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    val highestBidID: String,
    @SerialName(FirebaseRealtimeDatabase.HP)
    val hp: Int,
    @SerialName(FirebaseRealtimeDatabase.IMAGE_ID)
    val imageID: String,
    @SerialName(FirebaseRealtimeDatabase.IMAGE_URL)
    val imageURL: String,
    @SerialName(FirebaseRealtimeDatabase.JAPANESE_NUMBER)
    val japaneseNumber: String,
    @SerialName(FirebaseRealtimeDatabase.JAPANESE_SET)
    val japaneseSet: String,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    val lowestAskCost: Double,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    val lowestAskID: String,
    @SerialName(FirebaseRealtimeDatabase.NAME)
    val name: String,
    @SerialName(FirebaseRealtimeDatabase.NO_LANGUAGE_NO_EDITION)
    val noLanguagenoEdition: Int,
    @SerialName(FirebaseRealtimeDatabase.NUMBER)
    val number: Int,
    @SerialName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    val numberOfFavorites: Int,
    @SerialName(FirebaseRealtimeDatabase.OBJECT_ID)
    val objectID: String,
    @SerialName(FirebaseRealtimeDatabase.PRICE_FOIL)
    val priceFoil: String,
    @SerialName(FirebaseRealtimeDatabase.PRICE_NON_FOIL)
    val priceNonFoil: String,
    @SerialName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    val productType: String,
    @SerialName(FirebaseRealtimeDatabase.RARITY)
    val rarity: String,
    @SerialName(FirebaseRealtimeDatabase.RESISTANCE)
    val resistance: String,
    @SerialName(FirebaseRealtimeDatabase.RETREAT_COST)
    val retreatCost: String,
    @SerialName(FirebaseRealtimeDatabase.SET)
    val `set`: String,
    @SerialName(FirebaseRealtimeDatabase.STAGE)
    val stage: String,
    @SerialName(FirebaseRealtimeDatabase.WEAKNESS)
    val weakness: String
)
package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YugiohModel(
    @SerialName(FirebaseRealtimeDatabase.ASIAN_ENGLISH)
    val asianEnglish: Int,
    @SerialName(FirebaseRealtimeDatabase.ASIAN_ENGLISH_OG)
    val asianEnglishOG: Int,
    @SerialName(FirebaseRealtimeDatabase.CARD_TYPE)
    val cardType: String,
    @SerialName(FirebaseRealtimeDatabase.DETAIL_PRICE)
    val detailPrice: Double,
    @SerialName(FirebaseRealtimeDatabase.ENGLISH)
    val english: Int,
    @SerialName(FirebaseRealtimeDatabase.ENGLISH_OG)
    val englishOG: Int,
    @SerialName(FirebaseRealtimeDatabase.EURO_ENGLISH)
    val euroEnglish: Int,
    @SerialName(FirebaseRealtimeDatabase.FIRST_DESCRIPTION)
    val firstDescription: String,
    @SerialName(FirebaseRealtimeDatabase.FRENCH)
    val french: Int,
    @SerialName(FirebaseRealtimeDatabase.FRENCH_CANADIAN)
    val frenchCanadian: Int,
    @SerialName(FirebaseRealtimeDatabase.FRENCH_OG)
    val frenchOG: Int,
    @SerialName(FirebaseRealtimeDatabase.GERMAN)
    val german: Int,
    @SerialName(FirebaseRealtimeDatabase.GERMAN_OG)
    val germanOG: Int,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    val highestBidCost: Int,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    val highestBidID: String,
    @SerialName(FirebaseRealtimeDatabase.IMAGE_ID)
    val imageID: String,
    @SerialName(FirebaseRealtimeDatabase.IMAGE_URL)
    val imageURL: String,
    @SerialName(FirebaseRealtimeDatabase.ITALIAN)
    val italian: Int,
    @SerialName(FirebaseRealtimeDatabase.ITALIAN_OG)
    val italianOG: Int,
    @SerialName(FirebaseRealtimeDatabase.JAPANESE)
    val japanese: Int,
    @SerialName(FirebaseRealtimeDatabase.KOREAN)
    val korean: Int,
    @SerialName(FirebaseRealtimeDatabase.KOREAN_OG)
    val koreanOG: Int,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    val lowestAskCost: Int,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    val lowestAskID: String,
    @SerialName(FirebaseRealtimeDatabase.NAME)
    val name: String,
    @SerialName(FirebaseRealtimeDatabase.NUMBER)
    val number: String,
    @SerialName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    val numberOfFavorites: Int,
    @SerialName(FirebaseRealtimeDatabase.OBJECT_ID)
    val objectID: String,
    @SerialName(FirebaseRealtimeDatabase.OCEANIC_ENGLISH)
    val oceanicEnglish: Int,
    @SerialName(FirebaseRealtimeDatabase.PORTUGUESE)
    val portuguese: Int,
    @SerialName(FirebaseRealtimeDatabase.PORTUGUESE_OG)
    val portugueseOG: Int,
    @SerialName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    val productType: String,
    @SerialName(FirebaseRealtimeDatabase.RARITY)
    val rarity: String,
    @SerialName(FirebaseRealtimeDatabase.SET)
    val `set`: String,
    @SerialName(FirebaseRealtimeDatabase.SPANISH)
    val spanish: Int,
    @SerialName(FirebaseRealtimeDatabase.SPANISH_OG)
    val spanishOG: Int,
    @SerialName(FirebaseRealtimeDatabase.STATS)
    val stats: String
)
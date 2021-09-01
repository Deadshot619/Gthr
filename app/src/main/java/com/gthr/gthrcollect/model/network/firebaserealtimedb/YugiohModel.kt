package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YugiohModel(
    @SerialName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    val firImageURL: String? = "",

    @SerialName(FirebaseRealtimeDatabase.ASIAN_ENGLISH)
    val asianEnglish: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.ASIAN_ENGLISH_OG)
    val asianEnglishOG: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.CARD_TYPE)
    val cardType: String? = "",
    @SerialName(FirebaseRealtimeDatabase.DETAIL_PRICE)
    val detailPrice: String? = "",
    @SerialName(FirebaseRealtimeDatabase.ENGLISH)
    val english: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.ENGLISH_OG)
    val englishOG: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.EURO_ENGLISH)
    val euroEnglish: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.FIRST_DESCRIPTION)
    val firstDescription: String? = "",
    @SerialName(FirebaseRealtimeDatabase.FRENCH)
    val french: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.FRENCH_CANADIAN)
    val frenchCanadian: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.FRENCH_OG)
    val frenchOG: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.GERMAN)
    val german: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.GERMAN_OG)
    val germanOG: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    val highestBidCost: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    val highestBidID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.IMAGE_ID)
    val imageID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.IMAGE_URL)
    val imageURL: String? = "",
    @SerialName(FirebaseRealtimeDatabase.ITALIAN)
    val italian: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.ITALIAN_OG)
    val italianOG: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.JAPANESE)
    val japanese: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.KOREAN)
    val korean: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.KOREAN_OG)
    val koreanOG: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    val lowestAskCost: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    val lowestAskID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.NAME)
    val name: String? = "",
    @SerialName(FirebaseRealtimeDatabase.NUMBER)
    val number: String? = "",
    @SerialName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    val numberOfFavorites: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.OBJECT_ID)
    val objectID: String? = "",
    @SerialName(FirebaseRealtimeDatabase.OCEANIC_ENGLISH)
    val oceanicEnglish: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.PORTUGUESE)
    val portuguese: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.PORTUGUESE_OG)
    val portugueseOG: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    val productType: String? = "",
    @SerialName(FirebaseRealtimeDatabase.RARITY)
    val rarity: String? = "",
    @SerialName(FirebaseRealtimeDatabase.SET)
    val `set`: String? = "",
    @SerialName(FirebaseRealtimeDatabase.SPANISH)
    val spanish: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.SPANISH_OG)
    val spanishOG: Int? = 0,
    @SerialName(FirebaseRealtimeDatabase.STATS)
    val stats: String? = ""
)
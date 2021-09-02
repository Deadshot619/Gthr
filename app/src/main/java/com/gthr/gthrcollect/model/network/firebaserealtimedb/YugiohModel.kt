package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.google.firebase.database.PropertyName
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YugiohModel(
    @get:PropertyName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    @set:PropertyName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    var firImageURL: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.ASIAN_ENGLISH)
    @set:PropertyName(FirebaseRealtimeDatabase.ASIAN_ENGLISH)
    var asianEnglish: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.ASIAN_ENGLISH_OG)
    @set:PropertyName(FirebaseRealtimeDatabase.ASIAN_ENGLISH_OG)
    var asianEnglishOG: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.CARD_TYPE)
    @set:PropertyName(FirebaseRealtimeDatabase.CARD_TYPE)
    var cardType: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.DETAIL_PRICE)
    @set:PropertyName(FirebaseRealtimeDatabase.DETAIL_PRICE)
    var detailPrice: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.ENGLISH)
    @set:PropertyName(FirebaseRealtimeDatabase.ENGLISH)
    var english: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.ENGLISH_OG)
    @set:PropertyName(FirebaseRealtimeDatabase.ENGLISH_OG)
    var englishOG: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.EURO_ENGLISH)
    @set:PropertyName(FirebaseRealtimeDatabase.EURO_ENGLISH)
    var euroEnglish: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.FIRST_DESCRIPTION)
    @set:PropertyName(FirebaseRealtimeDatabase.FIRST_DESCRIPTION)
    var firstDescription: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.FRENCH)
    @set:PropertyName(FirebaseRealtimeDatabase.FRENCH)
    var french: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.FRENCH_CANADIAN)
    @set:PropertyName(FirebaseRealtimeDatabase.FRENCH_CANADIAN)
    var frenchCanadian: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.FRENCH_OG)
    @set:PropertyName(FirebaseRealtimeDatabase.FRENCH_OG)
    var frenchOG: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.GERMAN)
    @set:PropertyName(FirebaseRealtimeDatabase.GERMAN)
    var german: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.GERMAN_OG)
    @set:PropertyName(FirebaseRealtimeDatabase.GERMAN_OG)
    var germanOG: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    @set:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    var highestBidCost: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    var highestBidID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.IMAGE_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.IMAGE_ID)
    var imageID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.IMAGE_URL)
    @set:PropertyName(FirebaseRealtimeDatabase.IMAGE_URL)
    var imageURL: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.ITALIAN)
    @set:PropertyName(FirebaseRealtimeDatabase.ITALIAN)
    var italian: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.ITALIAN_OG)
    @set:PropertyName(FirebaseRealtimeDatabase.ITALIAN_OG)
    var italianOG: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.JAPANESE)
    @set:PropertyName(FirebaseRealtimeDatabase.JAPANESE)
    var japanese: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.KOREAN)
    @set:PropertyName(FirebaseRealtimeDatabase.KOREAN)
    var korean: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.KOREAN_OG)
    @set:PropertyName(FirebaseRealtimeDatabase.KOREAN_OG)
    var koreanOG: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    @set:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    var lowestAskCost: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    var lowestAskID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.NAME)
    @set:PropertyName(FirebaseRealtimeDatabase.NAME)
    var name: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.NUMBER)
    @set:PropertyName(FirebaseRealtimeDatabase.NUMBER)
    var number: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    @set:PropertyName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    var numberOfFavorites: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.OBJECT_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.OBJECT_ID)
    var objectID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.OCEANIC_ENGLISH)
    @set:PropertyName(FirebaseRealtimeDatabase.OCEANIC_ENGLISH)
    var oceanicEnglish: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.PORTUGUESE)
    @set:PropertyName(FirebaseRealtimeDatabase.PORTUGUESE)
    var portuguese: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.PORTUGUESE_OG)
    @set:PropertyName(FirebaseRealtimeDatabase.PORTUGUESE_OG)
    var portugueseOG: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    @set:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    var productType: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.RARITY)
    @set:PropertyName(FirebaseRealtimeDatabase.RARITY)
    var rarity: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.SET)
    @set:PropertyName(FirebaseRealtimeDatabase.SET)
    var `set`: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.SPANISH)
    @set:PropertyName(FirebaseRealtimeDatabase.SPANISH)
    var spanish: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.SPANISH_OG)
    @set:PropertyName(FirebaseRealtimeDatabase.SPANISH_OG)
    var spanishOG: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.STATS)
    @set:PropertyName(FirebaseRealtimeDatabase.STATS)
    var stats: String? = ""
)
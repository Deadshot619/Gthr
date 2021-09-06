package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.google.firebase.database.PropertyName
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class PokemonModel(
    @get:PropertyName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    @set:PropertyName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    var firImageURL: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID)
    @set:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID)
    var highestBid: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.JAPANESE_SET)
    @set:PropertyName(FirebaseRealtimeDatabase.JAPANESE_SET)
    var japaneseSet: String? = "",

    @get:PropertyName(FirebaseRealtimeDatabase.CARD_TYPE)
    @set:PropertyName(FirebaseRealtimeDatabase.CARD_TYPE)
    var cardType: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.ENGLISH)
    @set:PropertyName(FirebaseRealtimeDatabase.ENGLISH)
    var english: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    var highestBidID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.HP)
    @set:PropertyName(FirebaseRealtimeDatabase.HP)
    var hp: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.IMAGE_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.IMAGE_ID)
    var imageID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.IMAGE_URL)
    @set:PropertyName(FirebaseRealtimeDatabase.IMAGE_URL)
    var imageURL: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.JAPANESE)
    @set:PropertyName(FirebaseRealtimeDatabase.JAPANESE)
    var japanese: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.JAPANESE_NUMBER)
    @set:PropertyName(FirebaseRealtimeDatabase.JAPANESE_NUMBER)
    var japaneseNumber: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    @set:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    var lowestAskCost: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    var lowestAskID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.NAME)
    @set:PropertyName(FirebaseRealtimeDatabase.NAME)
    var name: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.NO_LANGUAGE_NO_EDITION)
    @set:PropertyName(FirebaseRealtimeDatabase.NO_LANGUAGE_NO_EDITION)
    var noLanguagenoEdition: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.NUMBER)
    @set:PropertyName(FirebaseRealtimeDatabase.NUMBER)
    var number: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    @set:PropertyName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    var numberOfFavorites: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.OBJECT_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.OBJECT_ID)
    var objectID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.PRICE_FOIL)
    @set:PropertyName(FirebaseRealtimeDatabase.PRICE_FOIL)
    var priceFoil: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.PRICE_NON_FOIL)
    @set:PropertyName(FirebaseRealtimeDatabase.PRICE_NON_FOIL)
    var priceNonFoil: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    @set:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    var productType: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.RARITY)
    @set:PropertyName(FirebaseRealtimeDatabase.RARITY)
    var rarity: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.RESISTANCE)
    @set:PropertyName(FirebaseRealtimeDatabase.RESISTANCE)
    var resistance: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.RETREAT_COST)
    @set:PropertyName(FirebaseRealtimeDatabase.RETREAT_COST)
    var retreatCost: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.SET)
    @set:PropertyName(FirebaseRealtimeDatabase.SET)
    var `set`: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.STAGE)
    @set:PropertyName(FirebaseRealtimeDatabase.STAGE)
    var stage: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.WEAKNESS)
    @set:PropertyName(FirebaseRealtimeDatabase.WEAKNESS)
    var weakness: String? = ""
)
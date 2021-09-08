package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.google.firebase.database.PropertyName
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.Serializable

@Serializable
data class MTGModel(
    @get:PropertyName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    @set:PropertyName(FirebaseRealtimeDatabase.FIR_IMAGE_URL)
    var firImageURL: String? = "",

    @get:PropertyName(FirebaseRealtimeDatabase.CARD_BACK_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.CARD_BACK_ID)
    var cardBackId: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.COLORS)
    @set:PropertyName(FirebaseRealtimeDatabase.COLORS)
    var colors: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.FLAVOR_TEXT)
    @set:PropertyName(FirebaseRealtimeDatabase.FLAVOR_TEXT)
    var flavorText: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.FOIL)
    @set:PropertyName(FirebaseRealtimeDatabase.FOIL)
    var foil: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    @set:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    var highestBidCost: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    var highestBidID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.ID)
    @set:PropertyName(FirebaseRealtimeDatabase.ID)
    var id: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.IMAGE_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.IMAGE_ID)
    var imageID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.IMAGE_URIS)
    @set:PropertyName(FirebaseRealtimeDatabase.IMAGE_URIS)
    var imageUris: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.LANG)
    @set:PropertyName(FirebaseRealtimeDatabase.LANG)
    var lang: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    @set:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    var lowestAskCost: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    var lowestAskID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.MTGO_FOIL_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.MTGO_FOIL_ID)
    var mtgoFoilId: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.MTGO_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.MTGO_ID)
    var mtgoId: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.NAME)
    @set:PropertyName(FirebaseRealtimeDatabase.NAME)
    var name: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    @set:PropertyName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    var numberOfFavorites: Int? = 0,
    @get:PropertyName(FirebaseRealtimeDatabase.OBJECT_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.OBJECT_ID)
    var objectID: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.ORACLE_ID)
    @set:PropertyName(FirebaseRealtimeDatabase.ORACLE_ID)
    var oracleId: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.OVER_SIZED)
    @set:PropertyName(FirebaseRealtimeDatabase.OVER_SIZED)
    var overSized: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    @set:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    var productType: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.RARITY)
    @set:PropertyName(FirebaseRealtimeDatabase.RARITY)
    var rarity: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.RELEASED_AT)
    @set:PropertyName(FirebaseRealtimeDatabase.RELEASED_AT)
    var releasedAt: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.SET_TYPE)
    @set:PropertyName(FirebaseRealtimeDatabase.SET_TYPE)
    var setType: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.TYPE_LINE)
    @set:PropertyName(FirebaseRealtimeDatabase.TYPE_LINE)
    var typeLine: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.SET_NAME)
    @set:PropertyName(FirebaseRealtimeDatabase.SET_NAME)
    var setName: String? = "",

    @get:PropertyName(FirebaseRealtimeDatabase.BRAWL)
    @set:PropertyName(FirebaseRealtimeDatabase.BRAWL)
    var brawl: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.COMMANDER)
    @set:PropertyName(FirebaseRealtimeDatabase.COMMANDER)
    var commander: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.DUEL)
    @set:PropertyName(FirebaseRealtimeDatabase.DUEL)
    var duel: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.FUTURE)
    @set:PropertyName(FirebaseRealtimeDatabase.FUTURE)
    var future: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.LEGACY)
    @set:PropertyName(FirebaseRealtimeDatabase.LEGACY)
    var legacy: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.MODERN)
    @set:PropertyName(FirebaseRealtimeDatabase.MODERN)
    var modern: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.OLDSCHOOL)
    @set:PropertyName(FirebaseRealtimeDatabase.OLDSCHOOL)
    var oldschool: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.PAUPER)
    @set:PropertyName(FirebaseRealtimeDatabase.PAUPER)
    var pauper: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.PENNY)
    @set:PropertyName(FirebaseRealtimeDatabase.PENNY)
    var penny: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.PIONEER)
    @set:PropertyName(FirebaseRealtimeDatabase.PIONEER)
    var pioneer: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.VINTAGE)
    @set:PropertyName(FirebaseRealtimeDatabase.VINTAGE)
    var vintage: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.STANDARD)
    @set:PropertyName(FirebaseRealtimeDatabase.STANDARD)
    var standard: Boolean? = false,
    @get:PropertyName(FirebaseRealtimeDatabase.HISTORIC)
    @set:PropertyName(FirebaseRealtimeDatabase.HISTORIC)
    var historic: Boolean? = false,

    @get:PropertyName(FirebaseRealtimeDatabase.POWER)
    @set:PropertyName(FirebaseRealtimeDatabase.POWER)
    var power: /*Int*/String? = /*-1*/"",
    @get:PropertyName(FirebaseRealtimeDatabase.TOUGHNESS)
    @set:PropertyName(FirebaseRealtimeDatabase.TOUGHNESS)
    var toughness: /*Int*/String? = /*-1*/"",
)
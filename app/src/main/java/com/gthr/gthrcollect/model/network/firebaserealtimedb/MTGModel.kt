package com.gthr.gthrcollect.model.network.firebaserealtimedb


import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MTGModel(
    @SerialName(FirebaseRealtimeDatabase.ARTIST)
    val artist: String,
    @SerialName(FirebaseRealtimeDatabase.ARTIST_IDS)
    val artistIds: String,
    @SerialName(FirebaseRealtimeDatabase.BOOSTER)
    val booster: String,
    @SerialName(FirebaseRealtimeDatabase.BORDER_COLOR)
    val borderColor: String,
    @SerialName(FirebaseRealtimeDatabase.CARD_BACK_ID)
    val cardBackId: String,
    @SerialName(FirebaseRealtimeDatabase.CMC)
    val cmc: Int,
    @SerialName(FirebaseRealtimeDatabase.COLLECTOR_NUMBER)
    val collectorNumber: Int,
    @SerialName(FirebaseRealtimeDatabase.COLOR_IDENTITY)
    val colorIdentity: String,
    @SerialName(FirebaseRealtimeDatabase.COLORS)
    val colors: String,
    @SerialName(FirebaseRealtimeDatabase.DIGITAL)
    val digital: String,
    @SerialName(FirebaseRealtimeDatabase.FLAVOR_TEXT)
    val flavorText: String,
    @SerialName(FirebaseRealtimeDatabase.FOIL)
    val foil: String,
    @SerialName(FirebaseRealtimeDatabase.FRAME)
    val frame: Int,
    @SerialName(FirebaseRealtimeDatabase.FULL_ART)
    val fullArt: String,
    @SerialName(FirebaseRealtimeDatabase.GAMES)
    val games: String,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_COST)
    val highestBidCost: Int,
    @SerialName(FirebaseRealtimeDatabase.HIGHEST_BID_ID)
    val highestBidID: String,
    @SerialName(FirebaseRealtimeDatabase.HIGHRES_IMAGE)
    val highresImage: String,
    @SerialName(FirebaseRealtimeDatabase.ID)
    val id: String,
    @SerialName(FirebaseRealtimeDatabase.ILLUSTRATION_ID)
    val illustrationId: String,
    @SerialName(FirebaseRealtimeDatabase.IMAGE_ID)
    val imageID: String,
    @SerialName(FirebaseRealtimeDatabase.IMAGE_URIS)
    val imageUris: String,
    @SerialName(FirebaseRealtimeDatabase.KEYWORDS)
    val keywords: String,
    @SerialName(FirebaseRealtimeDatabase.LANG)
    val lang: String,
    @SerialName(FirebaseRealtimeDatabase.LARGE_IMG)
    val largeImg: String,
    @SerialName(FirebaseRealtimeDatabase.LAYOUT)
    val layout: String,
    @SerialName(FirebaseRealtimeDatabase.LEGALITIES)
    val legalities: String,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_COST)
    val lowestAskCost: Int,
    @SerialName(FirebaseRealtimeDatabase.LOWEST_ASK_ID)
    val lowestAskID: String,
    @SerialName(FirebaseRealtimeDatabase.MANA_COST)
    val manaCost: String,
    @SerialName(FirebaseRealtimeDatabase.MTGO_FOIL_ID)
    val mtgoFoilId: String,
    @SerialName(FirebaseRealtimeDatabase.MTGO_ID)
    val mtgoId: String,
    @SerialName(FirebaseRealtimeDatabase.NAME)
    val name: String,
    @SerialName(FirebaseRealtimeDatabase.NONFOIL)
    val nonfoil: String,
    @SerialName(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
    val numberOfFavorites: Int,
    @SerialName(FirebaseRealtimeDatabase.OBJECT_ID)
    val objectID: String,
    @SerialName(FirebaseRealtimeDatabase.OBJECT)
    val objectX: String,
    @SerialName(FirebaseRealtimeDatabase.ORACLE_ID)
    val oracleId: String,
    @SerialName(FirebaseRealtimeDatabase.ORACLE_TEXT)
    val oracleText: String,
    @SerialName(FirebaseRealtimeDatabase.OVER_SIZED)
    val oversized: String,
    @SerialName(FirebaseRealtimeDatabase.PNG_IMG)
    val pngImg: String,
    @SerialName(FirebaseRealtimeDatabase.POWER)
    val power: Int,
    @SerialName(FirebaseRealtimeDatabase.PRICES)
    val prices: String,
    @SerialName(FirebaseRealtimeDatabase.PRINTS_SEARCH_URI)
    val printsSearchUri: String,
    @SerialName(FirebaseRealtimeDatabase.PRODUCED_MANA)
    val producedMana: String,
    @SerialName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    val productType: String,
    @SerialName(FirebaseRealtimeDatabase.PROMO)
    val promo: String,
    @SerialName(FirebaseRealtimeDatabase.PROMO_TYPES)
    val promoTypes: String,
    @SerialName(FirebaseRealtimeDatabase.RARITY)
    val rarity: String,
    @SerialName(FirebaseRealtimeDatabase.RELEASED_AT)
    val releasedAt: String,
    @SerialName(FirebaseRealtimeDatabase.REPRINT)
    val reprint: String,
    @SerialName(FirebaseRealtimeDatabase.RESERVED)
    val reserved: String,
    @SerialName(FirebaseRealtimeDatabase.RULINGS_URI)
    val rulingsUri: String,
    @SerialName(FirebaseRealtimeDatabase.SCRYFALL_SET_URI)
    val scryfallSetUri: String,
    @SerialName(FirebaseRealtimeDatabase.SCRYFALL_URI)
    val scryfallUri: String,
    @SerialName(FirebaseRealtimeDatabase.SET)
    val `set`: String,
    @SerialName(FirebaseRealtimeDatabase.SET_NAME)
    val setName: String,
    @SerialName(FirebaseRealtimeDatabase.SET_SEARCH_URI)
    val setSearchUri: String,
    @SerialName(FirebaseRealtimeDatabase.SET_TYPE)
    val setType: String,
    @SerialName(FirebaseRealtimeDatabase.SET_URI)
    val setUri: String,
    @SerialName(FirebaseRealtimeDatabase.SMALL_IMG)
    val smallImg: String,
    @SerialName(FirebaseRealtimeDatabase.STORY_SPOTLIGHT)
    val storySpotlight: String,
    @SerialName(FirebaseRealtimeDatabase.TCGPLAYER_ID)
    val tcgplayerId: Int,
    @SerialName(FirebaseRealtimeDatabase.TEXTLESS)
    val textless: String,
    @SerialName(FirebaseRealtimeDatabase.TOUGHNESS)
    val toughness: Int,
    @SerialName(FirebaseRealtimeDatabase.TYPE_LINE)
    val typeLine: String,
    @SerialName(FirebaseRealtimeDatabase.URI)
    val uri: String,
    @SerialName(FirebaseRealtimeDatabase.VARIATION)
    val variation: String,
    @SerialName(FirebaseRealtimeDatabase.WATERMARK)
    val watermark: String
)
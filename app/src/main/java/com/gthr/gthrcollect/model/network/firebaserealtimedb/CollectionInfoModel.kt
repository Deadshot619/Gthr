package com.gthr.gthrcollect.model.network.firebaserealtimedb

import com.google.firebase.database.PropertyName
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.Serializable

@Serializable
data class CollectionInfoModel(
    @get:PropertyName(FirebaseRealtimeDatabase.ABOUT)
    @set:PropertyName(FirebaseRealtimeDatabase.ABOUT)
    var about: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.BUY_LIST)
    @set:PropertyName(FirebaseRealtimeDatabase.BUY_LIST)
    var buyList: HashMap<String, String>? = hashMapOf(),
    @get:PropertyName(FirebaseRealtimeDatabase.COLLECTION_DISPLAY_NAME)
    @set:PropertyName(FirebaseRealtimeDatabase.COLLECTION_DISPLAY_NAME)
    var collectionDisplayName: String = "",
    @get:PropertyName(FirebaseRealtimeDatabase.COLLECTION_RAW_NAME)
    @set:PropertyName(FirebaseRealtimeDatabase.COLLECTION_RAW_NAME)
    var collectionRawName: String = "",
    @get:PropertyName(FirebaseRealtimeDatabase.FAVORITE_COLLECTION_LIST)
    @set:PropertyName(FirebaseRealtimeDatabase.FAVORITE_COLLECTION_LIST)
    var favoriteCollectionList: List<String>? = listOf(),
    @get:PropertyName(FirebaseRealtimeDatabase.FAVORITE_PRODUCT_LIST)
    @set:PropertyName(FirebaseRealtimeDatabase.FAVORITE_PRODUCT_LIST)
    var favoriteProductList: List<String>? = listOf(),
    @get:PropertyName(FirebaseRealtimeDatabase.FOLLOWERS_LIST)
    @set:PropertyName(FirebaseRealtimeDatabase.FOLLOWERS_LIST)
    var followersList: List<String>? = listOf(),

    @get:PropertyName(FirebaseRealtimeDatabase.PROFILE_URL_KEY)
    @set:PropertyName(FirebaseRealtimeDatabase.PROFILE_URL_KEY)
    var profileImageURL:String="",
    @get:PropertyName(FirebaseRealtimeDatabase.SELL_LIST)
    @set:PropertyName(FirebaseRealtimeDatabase.SELL_LIST)
    var sellList: List<String>? = listOf(),

    //corresponds to the firebase uid and the userInfoModel document id and uid
    @get:PropertyName(FirebaseRealtimeDatabase.USER_REF_KEY)
    @set:PropertyName(FirebaseRealtimeDatabase.USER_REF_KEY)
    var userRefKey: String = "",

    @get:PropertyName(FirebaseRealtimeDatabase.COLLECTION_LIST)
    @set:PropertyName(FirebaseRealtimeDatabase.COLLECTION_LIST)
    var collectionList: HashMap<String,CollectionItemModel>? = null
)
@Serializable
data class CollectionItemModel(

    @get:PropertyName(FirebaseRealtimeDatabase.ID)
    @set:PropertyName(FirebaseRealtimeDatabase.ID)
    var id: String?="",

    @get:PropertyName(FirebaseRealtimeDatabase.ITEM_REF_KEY)
    @set:PropertyName(FirebaseRealtimeDatabase.ITEM_REF_KEY)
    var itemRefKey: String?="",

    @get:PropertyName(FirebaseRealtimeDatabase.MARKET_COST)
    @set:PropertyName(FirebaseRealtimeDatabase.MARKET_COST)
    var marketCost: Double=0.0,

    @get:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    @set:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    var productType: String?="",

    @get:PropertyName(FirebaseRealtimeDatabase.ASK_REF_KEY)
    @set:PropertyName(FirebaseRealtimeDatabase.ASK_REF_KEY)
    var askRefKey: String?="",

    @get:PropertyName(FirebaseRealtimeDatabase.FRONT_IMAGE_URL)
    @set:PropertyName(FirebaseRealtimeDatabase.FRONT_IMAGE_URL)
    var frontImageURL: String?="",

    @get:PropertyName(FirebaseRealtimeDatabase.BACK_IMAGE_URL)
    @set:PropertyName(FirebaseRealtimeDatabase.BACK_IMAGE_URL)
    var backImageURL: String?="",

    @get:PropertyName(FirebaseRealtimeDatabase._CONDITION)
    @set:PropertyName(FirebaseRealtimeDatabase._CONDITION)
    var condition: ConditionModel? = null,

    @get:PropertyName(FirebaseRealtimeDatabase._LANGUAGE)
    @set:PropertyName(FirebaseRealtimeDatabase._LANGUAGE)
    var language: LanguageModel? = null,

    @get:PropertyName(FirebaseRealtimeDatabase._EDITION)
    @set:PropertyName(FirebaseRealtimeDatabase._EDITION)
    var edition: String?="",
)

@Serializable
data class ConditionModel(
        @get:PropertyName(FirebaseRealtimeDatabase.KEY)
        @set:PropertyName(FirebaseRealtimeDatabase.KEY)
        var key: Int? = -1,

        @get:PropertyName(FirebaseRealtimeDatabase.DISPLAY_NAME)
        @set:PropertyName(FirebaseRealtimeDatabase.DISPLAY_NAME)
        var displayName: String? = "",

        @get:PropertyName(FirebaseRealtimeDatabase.TYPE)
        @set:PropertyName(FirebaseRealtimeDatabase.TYPE)
        var type: String? = null,

        @get:PropertyName(FirebaseRealtimeDatabase.ABBREVIATED_NAME)
        @set:PropertyName(FirebaseRealtimeDatabase.ABBREVIATED_NAME)
        var abbreviatedName: String? = ""
)

@Serializable
data class LanguageModel(
    @get:PropertyName(FirebaseRealtimeDatabase.KEY)
    @set:PropertyName(FirebaseRealtimeDatabase.KEY)
    var key: Int ?=-1 ,

    @get:PropertyName(FirebaseRealtimeDatabase.DISPLAY_NAME)
    @set:PropertyName(FirebaseRealtimeDatabase.DISPLAY_NAME)
    var displayName: String ?="",

    @get:PropertyName(FirebaseRealtimeDatabase.ABBREVIATED_NAME)
    @set:PropertyName(FirebaseRealtimeDatabase.ABBREVIATED_NAME)
    var abbreviatedName: String ?=""
)
package com.gthr.gthrcollect.model.network.firebaserealtimedb

import com.algolia.search.serialize.KeyFrom
import com.google.firebase.database.PropertyName
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.enums.EditionType
import com.gthr.gthrcollect.utils.enums.ProductType
import kotlinx.serialization.Serializable

@Serializable
data class CollectionInfoModel(
    @get:PropertyName(FirebaseRealtimeDatabase.ABOUT)
    @set:PropertyName(FirebaseRealtimeDatabase.ABOUT)
    var about: String? = "",
    @get:PropertyName(FirebaseRealtimeDatabase.BUY_LIST)
    @set:PropertyName(FirebaseRealtimeDatabase.BUY_LIST)
    var buyList: List<String>? = listOf(),
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
    var id: String="",

    @get:PropertyName(FirebaseRealtimeDatabase.ITEM_REF_KEY)
    @set:PropertyName(FirebaseRealtimeDatabase.ITEM_REF_KEY)
    var itemRefKey: String?="",

    @get:PropertyName(FirebaseRealtimeDatabase.MARKET_COST)
    @set:PropertyName(FirebaseRealtimeDatabase.MARKET_COST)
    var marketCost: Double=0.0,

    @get:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    @set:PropertyName(FirebaseRealtimeDatabase.PRODUCT_TYPE)
    var productType: String?="",

    @get:PropertyName(FirebaseRealtimeDatabase.EDITION)
    @set:PropertyName(FirebaseRealtimeDatabase.EDITION)
    var edition: String?="",


    @get:PropertyName(FirebaseRealtimeDatabase.ASK_REF_KEY)
    @set:PropertyName(FirebaseRealtimeDatabase.ASK_REF_KEY)
    var askRefKey: String?="",

    @get:PropertyName(FirebaseRealtimeDatabase.FRONT_IMAGE_URL)
    @set:PropertyName(FirebaseRealtimeDatabase.FRONT_IMAGE_URL)
    var frontImageURL: String?="",

    @get:PropertyName(FirebaseRealtimeDatabase.BACK_IMAGE_URL)
    @set:PropertyName(FirebaseRealtimeDatabase.BACK_IMAGE_URL)
    var backImageURL: String?="",

    //  var condition: ConditionItem?,
    //  var language: LanguageItem?
)

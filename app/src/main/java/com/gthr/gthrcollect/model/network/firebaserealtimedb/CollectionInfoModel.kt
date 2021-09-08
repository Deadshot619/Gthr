package com.gthr.gthrcollect.model.network.firebaserealtimedb

import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionInfoModel(
    @SerialName(FirebaseRealtimeDatabase.ABOUT)
    val about: String? = "",
    @SerialName(FirebaseRealtimeDatabase.BUY_LIST)
    val buyList: List<String>? = listOf(),
    @SerialName(FirebaseRealtimeDatabase.COLLECTION_DISPLAY_NAME)
    var collectionDisplayName: String = "",
    @SerialName(FirebaseRealtimeDatabase.COLLECTION_RAW_NAME)
    val collectionRawName: String = "",
    @SerialName(FirebaseRealtimeDatabase.FAVORITE_COLLECTION_LIST)
    val favoriteCollectionList: List<String>? = listOf(),
    @SerialName(FirebaseRealtimeDatabase.FAVORITE_PRODUCT_LIST)
    val favoriteProductList: List<String>? = listOf(),
    @SerialName(FirebaseRealtimeDatabase.FOLLOWERS_LIST)
    val followersList: List<String>? = listOf(),

    @SerialName(FirebaseRealtimeDatabase.PROFILE_URL_KEY)
    var profileImageURL:String="",
    @SerialName(FirebaseRealtimeDatabase.SELL_LIST)
    val sellList: List<String>? = listOf(),

    //corresponds to the firebase uid and the userInfoModel document id and uid
    @SerialName(FirebaseRealtimeDatabase.USER_REF_KEY)
    val userRefKey: String = ""
//    val collectionList: CollectionList,
)
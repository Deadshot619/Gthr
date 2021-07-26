package com.gthr.gthrcollect.model.network.firebaserealtimedb

data class CollectionInfoModel(
    val about: String? = "",
    val buyList: List<String>? = listOf(),
    val collectionDisplayName: String = "",
//    val collectionList: CollectionList,
    val collectionRawName: String = "",
    val favoriteCollectionList: List<String>? = listOf(),
    val favoriteProductList: List<String>? = listOf(),
    val followersList: List<String>? = listOf(),
    val sellList: List<Any>? = listOf(),
    val userRefKey: String = ""       //corresponds to the firebase uid and the userInfoModel document id and uid
)
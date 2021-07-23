package com.gthr.gthrcollect.model.domain

data class CollectionInfoDomainModel(
    val about: String?,
    val buyList: List<String>?,
    val collectionDisplayName: String,
//    val collectionList: CollectionList,
    val collectionRawName: String,
    val favoriteCollectionList: List<String>?,
    val favoriteProductList: List<String>?,
    val followersList: List<String>?,
    val sellList: List<Any>?,
    val userRefKey: String      //corresponds to the firebase uid and the userInfoModel document id and uid
)
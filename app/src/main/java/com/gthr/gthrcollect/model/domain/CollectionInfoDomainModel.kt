package com.gthr.gthrcollect.model.domain

import com.gthr.gthrcollect.model.network.firebaserealtimedb.CollectionItemModel
import com.gthr.gthrcollect.utils.enums.EditionType
import com.gthr.gthrcollect.utils.enums.ProductType

data class CollectionInfoDomainModel(
    val about: String?,
    val buyList: List<String>?,
    val collectionDisplayName: String,
    val collectionRawName: String,
    val favoriteCollectionList: List<String>?,
    val favoriteProductList: List<String>?,
    val followersList: List<String>?,
    val sellList: List<Any>?,
    val userRefKey: String ,     //corresponds to the firebase uid and the userInfoModel document id and uid
    var profileImage:String="",
    var collectionId: String = "",
    val collectionList: HashMap<String,CollectionItemModel>?
    )

data class CollectionItemDomainModel(
    var id: String,
    var itemRefKey: String?,
    var marketCost: Double,
    var productType: ProductType?,
    var edition: EditionType?,
    var askRefKey: String?,
    var frontImageURL: String?,
    var backImageURL: String?,
    //  var condition: ConditionItem?,
    //  var language: LanguageItem?
)
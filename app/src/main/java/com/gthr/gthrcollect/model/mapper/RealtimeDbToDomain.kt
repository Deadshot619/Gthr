package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.CollectionInfoModel

fun CollectionInfoModel.toCollectionInfoDomainModel() = CollectionInfoDomainModel(
    about = about, buyList = buyList, collectionDisplayName = collectionDisplayName, //collectionList,
    collectionRawName = collectionRawName, favoriteCollectionList = favoriteCollectionList,
    favoriteProductList = favoriteProductList,
    followersList = followersList,
    sellList = sellList, userRefKey = userRefKey,
    profileImage=profileImageURL

)
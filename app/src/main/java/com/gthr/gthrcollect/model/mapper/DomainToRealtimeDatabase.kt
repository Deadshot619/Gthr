package com.gthr.gthrcollect.model.mapper

import com.google.firebase.firestore.IgnoreExtraProperties
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.CollectionInfoModel

fun UserInfoDomainModel.toRealtimeDatabaseModel() = CollectionInfoModel(
    collectionDisplayName = displayName,
    about = bio,
    userRefKey = GthrCollect.prefs?.signedInUser?.uid!!,
    collectionRawName = displayName,
    buyList = null,
    favoriteCollectionList = null,
    favoriteProductList = null,
    followersList = null,
    sellList = null
)
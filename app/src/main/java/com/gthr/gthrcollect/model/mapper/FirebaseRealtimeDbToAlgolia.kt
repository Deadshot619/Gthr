package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.model.network.algolia.AlgoliaCollectionModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.CollectionInfoModel

fun CollectionInfoModel.toAlgoliaCollectionModel() = AlgoliaCollectionModel(
    collectionDisplayName = collectionDisplayName,
    numberOfFavorites = followersList?.size ?: 0,
    objectID = userRefKey,
    about = about
)

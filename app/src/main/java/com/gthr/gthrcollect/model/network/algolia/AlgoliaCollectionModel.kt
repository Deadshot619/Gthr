package com.gthr.gthrcollect.model.network.algolia

data class AlgoliaCollectionModel(
    val collectionDisplayName: String,
    val numberOfFavorites: Int,
    val objectID: String,
    val about: String?
)

package com.gthr.gthrcollect.model.network.algolia

import kotlinx.serialization.Serializable

@Serializable
data class AlgoliaCollectionModel(
    val collectionDisplayName: String,
    val numberOfFavorites: Int,
    val objectID: String,
    val about: String?
)

package com.gthr.gthrcollect.model.network.algolia

import com.gthr.gthrcollect.utils.constants.AlgoliaConstants
import com.gthr.gthrcollect.utils.constants.Firestore
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlgoliaCollectionModel(
    @SerialName(AlgoliaConstants.COLLECTION_DISPLAY_NAME)
    val collectionDisplayName: String,
    @SerialName(AlgoliaConstants.NUMBER_OF_FAVORITES)
    val numberOfFavorites: Int,
    @SerialName(AlgoliaConstants.OBJECT_ID)
    val objectID: String,
    @SerialName(AlgoliaConstants.ABOUT)
    val about: String?
)

package com.gthr.gthrcollect.model.network.firestore

import com.gthr.gthrcollect.utils.constants.Firestore
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditAccInfoFireStoreModel(
    @SerialName(Firestore.FIRST_NAME)
    val firstName: String = "",
    @SerialName(Firestore.LAST_NAME)
    val lastName: String = "",
    @SerialName(Firestore.BIRTH_DATE)
    val birthDate: String = ""
)
package com.gthr.gthrcollect.model.network.firestore

import com.gthr.gthrcollect.utils.constants.Firestore
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressFirestoreModel(
    @SerialName(Firestore.DEFAULT)
    val default: Boolean=false,
    @SerialName(Firestore.ADDRESS_LINE1)
    val addressLine1: String="",
    @SerialName(Firestore.ADDRESS_LINE2)
    val addressLine2: String="",
    @SerialName(Firestore.CITY)
    val city: String="",
    @SerialName(Firestore.COUNTRY)
    val country: String="",
    @SerialName(Firestore.FIRST_NAME)
    val firstName: String="",
    @SerialName(Firestore.LAST_NAME)
    val lastName: String="",
    @SerialName(Firestore.STATE)
    val state: String="",
    @SerialName(Firestore.U_ID)
    val uid: String="",    //same as the firebase auth uid and the userInfoModel ref key
    @SerialName(Firestore.ZIP_CODE)
    val zipCode: String=""
)

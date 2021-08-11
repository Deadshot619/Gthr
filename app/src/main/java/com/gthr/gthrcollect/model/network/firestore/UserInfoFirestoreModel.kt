package com.gthr.gthrcollect.model.network.firestore

import com.gthr.gthrcollect.utils.constants.Firestore
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoFirestoreModel(
    @SerialName(Firestore.ADDRESS_LIST)
    val addressList: List<AddressFirestoreModel> = listOf(),
    @SerialName(Firestore.BIRTH_DATE)
    val birthDate: String = "",
    @SerialName(Firestore.COLLECTION_ID)
    val collectionId: String = "",
    @SerialName(Firestore.CREATION_DATE)
    val creationDate: String = "",
    @SerialName(Firestore.EMAIL)
    val email: String = "",      //corresponds to the firebaseAuth email used in account creation, updating this should also update the firebase Auth email variable and vice versa
    @SerialName(Firestore.IS_SELLER)
    val isSeller: Boolean = true,       //(Legacy) defaults to true
    @SerialName(Firestore.IS_VERIFIED)
    val isVerified: Boolean = false,        //once id is verified it is set to true, default to false
    @SerialName(Firestore.FIRST_NAME)
    val firstName: String = "",
    @SerialName(Firestore.LAST_NAME)
    val lastName: String = "",
    @SerialName(Firestore.PHONE_NUMBER)
    val phoneNumber: String = "",        //includes country code and phone number
    @SerialName(Firestore.U_ID)
    val uid: String = ""        //corresponds to the firebase auth uid
)

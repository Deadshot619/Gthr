package com.gthr.gthrcollect.model.network.firestore

data class UserInfoFirestoreModel(
    val addressList: List<AddressFirestoreModel> = listOf(),
    val birthDate: String = "",
    val collectionId: String = "",
    val creationDate: String = "",
    val email: String = "",      //corresponds to the firebaseAuth email used in account creation, updating this should also update the firebase Auth email variable and vice versa
    val isSeller: Boolean = true,       //(Legacy) defaults to true
    val isVerified: Boolean = false,        //once id is verified it is set to true, default to false
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",        //includes country code and phone number
    val uid: String = ""        //corresponds to the firebase auth uid
)

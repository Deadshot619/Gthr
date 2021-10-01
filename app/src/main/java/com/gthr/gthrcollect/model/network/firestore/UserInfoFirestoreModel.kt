package com.gthr.gthrcollect.model.network.firestore

import com.google.firebase.firestore.PropertyName
import com.gthr.gthrcollect.utils.constants.Firestore

data class UserInfoFirestoreModel(
    @PropertyName(Firestore.ADDRESS_LIST)
    var addressList: List<AddressFirestoreModel> = listOf(),
    @PropertyName(Firestore.BIRTH_DATE)
    var birthDate: String = "",
    @PropertyName(Firestore.COLLECTION_ID)
    var collectionID: String = "",
    @PropertyName(Firestore.CREATION__DATE)
    var creationDate: String = "",
    @PropertyName(Firestore.EMAIL)
    var email: String = "",      //corresponds to the firebaseAuth email used in account creation, updating this should also update the firebase Auth email variable and vice versa
    @field:JvmField
    @PropertyName(Firestore.IS_SELLER)
    var isSeller: Boolean = true,       //(Legacy) defaults to true
    @field:JvmField
    @PropertyName(Firestore.IS_VERIFIED)
    var isVerified: Boolean = false,        //once id is verified it is set to true, default to false
    @PropertyName(Firestore.FIRST_NAME)
    var firstName: String = "",
    @PropertyName(Firestore.LAST_NAME)
    var lastName: String = "",
    @PropertyName(Firestore.PHONE_NUMBER)
    var phoneNumber: String = "",        //includes country code and phone number
    @PropertyName(Firestore.U_ID)
    var uid: String = ""        //corresponds to the firebase auth uid
)

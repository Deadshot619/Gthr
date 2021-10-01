package com.gthr.gthrcollect.utils.constants

object Firestore {

    /* Collections */
    const val COLLECTION_USER_INFO = "userInfo"
    const val COLLECTION_ADDRESS_LIST = "addressList"

    /* Common */
    const val FIRST_NAME = "firstName"
    const val LAST_NAME = "lastName"
    const val BIRTH_DATE = "birthDate"
    const val U_ID = "uid"

    /* AddressFirestoreModel */
    const val DEFAULT = "default"
    const val ADDRESS_LINE1 = "addressLine1"
    const val ADDRESS_LINE2 = "addressLine2"
    const val CITY = "city"
    const val COUNTRY = "country"
    const val STATE = "state"
    const val ZIP_CODE = "zipCode"

    /* EditAccInfoFireStoreModel */
    const val ADDRESS_LIST = "addressList"
    const val COLLECTION_ID = "collectionID"
    const val CREATION_DATE = "creationDate"
    const val CREATION__DATE = "creation_date"
    const val EMAIL = "email"      //corresponds to the firebaseAuth email used in account creation, updating this should also update the firebase Auth email variable and vice versa
    const val IS_SELLER = "isSeller"       //(Legacy) defaults to true
    const val IS_VERIFIED = "isVerified"        //once id is verified it is set to true, default to false
    const val PHONE_NUMBER = "phoneNumber"        //includes country code and phone number



}
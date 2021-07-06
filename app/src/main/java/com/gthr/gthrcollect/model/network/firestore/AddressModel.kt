package com.gthr.gthrcollect.model.network.firestore

data class AddressModel(
    val isDefault: Boolean,
    val addressLine1: String,
    val addressLine2: String,
    val city: String,
    val country: String,
    val firstName: String,
    val lastName: String,
    val state: String,
    val uid: String,    //same as the firebase auth uid and the userInfoModel ref key
    val zipCode: String
)

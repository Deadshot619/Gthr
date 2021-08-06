package com.gthr.gthrcollect.model.domain

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserInfoDomainModel(
    var firstName: String = "",
    var lastName: String = "",
    var dd: String = "",
    var mm: String = "",
    var yyyy: String = "",
    var mobile: String = "",
    var countryCode: String = "",
    var tnc: Boolean = false,
    var displayName: String = "",
    var bio: String = "",
    var collectionId: String = "",
    var emailId: String = "",
    var addressList : List<ShippingAddressDomainModel> = listOf(),
)

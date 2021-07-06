package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel

fun UserInfoDomainModel.toFirestoreModel() =
    com.gthr.gthrcollect.model.network.firestore.UserInfoFirestoreModel(
        firstName = firstName,
        lastName = lastName,
        email = emailId,
        phoneNumber = "+$countryCode-$mobile",
        birthDate = "$mm/$dd/$yyyy",
        uid = GthrCollect.prefs?.signedInUser!!.uid,
        creationDate = "",
        collectionId = "",
        addressList = listOf()
    )
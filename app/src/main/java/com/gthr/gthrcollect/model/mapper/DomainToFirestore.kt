package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.model.domain.EditAccInfoDomainModel
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel

fun UserInfoDomainModel.toFirestoreModel(collectionId: String) =
    com.gthr.gthrcollect.model.network.firestore.UserInfoFirestoreModel(
        firstName = firstName,
        lastName = lastName,
        email = emailId,
        phoneNumber = "+$countryCode-$mobile",
        birthDate = "$mm/$dd/$yyyy",
        uid = GthrCollect.prefs?.signedInUser!!.uid,
        creationDate = "",
        collectionId = collectionId,
        addressList = listOf()
    )

fun EditAccInfoDomainModel.toFirestoreModel() =
    com.gthr.gthrcollect.model.network.firestore.EditAccInfoFireStoreModel(
        firstName = firstName,
        lastName = lastName,
        birthDate = "$mm/$dd/$yyyy",
    )
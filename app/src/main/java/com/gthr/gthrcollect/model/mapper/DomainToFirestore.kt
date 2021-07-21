package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.model.domain.EditAccInfoDomainModel
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.model.network.firestore.AddressFirestoreModel

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

fun ShippingAddressDomainModel.toFirestoreModel() =
    AddressFirestoreModel(
        default = isSelected,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        city = city,
        country = country,
        firstName = firstName,
        lastName = lastName,
        state = state,
        uid = GthrCollect.prefs?.signedInUser!!.uid,
        zipCode = postalCode
    )

fun List<ShippingAddressDomainModel>.toFirestoreModel() : List<AddressFirestoreModel> = run{
    val list : ArrayList<AddressFirestoreModel> = arrayListOf()
    this.forEach{
        list.add(it.toFirestoreModel())
    }
    return list
}
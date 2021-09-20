package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.model.domain.EditAccInfoDomainModel
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.model.network.firestore.AddressFirestoreModel
import com.gthr.gthrcollect.model.network.firestore.UserInfoFirestoreModel
import com.gthr.gthrcollect.utils.extensions.*

fun UserInfoFirestoreModel.toEditAccountDomainModel() =
    EditAccInfoDomainModel(
        firstName = this.firstName,
        lastName = this.lastName,
        emailId = this.email,
        mobile = this.phoneNumber.toMobileNumber(),
        countryCode = this.phoneNumber.toCountryCode(),
        mm = this.birthDate.toMM(),
        dd = this.birthDate.toDD(),
        yyyy = this.birthDate.toYYYY()
    )


fun UserInfoFirestoreModel.toUserInfoDomainModel() =
    UserInfoDomainModel(
        firstName = this.firstName,
        lastName = this.lastName,
        emailId = this.email,
        mobile = this.phoneNumber.toMobileNumber(),
        countryCode = this.phoneNumber.toCountryCode().toString(),
        mm = this.birthDate.toMM(),
        dd = this.birthDate.toDD(),
        yyyy = this.birthDate.toYYYY(),
        addressList = this.addressList.toShippingAddressDomainModelList(),
        collectionId = this.collectionId,
        isVerified = this.isVerified
    )



fun AddressFirestoreModel.toShippingAddressDomainModel(id : Int) =
    ShippingAddressDomainModel(
        id = id,
        firstName = this.firstName,
        lastName = this.lastName,
        addressLine1 = this.addressLine1,
        addressLine2 = this.addressLine2,
        city = this.city,
        country = this.country,
        isSelected = this.default,
        uid = this.uid,
        postalCode = this.zipCode,
        state = this.state
    )

fun List<AddressFirestoreModel>.toShippingAddressDomainModelList() =
    run {
        val list : ArrayList<ShippingAddressDomainModel> = arrayListOf()
        for(index in this.indices)  {
            list.add(this[index].toShippingAddressDomainModel(index))
        }
        list
    }

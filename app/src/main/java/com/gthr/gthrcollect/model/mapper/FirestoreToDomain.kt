package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.model.domain.EditAccInfoDomainModel
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
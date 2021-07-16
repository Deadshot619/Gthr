package com.gthr.gthrcollect.model.domain

data class EditAccInfoDomainModel(
    var firstName: String = "",
    var lastName: String = "",
    var dd: String = "",
    var mm: String = "",
    var yyyy: String = "",
    var mobile: String = "",
    var emailId: String = "",
    var countryCode: Int = 0,
)

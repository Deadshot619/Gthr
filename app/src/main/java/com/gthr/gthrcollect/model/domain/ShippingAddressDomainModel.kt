package com.gthr.gthrcollect.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShippingAddressDomainModel(
    var id : Int = -1,
    var uid: String,
    var firstName: String,
    var lastName: String,
    var addressLine1: String,
    var addressLine2: String,
    var city: String,
    var state: String,
    var country: String,
    var postalCode: String,
    var isSelected: Boolean,
    var addresKey: Int=0,
) : Parcelable

package com.gthr.gthrcollect.model.domain

data class ShippingAddress(
    val firstName: String,
    val lastName: String,
    val addressLine1: String,
    val addressLine2: String,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: String,
    val isSelected: Boolean
)

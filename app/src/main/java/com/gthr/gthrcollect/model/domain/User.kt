package com.gthr.gthrcollect.model.domain

data class User(
    val email: String?,
    val uid: String,
    val isEmailVerified: Boolean,
    val displayName: String?,
    val phoneNumber: String?
)

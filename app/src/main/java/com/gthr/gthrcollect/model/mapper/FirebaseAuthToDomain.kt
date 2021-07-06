package com.gthr.gthrcollect.model.mapper

import com.google.firebase.auth.FirebaseUser
import com.gthr.gthrcollect.model.domain.User

fun FirebaseUser.toUser() = User(
    email = this.email,
    uid = this.uid,
    isEmailVerified = isEmailVerified,
    phoneNumber = phoneNumber,
    displayName = displayName
)
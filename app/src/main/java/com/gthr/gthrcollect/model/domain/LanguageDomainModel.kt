package com.gthr.gthrcollect.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LanguageDomainModel(
    val key: Int,
    val displayName: String,
    val abbreviatedName: String
) : Parcelable
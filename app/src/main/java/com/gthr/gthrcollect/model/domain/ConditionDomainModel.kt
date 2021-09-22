package com.gthr.gthrcollect.model.domain

import android.os.Parcelable
import com.gthr.gthrcollect.utils.enums.ConditionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConditionDomainModel(
    val key: Int,
    val displayName: String,
    val type: ConditionType,
    val abbreviatedName: String
) : Parcelable
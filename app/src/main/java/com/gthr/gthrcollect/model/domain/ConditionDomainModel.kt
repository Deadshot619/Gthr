package com.gthr.gthrcollect.model.domain

import com.gthr.gthrcollect.utils.enums.ConditionType

data class ConditionDomainModel(
    val key: Int,
    val displayName: String,
    val type: ConditionType
)
package com.gthr.gthrcollect.model.domain

import com.gthr.gthrcollect.utils.enums.AdapterViewType

data class SearchCollection(
    val objectId : String?,
    val profileUrl : String?,
    val name : String?,
    val productImage : String?,
    val viewType : AdapterViewType = AdapterViewType.VIEW_TYPE_ITEM
)

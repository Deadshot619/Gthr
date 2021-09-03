package com.gthr.gthrcollect.model.mapper

import com.gthr.gthrcollect.model.domain.SearchProductDomainModel
import com.gthr.gthrcollect.model.network.cloudfunction.SearchProductModel
import com.gthr.gthrcollect.utils.getProductType

fun SearchProductModel.toSearchProductDomainModel() =
    SearchProductDomainModel(
        title = title,
        productType = getProductType(productType!!),
        objectID = objectID,
        numberOfFavorites = numberOfFavorites,

    )


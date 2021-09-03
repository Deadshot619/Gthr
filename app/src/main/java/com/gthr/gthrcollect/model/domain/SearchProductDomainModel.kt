package com.gthr.gthrcollect.model.domain

import com.google.gson.annotations.SerializedName
import com.gthr.gthrcollect.utils.enums.ProductType

data class SearchProductDomainModel(

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("productType")
	val productType: ProductType? = null,

	@field:SerializedName("objectID")
	val objectID: String? = null,

	@field:SerializedName("numberOfFavorites")
	val numberOfFavorites: Long? = null
)


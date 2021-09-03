package com.gthr.gthrcollect.model.network.cloudfunction

import com.google.gson.annotations.SerializedName

data class SearchProductModel(

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("productType")
	val productType: String? = null,

	@field:SerializedName("objectID")
	val objectID: String? = null,

	@field:SerializedName("numberOfFavorites")
	val numberOfFavorites: Long? = null
)


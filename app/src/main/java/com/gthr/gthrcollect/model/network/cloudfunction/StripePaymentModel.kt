package com.gthr.gthrcollect.model.network.cloudfunction

import com.google.gson.annotations.SerializedName

data class StripePaymentModel(

	@field:SerializedName("appFee")
	var appFee: String? = "",

	@field:SerializedName("sellerPayout")
	var sellerPayout: String? = null,

	@field:SerializedName("buyerCharge")
	var buyerCharge: String? = null,

	@field:SerializedName("clientSecret")
	var clientSecret: String? = null
)


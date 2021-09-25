package com.gthr.gthrcollect.model.network.firebaserealtimedb

import com.google.firebase.database.PropertyName
import kotlinx.serialization.Serializable

@Serializable
data class ReceiptModel(
    @get:PropertyName("refKey")
    @set:PropertyName("refKey")
    var refKey: String? = null,
    @get:PropertyName("itemRefKey")
    @set:PropertyName("itemRefKey")
    var itemRefKey: String? = null,
    @get:PropertyName("productType")
    @set:PropertyName("productType")
    var productType: String? = null,
    @get:PropertyName("objectID")
    @set:PropertyName("objectID")
    var objectID: String? = null,
    @get:PropertyName("buyerCharge")
    @set:PropertyName("buyerCharge")
    var buyerCharge: String? = null,
    @get:PropertyName("sellerPayout")
    @set:PropertyName("sellerPayout")
    var sellerPayout: String? = null,
    @get:PropertyName("appFee")
    @set:PropertyName("appFee")
    var appFee: String? = null,
    @get:PropertyName("paymentID")
    @set:PropertyName("paymentID")
    var paymentID: String? = null,
    @get:PropertyName("buyerUID")
    @set:PropertyName("buyerUID")
    var buyerUID: String? = null,
    @get:PropertyName("sellerUID")
    @set:PropertyName("sellerUID")
    var sellerUID: String? = null,
    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: String? = null,
    @get:PropertyName("saleID")
    @set:PropertyName("saleID")
    var saleID: String? = null,
    @get:PropertyName("trackingNumber")
    @set:PropertyName("trackingNumber")
    var trackingNumber: String? = null,
    @get:PropertyName("abbrevaitedPaymentNumber")
    @set:PropertyName("abbrevaitedPaymentNumber")
    var abbrevaitedPaymentNumber: String? = null,
    @get:PropertyName("paymentProvider")
    @set:PropertyName("paymentProvider")
    var paymentProvider: String? = null,
    @get:PropertyName("trackingLink")
    @set:PropertyName("trackingLink")
    var trackingLink: String? = null,
    @get:PropertyName("orderStatus")
    @set:PropertyName("orderStatus")
    var orderStatus: String? = null,
    @get:PropertyName("shippingTierKey")
    @set:PropertyName("shippingTierKey")
    var shippingTierKey: String? = null,
    @get:PropertyName("buyerShippingName")
    @set:PropertyName("buyerShippingName")
    var buyerShippingName: String? = null,
    @get:PropertyName("buyerShippingAddressLine1")
    @set:PropertyName("buyerShippingAddressLine1")
    var buyerShippingAddressLine1: String? = null,
    @get:PropertyName("buyerShippingAddressLine2")
    @set:PropertyName("buyerShippingAddressLine2")
    var buyerShippingAddressLine2: String? = null,
    @get:PropertyName("buyerShippingCity")
    @set:PropertyName("buyerShippingCity")
    var buyerShippingCity: String? = null,
    @get:PropertyName("buyerShippingState")
    @set:PropertyName("buyerShippingState")
    var buyerShippingState: String? = null,
    @get:PropertyName("buyerShippingZipCode")
    @set:PropertyName("buyerShippingZipCode")
    var buyerShippingZipCode: String? = null,
    @get:PropertyName("buyerShippingCountry")
    @set:PropertyName("buyerShippingCountry")
    var buyerShippingCountry: String? = null,
    @get:PropertyName("sellerShippingName")
    @set:PropertyName("sellerShippingName")
    var sellerShippingName: String? = null,
    @get:PropertyName("sellerShippingAddressLine1")
    @set:PropertyName("sellerShippingAddressLine1")
    var sellerShippingAddressLine1: String? = null,
    @get:PropertyName("sellerShippingAddressLine2")
    @set:PropertyName("sellerShippingAddressLine2")
    var sellerShippingAddressLine2: String? = null,
    @get:PropertyName("sellerShippingCity")
    @set:PropertyName("sellerShippingCity")
    var sellerShippingCity: String? = null,
    @get:PropertyName("sellerShippingState")
    @set:PropertyName("sellerShippingState")
    var sellerShippingState: String? = null,
    @get:PropertyName("sellerShippingZipCode")
    @set:PropertyName("sellerShippingZipCode")
    var sellerShippingZipCode: String? = null,
    @get:PropertyName("sellerShippingCountry")
    @set:PropertyName("sellerShippingCountry")
    var sellerShippingCountry: String? = null,
    @get:PropertyName("edition")
    @set:PropertyName("edition")
    var edition: String? = null,
    @get:PropertyName("lang")
    @set:PropertyName("lang")
    var lang: Int? = null,
    @get:PropertyName("condition")
    @set:PropertyName("condition")
    var condition: String? = null,
)

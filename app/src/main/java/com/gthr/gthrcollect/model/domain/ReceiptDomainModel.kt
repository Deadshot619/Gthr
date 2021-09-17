package com.gthr.gthrcollect.model.domain

import android.os.Parcelable
import com.gthr.gthrcollect.utils.enums.ProductType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReceiptDomainModel(
    //temporary created & will be used to send data from ask flow
    var imageUrl: String? = null,
    var totalAskPrice: Double? = null,
    var shippingReimbursement: Double? = null,
    var refKey: String? = null,
    var itemRefKey: String? = null,
    var productType: ProductType? = null,
    var objectID: String? = null,
    var buyerCharge: String? = null,
    var sellerPayout: String? = null,
    var appFee: String? = null,
    var paymentID: String? = null,
    var buyerUID: String? = null,
    var sellerUID: String? = null,
    var date: String? = null,
    var saleID: String? = null,
    var trackingNumber: String? = null,
    var abbrevaitedPaymentNumber: String? = null,
    var paymentProvider: String? = null,
    var trackingLink: String? = null,
    var orderStatus: String? = null,
    var shippingTierKey: String? = null,
    var buyerShippingName: String? = null,
    var buyerShippingAddressLine1: String? = null,
    var buyerShippingAddressLine2: String? = null,
    var buyerShippingCity: String? = null,
    var buyerShippingState: String? = null,
    var buyerShippingZipCode: String? = null,
    var buyerShippingCountry: String? = null,
    var sellerShippingName: String? = null,
    var sellerShippingAddressLine1: String? = null,
    var sellerShippingAddressLine2: String? = null,
    var sellerShippingCity: String? = null,
    var sellerShippingState: String? = null,
    var sellerShippingZipCode: String? = null,
    var sellerShippingCountry: String? = null,
    var edition: String? = null,
    var lang: Int? = null,
    var condition: String? = null,
    var order_status: String? = null,
) : Parcelable

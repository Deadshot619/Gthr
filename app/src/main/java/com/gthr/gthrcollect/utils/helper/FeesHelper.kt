package com.gthr.gthrcollect.utils.helper

import com.gthr.gthrcollect.utils.extensions.toTwoDecimal

private const val PERCENT_SELLING_FEE = 8.5
private const val PERCENT_PAYMENT_PROCESSING = 2.9

fun Double?.getPaymentProcessing(): Double =
    (((this ?: 0.00) * PERCENT_PAYMENT_PROCESSING) / 100.00).toTwoDecimal()

fun Double?.getSellingFee(): Double =
    (((this ?: 0.00) * PERCENT_SELLING_FEE) / 100.00).toTwoDecimal()
package com.gthr.gthrcollect.utils.helper

import com.gthr.gthrcollect.utils.customviews.CustomDeliveryButton


fun getOrderStatusFromRaw(status: String): CustomDeliveryButton.OrderStatus {

    return when (status) {

        "delivered" -> CustomDeliveryButton.OrderStatus.DELIVERED

        "ordered" -> CustomDeliveryButton.OrderStatus.ORDERED

        else -> CustomDeliveryButton.OrderStatus.PENDING
    }
}
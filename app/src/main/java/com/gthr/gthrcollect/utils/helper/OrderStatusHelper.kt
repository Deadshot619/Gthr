package com.gthr.gthrcollect.utils.helper

import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.utils.customviews.CustomDeliveryButton
import com.gthr.gthrcollect.utils.enums.ProductCategory


fun getOrderStatusFromRaw(status: String): CustomDeliveryButton.OrderStatus {

    return when (status) {

        "pending" -> CustomDeliveryButton.OrderStatus.PENDING

        "ordered" -> CustomDeliveryButton.OrderStatus.ORDERED

        else -> CustomDeliveryButton.OrderStatus.DELIVERED
    }
}
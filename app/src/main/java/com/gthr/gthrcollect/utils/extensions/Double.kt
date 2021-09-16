package com.gthr.gthrcollect.utils.extensions

import java.math.RoundingMode

fun Double.toTwoDecimal(): Double =
    this.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
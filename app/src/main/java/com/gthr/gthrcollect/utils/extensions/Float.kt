package com.gthr.gthrcollect.utils.extensions

import java.math.RoundingMode

fun Float.toTwoDecimal(): Float = this.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toFloat()
package com.gthr.gthrcollect.utils.helper

import com.gthr.gthrcollect.model.domain.ConditionDomainModel
import com.gthr.gthrcollect.utils.enums.ConditionType

private fun getCondition(key: Int): ConditionDomainModel {
    return when (key) {
        -1 -> ConditionDomainModel(key = key, displayName = "New", type = ConditionType.NEW)
        0 -> ConditionDomainModel(key = key, displayName = "PSA 10", type = ConditionType.PSA)
        1 -> ConditionDomainModel(key = key, displayName = "PSA 9.5", type = ConditionType.PSA)
        2 -> ConditionDomainModel(key = key, displayName = "PSA 9", type = ConditionType.PSA)
        3 -> ConditionDomainModel(key = key, displayName = "PSA 8.5", type = ConditionType.PSA)
        4 -> ConditionDomainModel(key = key, displayName = "PSA 8", type = ConditionType.PSA)
        5 -> ConditionDomainModel(key = key, displayName = "PSA 7.5", type = ConditionType.PSA)
        6 -> ConditionDomainModel(key = key, displayName = "PSA 7", type = ConditionType.PSA)
        7 -> ConditionDomainModel(key = key, displayName = "PSA 6.5", type = ConditionType.PSA)
        8 -> ConditionDomainModel(key = key, displayName = "PSA 6", type = ConditionType.PSA)
        9 -> ConditionDomainModel(key = key, displayName = "PSA 5.5", type = ConditionType.PSA)
        10 -> ConditionDomainModel(key = key, displayName = "PSA 5", type = ConditionType.PSA)
        11 -> ConditionDomainModel(key = key, displayName = "PSA 4.5", type = ConditionType.PSA)
        12 -> ConditionDomainModel(key = key, displayName = "PSA 4", type = ConditionType.PSA)
        13 -> ConditionDomainModel(key = key, displayName = "PSA 3.5", type = ConditionType.PSA)
        14 -> ConditionDomainModel(key = key, displayName = "PSA 3", type = ConditionType.PSA)
        15 -> ConditionDomainModel(key = key, displayName = "PSA 2.5", type = ConditionType.PSA)
        16 -> ConditionDomainModel(key = key, displayName = "PSA 2", type = ConditionType.PSA)
        17 -> ConditionDomainModel(key = key, displayName = "PSA 1.5", type = ConditionType.PSA)
        18 -> ConditionDomainModel(key = key, displayName = "PSA 1", type = ConditionType.PSA)
        19 -> ConditionDomainModel(key = key, displayName = "Black Label 10", type = ConditionType.BGS)
        20 -> ConditionDomainModel(key = key, displayName = "BGS 10", type = ConditionType.BGS)
        21 -> ConditionDomainModel(key = key, displayName = "BGS 9.5", type = ConditionType.BGS)
        22 -> ConditionDomainModel(key = key, displayName = "BGS 9", type = ConditionType.BGS)
        23 -> ConditionDomainModel(key = key, displayName = "BGS 8.5", type = ConditionType.BGS)
        24 -> ConditionDomainModel(key = key, displayName = "BGS 8", type = ConditionType.BGS)
        25 -> ConditionDomainModel(key = key, displayName = "BGS 7.5", type = ConditionType.BGS)
        26 -> ConditionDomainModel(key = key, displayName = "BGS 7", type = ConditionType.BGS)
        27 -> ConditionDomainModel(key = key, displayName = "BGS 6.5", type = ConditionType.BGS)
        28 -> ConditionDomainModel(key = key, displayName = "BGS 6", type = ConditionType.BGS)
        29 -> ConditionDomainModel(key = key, displayName = "BGS 5.5", type = ConditionType.BGS)
        30 -> ConditionDomainModel(key = key, displayName = "BGS 5", type = ConditionType.BGS)
        31 -> ConditionDomainModel(key = key, displayName = "BGS 4.5", type = ConditionType.BGS)
        32 -> ConditionDomainModel(key = key, displayName = "BGS 4", type = ConditionType.BGS)
        33 -> ConditionDomainModel(key = key, displayName = "BGS 3.5", type = ConditionType.BGS)
        34 -> ConditionDomainModel(key = key, displayName = "BGS 3", type = ConditionType.BGS)
        35 -> ConditionDomainModel(key = key, displayName = "BGS 2.5", type = ConditionType.BGS)
        36 -> ConditionDomainModel(key = key, displayName = "BGS 2", type = ConditionType.BGS)
        37 -> ConditionDomainModel(key = key, displayName = "BGS 1.5", type = ConditionType.BGS)
        38 -> ConditionDomainModel(key = key, displayName = "BGS 1", type = ConditionType.BGS)
        39 -> ConditionDomainModel(key = key, displayName = "Gem Mint", type = ConditionType.UG)
        40 -> ConditionDomainModel(key = key, displayName = "Near Mint", type = ConditionType.UG)
        41 -> ConditionDomainModel(key = key, displayName = "Lightly Played", type = ConditionType.UG)
        42 -> ConditionDomainModel(key = key, displayName = "Moderately Played", type = ConditionType.UG)
        43 -> ConditionDomainModel(key = key, displayName = "Heavily Played", type = ConditionType.UG)
        44 -> ConditionDomainModel(key = key, displayName = "Damaged", type = ConditionType.UG)
        45 -> ConditionDomainModel(key = key, displayName = "CGC P10", type = ConditionType.CGC)
        46 -> ConditionDomainModel(key = key, displayName = "CGC 10", type = ConditionType.CGC)
        47 -> ConditionDomainModel(key = key, displayName = "CGC 9.5", type = ConditionType.CGC)
        48 -> ConditionDomainModel(key = key, displayName = "CGC 9", type = ConditionType.CGC)
        49 -> ConditionDomainModel(key = key, displayName = "CGC 8.5", type = ConditionType.CGC)
        50 -> ConditionDomainModel(key = key, displayName = "CGC 8", type = ConditionType.CGC)
        51 -> ConditionDomainModel(key = key, displayName = "CGC 7.5", type = ConditionType.CGC)
        52 -> ConditionDomainModel(key = key, displayName = "CGC 7", type = ConditionType.CGC)
        53 -> ConditionDomainModel(key = key, displayName = "CGC 6.5", type = ConditionType.CGC)
        54 -> ConditionDomainModel(key = key, displayName = "CGC 6", type = ConditionType.CGC)
        55 -> ConditionDomainModel(key = key, displayName = "CGC 5.5", type = ConditionType.CGC)
        56 -> ConditionDomainModel(key = key, displayName = "CGC 5", type = ConditionType.CGC)
        57 -> ConditionDomainModel(key = key, displayName = "CGC 4.5", type = ConditionType.CGC)
        58 -> ConditionDomainModel(key = key, displayName = "CGC 4", type = ConditionType.CGC)
        59 -> ConditionDomainModel(key = key, displayName = "CGC 3.5", type = ConditionType.CGC)
        60 -> ConditionDomainModel(key = key, displayName = "CGC 3", type = ConditionType.CGC)
        61 -> ConditionDomainModel(key = key, displayName = "CGC 2.5", type = ConditionType.CGC)
        62 -> ConditionDomainModel(key = key, displayName = "CGC 2", type = ConditionType.CGC)
        63 -> ConditionDomainModel(key = key, displayName = "CGC 1.5", type = ConditionType.CGC)
        64 -> ConditionDomainModel(key = key, displayName = "CGC 1", type = ConditionType.CGC)
        else -> ConditionDomainModel(key = key, displayName = "", type = ConditionType.CGC)
    }
}

fun getConditionList(conditionType: ConditionType): List<ConditionDomainModel> {
    val conditionList = arrayListOf<ConditionDomainModel>()
    return when (conditionType) {
        ConditionType.PSA -> {
            for (i in 0..18) {
                conditionList.add(getCondition(i))
            }
            conditionList
        }
        ConditionType.BGS -> {
            for (i in 19..38) {
                conditionList.add(getCondition(i))
            }
            conditionList
        }
        ConditionType.UG -> {
            for (i in 39..44) {
                conditionList.add(getCondition(i))
            }
            conditionList
        }
        ConditionType.CGC -> {
            for (i in 45..64) {
                conditionList.add(getCondition(i))
            }
            conditionList
        }
        else -> conditionList
    }
}
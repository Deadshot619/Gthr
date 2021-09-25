package com.gthr.gthrcollect.utils.helper

import android.content.Context
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.model.domain.ConditionDomainModel
import com.gthr.gthrcollect.utils.enums.ConditionType

fun getCondition(key: Int): ConditionDomainModel {
    return when (key) {
        -1 -> ConditionDomainModel(
            key = key,
            displayName = "New",
            type = ConditionType.NEW,
            abbreviatedName = "New"
        )
        0 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 10",
            type = ConditionType.PSA,
            abbreviatedName = "10"
        )
        1 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 9.5",
            type = ConditionType.PSA,
            abbreviatedName = "9.5"
        )
        2 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 9",
            type = ConditionType.PSA,
            abbreviatedName = "9"
        )
        3 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 8.5",
            type = ConditionType.PSA,
            abbreviatedName = "8.5"
        )
        4 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 8",
            type = ConditionType.PSA,
            abbreviatedName = "8"
        )
        5 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 7.5",
            type = ConditionType.PSA,
            abbreviatedName = "7.5"
        )
        6 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 7",
            type = ConditionType.PSA,
            abbreviatedName = "7"
        )
        7 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 6.5",
            type = ConditionType.PSA,
            abbreviatedName = "6.5"
        )
        8 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 6",
            type = ConditionType.PSA,
            abbreviatedName = "6"
        )
        9 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 5.5",
            type = ConditionType.PSA,
            abbreviatedName = "5.5"
        )
        10 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 5",
            type = ConditionType.PSA,
            abbreviatedName = "5"
        )
        11 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 4.5",
            type = ConditionType.PSA,
            abbreviatedName = "4.5"
        )
        12 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 4",
            type = ConditionType.PSA,
            abbreviatedName = "4"
        )
        13 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 3.5",
            type = ConditionType.PSA,
            abbreviatedName = "3.5"
        )
        14 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 3",
            type = ConditionType.PSA,
            abbreviatedName = "3"
        )
        15 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 2.5",
            type = ConditionType.PSA,
            abbreviatedName = "2.5"
        )
        16 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 2",
            type = ConditionType.PSA,
            abbreviatedName = "2"
        )
        17 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 1.5",
            type = ConditionType.PSA,
            abbreviatedName = "1.5"
        )
        18 -> ConditionDomainModel(
            key = key,
            displayName = "PSA 1",
            type = ConditionType.PSA,
            abbreviatedName = "1"
        )
        19 -> ConditionDomainModel(
            key = key,
            displayName = "Black Label 10",
            type = ConditionType.BGS,
            abbreviatedName = "BL 10"
        )
        20 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 10",
            type = ConditionType.BGS,
            abbreviatedName = "10"
        )
        21 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 9.5",
            type = ConditionType.BGS,
            abbreviatedName = "9.5"
        )
        22 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 9",
            type = ConditionType.BGS,
            abbreviatedName = "9"
        )
        23 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 8.5",
            type = ConditionType.BGS,
            abbreviatedName = "8.5"
        )
        24 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 8",
            type = ConditionType.BGS,
            abbreviatedName = "8"
        )
        25 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 7.5",
            type = ConditionType.BGS,
            abbreviatedName = "7.5"
        )
        26 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 7",
            type = ConditionType.BGS,
            abbreviatedName = "7"
        )
        27 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 6.5",
            type = ConditionType.BGS,
            abbreviatedName = "6.5"
        )
        28 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 6",
            type = ConditionType.BGS,
            abbreviatedName = "6"
        )
        29 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 5.5",
            type = ConditionType.BGS,
            abbreviatedName = "5.5"
        )
        30 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 5",
            type = ConditionType.BGS,
            abbreviatedName = "5"
        )
        31 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 4.5",
            type = ConditionType.BGS,
            abbreviatedName = "4.5"
        )
        32 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 4",
            type = ConditionType.BGS,
            abbreviatedName = "4"
        )
        33 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 3.5",
            type = ConditionType.BGS,
            abbreviatedName = "3.5"
        )
        34 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 3",
            type = ConditionType.BGS,
            abbreviatedName = "3"
        )
        35 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 2.5",
            type = ConditionType.BGS,
            abbreviatedName = "2.5"
        )
        36 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 2",
            type = ConditionType.BGS,
            abbreviatedName = "2"
        )
        37 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 1.5",
            type = ConditionType.BGS,
            abbreviatedName = "1.5"
        )
        38 -> ConditionDomainModel(
            key = key,
            displayName = "BGS 1",
            type = ConditionType.BGS,
            abbreviatedName = "1"
        )
        39 -> ConditionDomainModel(
            key = key,
            displayName = "Gem Mint",
            type = ConditionType.UG,
            abbreviatedName = "GM"
        )
        40 -> ConditionDomainModel(
            key = key,
            displayName = "Near Mint",
            type = ConditionType.UG,
            abbreviatedName = "NM"
        )
        41 -> ConditionDomainModel(
            key = key,
            displayName = "Lightly Played",
            type = ConditionType.UG,
            abbreviatedName = "LP"
        )
        42 -> ConditionDomainModel(
            key = key,
            displayName = "Moderately Played",
            type = ConditionType.UG,
            abbreviatedName = "MP"
        )
        43 -> ConditionDomainModel(
            key = key,
            displayName = "Heavily Played",
            type = ConditionType.UG,
            abbreviatedName = "HP"
        )
        44 -> ConditionDomainModel(
            key = key,
            displayName = "Damaged",
            type = ConditionType.UG,
            abbreviatedName = "D"
        )
        45 -> ConditionDomainModel(
            key = key,
            displayName = "CGC P10",
            type = ConditionType.CGC,
            abbreviatedName = "P10"
        )
        46 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 10",
            type = ConditionType.CGC,
            abbreviatedName = " 10"
        )
        47 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 9.5",
            type = ConditionType.CGC,
            abbreviatedName = "9.5"
        )
        48 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 9",
            type = ConditionType.CGC,
            abbreviatedName = "9"
        )
        49 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 8.5",
            type = ConditionType.CGC,
            abbreviatedName = "8.5"
        )
        50 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 8",
            type = ConditionType.CGC,
            abbreviatedName = "8"
        )
        51 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 7.5",
            type = ConditionType.CGC,
            abbreviatedName = "7.5"
        )
        52 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 7",
            type = ConditionType.CGC,
            abbreviatedName = "7"
        )
        53 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 6.5",
            type = ConditionType.CGC,
            abbreviatedName = "6.5"
        )
        54 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 6",
            type = ConditionType.CGC,
            abbreviatedName = "6"
        )
        55 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 5.5",
            type = ConditionType.CGC,
            abbreviatedName = "5.5"
        )
        56 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 5",
            type = ConditionType.CGC,
            abbreviatedName = "5"
        )
        57 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 4.5",
            type = ConditionType.CGC,
            abbreviatedName = "4.5"
        )
        58 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 4",
            type = ConditionType.CGC,
            abbreviatedName = "4"
        )
        59 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 3.5",
            type = ConditionType.CGC,
            abbreviatedName = "3.5"
        )
        60 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 3",
            type = ConditionType.CGC,
            abbreviatedName = "3"
        )
        61 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 2.5",
            type = ConditionType.CGC,
            abbreviatedName = "2.5"
        )
        62 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 2",
            type = ConditionType.CGC,
            abbreviatedName = "2"
        )
        63 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 1.5",
            type = ConditionType.CGC,
            abbreviatedName = "1.5"
        )
        64 -> ConditionDomainModel(
            key = key,
            displayName = "CGC 1",
            type = ConditionType.CGC,
            abbreviatedName = "1"
        )
        else -> ConditionDomainModel(
            key = key,
            displayName = "",
            type = ConditionType.CGC,
            abbreviatedName = ""
        )
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

fun Context.getConditionTitle(conditionType: ConditionType) : String{
    return when (conditionType) {
        ConditionType.UG -> getString(R.string.raw)
        ConditionType.PSA -> getString(R.string.psa)
        ConditionType.BGS -> getString(R.string.bgs)
        ConditionType.CGC -> getString(R.string.cgc)
        else -> getString(R.string.text_new_condition)
    }
}

fun getConditionFromDisplayName(displayName: String): ConditionDomainModel {
    if (displayName.isEmpty() || displayName.equals(getCondition(-1).displayName, true))
        return getCondition(-1)

    for (i in 0..64)
        if (getCondition(i).displayName.equals(displayName, ignoreCase = true))
            return getCondition(i)
    return getCondition(-1)
}

fun getConditionFromRaw(raw: String): ConditionType = when {
    raw.equals(ConditionType.UG.title, true) -> ConditionType.UG
    raw.equals(ConditionType.PSA.title, true) -> ConditionType.PSA
    raw.equals(ConditionType.BGS.title, true) -> ConditionType.BGS
    raw.equals(ConditionType.CGC.title, true) -> ConditionType.CGC
    else -> ConditionType.NEW
}
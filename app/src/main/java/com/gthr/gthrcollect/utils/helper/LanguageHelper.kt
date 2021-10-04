package com.gthr.gthrcollect.utils.helper

import com.gthr.gthrcollect.model.domain.LanguageDomainModel
import com.gthr.gthrcollect.model.domain.PokemonDomainModel
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.model.domain.YugiohDomainModel
import com.gthr.gthrcollect.utils.enums.ProductType

fun getYugiohLangList(item: YugiohDomainModel): List<LanguageDomainModel> {
    val langList = arrayListOf<LanguageDomainModel>()
    if (item.english > 0) langList.add(getYugiohLanguageDomainModel(0))
    if (item.french > 0) langList.add(getYugiohLanguageDomainModel(1))
    if (item.german > 0) langList.add(getYugiohLanguageDomainModel(2))
    if (item.italian > 0) langList.add(getYugiohLanguageDomainModel(3))
    if (item.spanish > 0) langList.add(getYugiohLanguageDomainModel(4))
    if (item.portuguese > 0) langList.add(getYugiohLanguageDomainModel(5))
    if (item.korean > 0) langList.add(getYugiohLanguageDomainModel(6))
    if (item.japanese > 0) langList.add(getYugiohLanguageDomainModel(7))
    if (item.asianEnglish > 0) langList.add(getYugiohLanguageDomainModel(8))
    if (item.euroEnglish > 0) langList.add(getYugiohLanguageDomainModel(9))
    if (item.frenchOG > 0) langList.add(getYugiohLanguageDomainModel(10))
    if (item.germanOG > 0) langList.add(getYugiohLanguageDomainModel(11))
    if (item.italianOG > 0) langList.add(getYugiohLanguageDomainModel(12))
    if (item.koreanOG > 0) langList.add(getYugiohLanguageDomainModel(13))
    if (item.spanishOG > 0) langList.add(getYugiohLanguageDomainModel(14))
    if (item.portugueseOG > 0) langList.add(getYugiohLanguageDomainModel(15))
    if (item.oceanicEnglish > 0) langList.add(getYugiohLanguageDomainModel(16))
    if (item.frenchCanadian > 0) langList.add(getYugiohLanguageDomainModel(17))
    if (item.englishOG > 0) langList.add(getYugiohLanguageDomainModel(18))
    if (item.asianEnglishOG > 0) langList.add(getYugiohLanguageDomainModel(19))
    return langList
}

fun getYugiohLanguageDomainModel(key: Int): LanguageDomainModel {
    return when (key) {
        0 -> LanguageDomainModel(key = key, displayName = "English (-EN)", abbreviatedName = "Eng")
        1 -> LanguageDomainModel(key = key, displayName = "French (-FR)", abbreviatedName = "Fr")
        2 -> LanguageDomainModel(key = key, displayName = "German (-DE)", abbreviatedName = "De")
        3 -> LanguageDomainModel(key = key, displayName = "Italian (-IT)", abbreviatedName = "It")
        4 -> LanguageDomainModel(key = key, displayName = "Spanish (-SP)", abbreviatedName = "Sp")
        5 -> LanguageDomainModel(
            key = key,
            displayName = "Portuguese (-PT)",
            abbreviatedName = "Pt"
        )
        6 -> LanguageDomainModel(key = key, displayName = "Korean (-KR)", abbreviatedName = "Kr")
        7 -> LanguageDomainModel(key = key, displayName = "Japanese (-JP)", abbreviatedName = "Jp")
        8 -> LanguageDomainModel(
            key = key,
            displayName = "Asian-English (-AE)",
            abbreviatedName = "AE"
        )
        9 -> LanguageDomainModel(
            key = key,
            displayName = "European English (-E/-EN)",
            abbreviatedName = "EU"
        )
        10 -> LanguageDomainModel(key = key, displayName = "French (-F)", abbreviatedName = "Fr")
        11 -> LanguageDomainModel(key = key, displayName = "German (-G)", abbreviatedName = "De")
        12 -> LanguageDomainModel(key = key, displayName = "Italian (-I)", abbreviatedName = "It")
        13 -> LanguageDomainModel(key = key, displayName = "Korean (-K)", abbreviatedName = "Kr")
        14 -> LanguageDomainModel(key = key, displayName = "Spanish (-S)", abbreviatedName = "Sp")
        15 -> LanguageDomainModel(
            key = key,
            displayName = "Portuguese (-P)",
            abbreviatedName = "Pt"
        )
        16 -> LanguageDomainModel(
            key = key,
            displayName = "Oceanic English (-A)",
            abbreviatedName = "Au"
        )
        17 -> LanguageDomainModel(
            key = key,
            displayName = "French-Canadian (-C)",
            abbreviatedName = "FrCa"
        )
        18 -> LanguageDomainModel(key = key, displayName = "English", abbreviatedName = "Eng")
        19 -> LanguageDomainModel(
            key = key,
            displayName = "Asian-English (-AE)",
            abbreviatedName = "AE"
        )
        else -> LanguageDomainModel(key = key, displayName = "", abbreviatedName = "")
    }
}

fun getPokemonLangList(item: PokemonDomainModel): List<LanguageDomainModel> {
    val langList = arrayListOf<LanguageDomainModel>()
    if (item.japanese == 0 && item.english == 0) langList.add(getPokemonLanguageDomainModel(2))
    if (item.english > 0) langList.add(getPokemonLanguageDomainModel(0))
    if (item.japanese > 0) langList.add(getPokemonLanguageDomainModel(1))
    if (item.noLanguagenoEdition > 0) langList.add(getPokemonLanguageDomainModel(2))
    return langList
}

fun getPokemonLanguageDomainModel(key: Int): LanguageDomainModel {
    return when (key) {
        0 -> LanguageDomainModel(key = key, displayName = "English", abbreviatedName = "Eng")
        1 -> LanguageDomainModel(key = key, displayName = "Japanese", abbreviatedName = "Jp")
        2 -> LanguageDomainModel(key = key, displayName = "None", abbreviatedName = "NA")
        else -> LanguageDomainModel(key = key, "", "")
    }
}

fun isPromo(productModel: ProductDisplayModel, pokemonModel: PokemonDomainModel): Boolean {
    if (productModel.productType == ProductType.POKEMON) {
        if (pokemonModel.noLanguagenoEdition != 0) {
            return true
        }
    }
    return false
}

fun getMTGLangList(): List<LanguageDomainModel> {
    val languageList = arrayListOf<LanguageDomainModel>()
    for (i in 1..10) {
        languageList.add(getMTGLanguage(i))
    }
    return languageList
}

fun getMTGLanguage(key: Int): LanguageDomainModel {
    return when (key) {
        0 -> LanguageDomainModel(key = key, displayName = "English", abbreviatedName = "Eng")
        1 -> LanguageDomainModel(key = key, displayName = "French", abbreviatedName = "Fr")
        2 -> LanguageDomainModel(key = key, displayName = "German", abbreviatedName = "De")
        3 -> LanguageDomainModel(key = key, displayName = "Italian", abbreviatedName = "It")
        4 -> LanguageDomainModel(key = key, displayName = "Japanese", abbreviatedName = "Jp")
        5 -> LanguageDomainModel(key = key, displayName = "Chinese (S)", abbreviatedName = "Ch(S)")
        6 -> LanguageDomainModel(key = key, displayName = "Chinese (T)", abbreviatedName = "Ch(T)")
        7 -> LanguageDomainModel(key = key, displayName = "Korean", abbreviatedName = "Kr")
        8 -> LanguageDomainModel(key = key, displayName = "Portuguese", abbreviatedName = "Pt")
        9 -> LanguageDomainModel(key = key, displayName = "Russian", abbreviatedName = "Ru")
        10 -> LanguageDomainModel(key = key, displayName = "Spanish", abbreviatedName = "Sp")
        else -> LanguageDomainModel(key = key, displayName = "", abbreviatedName = "")
    }
}

fun getLanguageDomainModelFromKey(key : Int, productType: ProductType) : LanguageDomainModel? = when (productType) {
    ProductType.MAGIC_THE_GATHERING -> getMTGLanguage(key)
    ProductType.POKEMON -> getPokemonLanguageDomainModel(key)
    ProductType.YUGIOH -> getYugiohLanguageDomainModel(key)
    else -> null
}
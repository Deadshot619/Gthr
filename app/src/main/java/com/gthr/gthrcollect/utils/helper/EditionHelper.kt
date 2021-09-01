package com.gthr.gthrcollect.utils.helper

import com.gthr.gthrcollect.model.domain.PokemonDomainModel
import com.gthr.gthrcollect.model.domain.YugiohDomainModel
import com.gthr.gthrcollect.utils.enums.EditionType

fun getYugiohEditionKey(langKey: Int, yugiohCard: YugiohDomainModel): Int {
    return when (langKey) {
        0 -> yugiohCard.english
        1 -> yugiohCard.french
        2 -> yugiohCard.german
        3 -> yugiohCard.italian
        4 -> yugiohCard.spanish
        5 -> yugiohCard.portuguese
        6 -> yugiohCard.korean
        7 -> yugiohCard.japanese
        8 -> yugiohCard.asianEnglish
        9 -> yugiohCard.euroEnglish
        10 -> yugiohCard.frenchOG
        11 -> yugiohCard.germanOG
        12 -> yugiohCard.italianOG
        13 -> yugiohCard.koreanOG
        14 -> yugiohCard.spanishOG
        15 -> yugiohCard.portugueseOG
        16 -> yugiohCard.oceanicEnglish
        17 -> yugiohCard.frenchCanadian
        18 -> yugiohCard.englishOG
        19 -> yugiohCard.asianEnglishOG
        else -> -1
    }
}

fun getYugiohEditionList(rawKey: Int): List<EditionType> {
    return when (rawKey) {
        0 -> arrayListOf()
        1 -> arrayListOf(EditionType.FIRST_EDITION)
        2 -> arrayListOf(EditionType.LIMITED_EDITION)
        3 -> arrayListOf(EditionType.UNLIMITED)
        4 -> arrayListOf(EditionType.FIRST_EDITION, EditionType.LIMITED_EDITION)
        5 -> arrayListOf(EditionType.FIRST_EDITION, EditionType.UNLIMITED)
        6 -> arrayListOf(EditionType.LIMITED_EDITION, EditionType.UNLIMITED)
        7 -> arrayListOf(EditionType.FIRST_EDITION, EditionType.LIMITED_EDITION, EditionType.UNLIMITED)
        else -> arrayListOf()
    }
}

fun getPokemonEditionKey(langKey: Int, pokemon: PokemonDomainModel) : Int {
    return when (langKey) {
        0 -> return pokemon.english
        1 -> return pokemon.japanese
        2 -> return pokemon.noLanguagenoEdition
        else -> 1
    }
}

fun getPokemonEditionList(rawKey: Int) : List<EditionType> {
    return when(rawKey) {
        1 ->  arrayListOf(EditionType.UNLIMITED)
        2 ->  arrayListOf(EditionType.FIRST_EDITION, EditionType.UNLIMITED)
        3 ->  arrayListOf(EditionType.FOURTH_PRINT_UK, EditionType.UNLIMITED)
        4 ->  arrayListOf(EditionType.REVERSE_HOLO_UNLIMITED, EditionType.UNLIMITED)
        5 ->  arrayListOf(EditionType.REVERSE_HOLO_UNLIMITED)
        6 ->  arrayListOf(EditionType.UNLIMITED, EditionType.HOLO_UNLIMITED)
        7 -> arrayListOf(EditionType.PROMO)
        8 -> arrayListOf(EditionType.JUMBO_PROMO)
        9 -> arrayListOf(EditionType.FIRST_EDITION)
        10 -> arrayListOf(EditionType.REVERSE_HOLO_FIRST_EDITION)
        11 -> arrayListOf(EditionType.FOURTH_PRINT_UK)
        12 -> arrayListOf(
            EditionType.REVERSE_HOLO_FIRST_EDITION,
            EditionType.REVERSE_HOLO_UNLIMITED,
            EditionType.UNLIMITED,
            EditionType.FIRST_EDITION
        )
        else -> arrayListOf()
    }
}

fun getMTGEditionList(): List<EditionType> =
    listOf<EditionType>(EditionType.FOIL, EditionType.NON_FOIL)

fun getSelectedMTGEdition(key: Int): EditionType {
    return when (key) {
        0 -> EditionType.FOIL
        1 -> EditionType.NON_FOIL
        else -> EditionType.FOIL
    }
}
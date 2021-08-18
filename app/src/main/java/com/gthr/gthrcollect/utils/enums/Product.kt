package com.gthr.gthrcollect.utils

enum class ProductCategory(val title: String) {
    TOYS("toys"),
    CARDS("cards"),
    SEALED("sealed")
}

enum class ProductType(val title: String) {
    MAGIC_THE_GATHERING("MTG"),
    YUGIOH("Yugioh"),
    POKEMON("Pokemon"),
    FUNKO("Funko"),
    SEALED_POKEMON("sealed_Pokemon"),
    SEALED_YUGIOH("sealed_Yugioh"),
    SEALED_MTG("sealed_magicthegathering")
}

val product = hashMapOf(
    ProductCategory.CARDS.title to setOf(
        ProductType.POKEMON,
        ProductType.YUGIOH,
        ProductType.MAGIC_THE_GATHERING
    ),
    ProductCategory.SEALED.title to setOf(
        ProductType.SEALED_MTG,
        ProductType.SEALED_POKEMON,
        ProductType.SEALED_YUGIOH
    ),
    ProductCategory.TOYS.title to setOf(ProductType.FUNKO)
)
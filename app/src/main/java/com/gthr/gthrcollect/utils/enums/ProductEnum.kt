package com.gthr.gthrcollect.utils.enums

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

val product: HashMap<ProductCategory, Set<ProductType>> = hashMapOf(
    ProductCategory.CARDS to setOf(
        ProductType.POKEMON,
        ProductType.YUGIOH,
        ProductType.MAGIC_THE_GATHERING
    ),
    ProductCategory.SEALED to setOf(
        ProductType.SEALED_MTG,
        ProductType.SEALED_POKEMON,
        ProductType.SEALED_YUGIOH
    ),
    ProductCategory.TOYS to setOf(ProductType.FUNKO)
)
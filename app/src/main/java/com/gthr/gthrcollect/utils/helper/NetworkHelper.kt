package com.gthr.gthrcollect.utils.helper

import com.gthr.gthrcollect.model.mapper.*
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.enums.ProductType

fun getFbRtModelNameFromProduct(productType: ProductType): String = when (productType) {
    ProductType.MAGIC_THE_GATHERING -> FirebaseRealtimeDatabase.MTG_MODEL
    ProductType.YUGIOH -> FirebaseRealtimeDatabase.YUGIOH_MODEL
    ProductType.POKEMON -> FirebaseRealtimeDatabase.POKEMON_MODEL
    ProductType.FUNKO -> FirebaseRealtimeDatabase.FUNKO_MODEL
    ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> FirebaseRealtimeDatabase.SEALED_MODEL
}

fun getModelTypeFromProduct(productType: ProductType): Class<out Any> = when (productType) {
    ProductType.MAGIC_THE_GATHERING -> MTGModel::class.java
    ProductType.YUGIOH -> YugiohModel::class.java
    ProductType.POKEMON -> PokemonModel::class.java
    ProductType.FUNKO -> FunkoModel::class.java
    ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> SealedModel::class.java
}

fun <T> getDomainModelFromNetworkModel(
    productType: ProductType,
    referenceKey: String,
    networkModel: Any
): T = when (productType) {
    ProductType.MAGIC_THE_GATHERING -> (networkModel as MTGModel).toMTGDomainModel(
        referenceKey ?: ""
    ) as T
    ProductType.YUGIOH -> (networkModel as YugiohModel).toYugiohDomainModel(referenceKey ?: "") as T
    ProductType.POKEMON -> (networkModel as PokemonModel).toPokemonDomainModel(
        referenceKey ?: ""
    ) as T
    ProductType.FUNKO -> (networkModel as FunkoModel).toFunkoDomainModel(referenceKey ?: "") as T
    ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG ->
        (networkModel as SealedModel).toSealedDomainModel(referenceKey ?: "") as T
}
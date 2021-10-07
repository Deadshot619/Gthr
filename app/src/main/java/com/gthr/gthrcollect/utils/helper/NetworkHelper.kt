package com.gthr.gthrcollect.utils.helper

import android.content.Context
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.model.mapper.*
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.model.network.firestore.UserInfoFirestoreModel
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.Firestore
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.getUserUID
import com.gthr.gthrcollect.utils.extensions.showToast
import kotlinx.coroutines.tasks.await

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

/**
 * Method to check if user is verified or not, Should be run on [Dispatchers.Main] thread.
 */
suspend fun Context.isUserVerified(
    runEverytime: () -> Unit,
    verified: () -> Unit,
    notVerified: () -> Unit
) {
    val userId = GthrCollect.prefs?.getUserUID()
    if (userId.isNullOrEmpty()) return
    val userModel = Firebase.firestore.collection(Firestore.COLLECTION_USER_INFO)
        .document(userId).get().await().toObject(UserInfoFirestoreModel::class.java)
    userModel?.let {
        when {
            it.isVerified -> verified()
            it.underReview -> this.showToast(this.getString(R.string.text_id_under_review))
            else -> notVerified()
        }
    }
    runEverytime()
}
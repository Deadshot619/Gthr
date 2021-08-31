package com.gthr.gthrcollect.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.mapper.*
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.enums.ProductType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class ProductDetailsRepository {

    private val mFirebaseRD = Firebase.database.reference

    fun <T> getProductDetails(id : String,type : ProductType) =  flow<State<T>> {
        emit(State.loading())
        var ref = mFirebaseRD
        val networkModelType = when(type){
            ProductType.MAGIC_THE_GATHERING -> {
                ref = mFirebaseRD.child(FirebaseRealtimeDatabase.MTG_MODEL)
                MTGModel::class.java
            }
            ProductType.YUGIOH -> {
                ref = mFirebaseRD.child(FirebaseRealtimeDatabase.YUGIOH_MODEL)
                YugiohModel::class.java
            }
            ProductType.POKEMON -> {
                ref = mFirebaseRD.child(FirebaseRealtimeDatabase.POKEMON_MODEL)
                PokemonModel::class.java
            }
            ProductType.FUNKO -> {
                ref = mFirebaseRD.child(FirebaseRealtimeDatabase.FUNKO_MODEL)
                FunkoModel::class.java
            }
            ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> {
                ref = mFirebaseRD.child(FirebaseRealtimeDatabase.SEALED_MODEL)
                SealedModel::class.java
            }
        }
        val await = ref.child(id).get().await()
        val productDetailsNetworkModel = await.getValue(networkModelType)
        val productDetailsDomainModel = when(type){
            ProductType.MAGIC_THE_GATHERING -> (productDetailsNetworkModel as MTGModel).toMTGDomainModel(await.key?:"")
            ProductType.YUGIOH -> (productDetailsNetworkModel as YugiohModel).toYugiohDomainModel(await.key?:"")
            ProductType.POKEMON -> (productDetailsNetworkModel as PokemonModel).toPokemonDomainModel(await.key?:"")
            ProductType.FUNKO -> (productDetailsNetworkModel as FunkoModel).toFunkoDomainModel(await.key?:"")
            ProductType.SEALED_POKEMON,ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> (productDetailsNetworkModel as SealedModel).toSealedDomainModel(await.key?:"")
        }
        emit(State.Success(productDetailsDomainModel as T))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}
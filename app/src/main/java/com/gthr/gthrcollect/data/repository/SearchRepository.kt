package com.gthr.gthrcollect.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.data.remote.fetchData
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.model.mapper.*
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.utils.constants.CloudFunctions
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.getProductType
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.HashMap

class SearchRepository {

    private val mFirebaseRD = Firebase.database.reference

    private lateinit var functions: FirebaseFunctions

    fun fetchProducts(searchKey: String?=null, productCategory:String?=null, productType :String?=null, limit:Int?=null) = flow<State<List<ProductDisplayModel>>> {
        // Emit loading state
        emit(State.loading())

        val data = hashMapOf(
            CloudFunctions.SWEEP_TAKES to searchKey,
            CloudFunctions.PRODUCT_CATEGORY to productCategory,
            CloudFunctions.PRODUCT_TYPE to productType,
            CloudFunctions.LIMIT to limit.toString()
        )

        val productData =
            fetchData<List<HashMap<String,String>>>(CloudFunctions.SEARCH_PRODUCT,data).await()
        GthrLogger.d("productData", "${productData}")

        val productList = mutableListOf<ProductDisplayModel>()

        productData.forEachIndexed {index, it ->

            val objectID =productData.get(index)[FirebaseRealtimeDatabase.OBJECT_ID]
            val productType =productData.get(index)[FirebaseRealtimeDatabase.PRODUCT_TYPE]

            when ( getProductType(productType!!)) {

                ProductType.MAGIC_THE_GATHERING ->{
                    val data=   getProductDetailsByObjectId2<MTGDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay= ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.YUGIOH ->{
                    val data=   getProductDetailsByObjectId2<YugiohDomainModel>(
                        objectID!!,
                        getProductType (productType)!!)
                    data?.let {
                        val prodDisplay= ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }

                ProductType.POKEMON ->{
                    val data=   getProductDetailsByObjectId2<PokemonDomainModel>(
                        objectID!!,
                        getProductType(productType)!!)
                    data?.let {
                        val prodDisplay= ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }

                ProductType.FUNKO -> {
                    val data=   getProductDetailsByObjectId2<FunkoDomainModel>(
                        objectID!!,
                        getProductType(productType)!!)
                    data?.let {
                        val prodDisplay= ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }

                ProductType.SEALED_POKEMON,
                ProductType.SEALED_YUGIOH,
                ProductType.SEALED_MTG -> {

                    val data=   getProductDetailsByObjectId2<SealedDomainModel>(
                        objectID!!,
                        getProductType(productType)!!)
                    data?.let {
                        val prodDisplay= ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }

            }
        }

        emit(State.success(productList))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


  suspend  fun <T> getProductDetailsByObjectId2(id: String, type: ProductType) : T? {

        var ref = mFirebaseRD
        val networkModelType = when (type) {
            ProductType.MAGIC_THE_GATHERING -> {
                ref = ref.child(FirebaseRealtimeDatabase.MTG_MODEL)
                MTGModel::class.java
            }
            ProductType.YUGIOH -> {
                ref = ref.child(FirebaseRealtimeDatabase.YUGIOH_MODEL)
                YugiohModel::class.java
            }
            ProductType.POKEMON -> {
                ref = ref.child(FirebaseRealtimeDatabase.POKEMON_MODEL)
                PokemonModel::class.java
            }
            ProductType.FUNKO -> {
                ref = ref.child(FirebaseRealtimeDatabase.FUNKO_MODEL)
                FunkoModel::class.java
            }
            ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> {
                ref = ref.child(FirebaseRealtimeDatabase.SEALED_MODEL)
                SealedModel::class.java
            }
        }
        val await = ref.orderByChild(FirebaseRealtimeDatabase.OBJECT_ID).equalTo(id).get().await()

        if (await.childrenCount == 1L) {
            val productDetailsNetworkModel =
                await.children.iterator().next().getValue(networkModelType)
            val productDetailsDomainModel = when (type) {
                ProductType.MAGIC_THE_GATHERING -> (productDetailsNetworkModel as MTGModel).toMTGDomainModel(await.key ?: "")

                ProductType.YUGIOH -> (productDetailsNetworkModel as YugiohModel).toYugiohDomainModel(await.key ?: "")

                ProductType.POKEMON -> (productDetailsNetworkModel as PokemonModel).toPokemonDomainModel(await.key ?: "")

                ProductType.FUNKO -> (productDetailsNetworkModel as FunkoModel).toFunkoDomainModel(await.key ?: "")

                ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> (productDetailsNetworkModel as SealedModel).toSealedDomainModel(await.key ?: "")
            }
            return  productDetailsDomainModel as T
        }
      return null
    }
}
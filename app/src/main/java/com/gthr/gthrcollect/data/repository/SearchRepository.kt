package com.gthr.gthrcollect.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonElement
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.data.remote.fetchData
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.model.mapper.*
import com.gthr.gthrcollect.model.network.cloudfunction.ForSaleItemModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.utils.constants.CloudFunctions
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.fromJsonElement
import com.gthr.gthrcollect.utils.extensions.gson
import com.gthr.gthrcollect.utils.extensions.toJsonElement
import com.gthr.gthrcollect.utils.getProductType
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class SearchRepository {

    private val mFirebaseRD = Firebase.database.reference

    fun fetchProducts(
        searchTerm: String? = null,
        productCategory: String? = null,
        productType: String? = null,
        limit: Int? = null,
        page: Int? = null,
        isAscending:Int?=null,
        sortBy: String? = null
    ) = flow<State<List<ProductDisplayModel>>> {
        // Emit loading state
        emit(State.loading())

        val data = hashMapOf(
            CloudFunctions.SEARCK_KEY to (searchTerm ?: ""),
            CloudFunctions.PRODUCT_CATEGORY to (productCategory ?: ""),
            CloudFunctions.PRODUCT_TYPE to (productType ?: ""),
            CloudFunctions.LIMIT to (limit ?: 60),
            CloudFunctions.PAGE to (page ?: 0),
            CloudFunctions.IS_ASCENDING to (isAscending ?: null),
            CloudFunctions.SORT_BY to (sortBy ?: FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
        )

        GthrLogger.d("ProductsMayank", data.toString())

        val productData =
            fetchData<List<HashMap<String, String>>>(CloudFunctions.SEARCH_PRODUCT, data).await()
        val productList = mutableListOf<ProductDisplayModel>()

        productData.forEachIndexed { index, it ->
            val objectID = productData[index][FirebaseRealtimeDatabase.OBJECT_ID]
            val productType = productData[index][FirebaseRealtimeDatabase.PRODUCT_TYPE]

            when (getProductType(productType!!)) {
                ProductType.MAGIC_THE_GATHERING -> {
                    val data = getProductDetailsByObjectId2<MTGDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay = ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.YUGIOH -> {
                    val data = getProductDetailsByObjectId2<YugiohDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay = ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.POKEMON -> {
                    val data = getProductDetailsByObjectId2<PokemonDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay = ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.FUNKO -> {
                    val data = getProductDetailsByObjectId2<FunkoDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay = ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> {
                    val data = getProductDetailsByObjectId2<SealedDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay = ProductDisplayModel(data)
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

    suspend fun <T> getProductDetailsByObjectId2(id: String, type: ProductType): T? {
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
            val snapShot = await.children.iterator().next()
            val productDetailsNetworkModel = snapShot.getValue(networkModelType)
            val productDetailsDomainModel = when (type) {
                ProductType.MAGIC_THE_GATHERING -> (productDetailsNetworkModel as MTGModel).toMTGDomainModel(
                    snapShot.key ?: ""
                )
                ProductType.YUGIOH -> (productDetailsNetworkModel as YugiohModel).toYugiohDomainModel(
                    snapShot.key ?: ""
                )
                ProductType.POKEMON -> (productDetailsNetworkModel as PokemonModel).toPokemonDomainModel(
                    snapShot.key ?: ""
                )
                ProductType.FUNKO -> (productDetailsNetworkModel as FunkoModel).toFunkoDomainModel(
                    snapShot.key ?: ""
                )
                ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> (productDetailsNetworkModel as SealedModel).toSealedDomainModel(
                    snapShot.key ?: ""
                )
            }
            return productDetailsDomainModel as T
        }
        return null
    }

    fun fetchCollection(searchTerm: String? = null, limit: Int? = null, page: Int? = null) =
        flow<State<List<SearchCollection>>> {
            // Emit loading state
            emit(State.loading())

            val data = hashMapOf(
                CloudFunctions.USERID to (GthrCollect.prefs?.collectionInfoModel?.userRefKey ?: ""),
                CloudFunctions.SEARCK_KEY to (searchTerm ?: ""),
                CloudFunctions.LIMIT to (limit ?: 60),
                CloudFunctions.PAGE to (page ?: 0)
            )

            GthrLogger.d("mayank", data.toString())

            val collectionData =
                fetchData<List<HashMap<String, *>>>(CloudFunctions.SEARCH_COLLECTION, data).await()
            val searchCollectionList = mutableListOf<SearchCollection>()

            collectionData.forEachIndexed { index, it ->
                try {
                    val profileImage: String? =
                        (collectionData[index][FirebaseRealtimeDatabase.PROFILE_URL_KEY]
                            ?: "") as String
                    val userName: String? =
                        (collectionData[index][FirebaseRealtimeDatabase.DISPLAY_NAME]
                            ?: "") as String
                    val objectId: String? =
                        (collectionData[index][FirebaseRealtimeDatabase.OBJECT_ID] ?: "") as String

                    var frontImage: String? = null
                    collectionData[index][FirebaseRealtimeDatabase.COLLECTION_LIST]?.let label@{
                        val collectionItemList = it as HashMap<String, HashMap<String, String>>
                        frontImage = collectionItemList.entries.iterator()
                            .next().value[FirebaseRealtimeDatabase.FRONT_IMAGE_URL] ?: ""
                    }

                    val data = SearchCollection(objectId, profileImage, userName, frontImage)

                    searchCollectionList.add(data)
                } catch (ex: Exception) {
                    print(ex.message)
                }
            }

            emit(State.success(searchCollectionList))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.d("collectionData", "${it.message}}")
        }.flowOn(Dispatchers.IO)

    fun fetchSearchAsk(
        searchTerm: String? = null,
        limit: Int? = null,
        page: Int? = null,
        sortBy: String? = null,
        isAscending: Int? = null,
        productCategory: String? = null,
        productType: String? = null,
        objectId: String? = null
    ) = flow<State<List<ProductDisplayModel>>> {
        // Emit loading state
        emit(State.loading())

        val data = hashMapOf(
            CloudFunctions.LIMIT to (limit ?: 60),
            CloudFunctions.SEARCK_KEY to (searchTerm ?: ""),
            CloudFunctions.PAGE to (page ?: 0),
            CloudFunctions.IS_ASCENDING to (isAscending ?: 1),
            CloudFunctions.SORT_BY to (sortBy),
            CloudFunctions.PRODUCT_CATEGORY to (productCategory ?: ""),
            CloudFunctions.PRODUCT_TYPE to (productType ?: ""),
            CloudFunctions.OBJECT_ID to (objectId ?: "")
        )

        GthrLogger.d("searchAskQuery", data.toString())
        val askData =
            fetchData<List<HashMap<String, *>>>(CloudFunctions.SEARCH_ASK, data).await()
        val searchCollectionList = mutableListOf<ProductDisplayModel>()

        GthrLogger.d("searchAskData", "${askData.toString()}}")

        askData.forEachIndexed { index, it ->
            try {
                //  val gson = Gson()
                val jsonElement: JsonElement = gson.toJsonElement(it)
                val saleItem: ForSaleItemModel? = gson.fromJsonElement(jsonElement)

                saleItem?.let {
                    searchCollectionList.add(ProductDisplayModel(it.toDomainModel()))
                }
            } catch (ex: Exception) {
                GthrLogger.e("searchAsk", ex.message.toString())
            }
        }

        emit(State.success(searchCollectionList))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
        GthrLogger.e("searchAsk", "${it.message}}")
    }.flowOn(Dispatchers.IO)

}
package com.gthr.gthrcollect.data.repository

import com.algolia.search.client.ClientSearch
import com.algolia.search.model.*
import com.algolia.search.model.indexing.Partial
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.model.domain.RecentSaleDomainModel
import com.gthr.gthrcollect.model.mapper.*
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.utils.constants.AlgoliaConstants
import com.gthr.gthrcollect.utils.constants.CalendarConstants.RECENT_SALE_DATE_DISPLAY_FORMAT
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.getUserCollectionId
import com.gthr.gthrcollect.utils.helper.getEmptyRecentSaleDomainModel
import com.gthr.gthrcollect.utils.helper.getEmptyRecentSaleDomainModelList
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat

class ProductDetailsRepository {

    private val mFirebaseRD = Firebase.database.reference
    private val mStorageRef = Firebase.storage.reference

    fun <T> getProductDetails(id: String, type: ProductType) = flow<State<T>> {
        emit(State.loading())
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
        val await = ref.child(id).get().await()
        val productDetailsNetworkModel = await.getValue(networkModelType)
        val productDetailsDomainModel = when (type) {
            ProductType.MAGIC_THE_GATHERING -> (productDetailsNetworkModel as MTGModel).toMTGDomainModel(
                await.key ?: ""
            )
            ProductType.YUGIOH -> (productDetailsNetworkModel as YugiohModel).toYugiohDomainModel(
                await.key ?: ""
            )
            ProductType.POKEMON -> (productDetailsNetworkModel as PokemonModel).toPokemonDomainModel(
                await.key ?: ""
            )
            ProductType.FUNKO -> (productDetailsNetworkModel as FunkoModel).toFunkoDomainModel(
                await.key ?: ""
            )
            ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> (productDetailsNetworkModel as SealedModel).toSealedDomainModel(
                await.key ?: ""
            )
        }
        emit(State.Success(productDetailsDomainModel as T))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    suspend fun <T> getProductDetailsByObjectId(objectId: String, type: ProductType) = flow<State<T>> {
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
        val await = ref.orderByChild(FirebaseRealtimeDatabase.OBJECT_ID).equalTo(objectId).get().await()

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
            emit(State.Success(productDetailsDomainModel as T))
        }
        else{
            emit(State.failed("No data found"))
        }
    }



    fun getRecentSellList(objectId : String) = flow<State<List<RecentSaleDomainModel>>>{
        emit(State.loading())
        val data: DataSnapshot = mFirebaseRD.child(FirebaseRealtimeDatabase.SALE_HISTORY_MODEL).orderByChild(FirebaseRealtimeDatabase.OBJECT_ID).equalTo(objectId).get().await()
        GthrLogger.i("dschsdsjds", "getRecentSellList: "+data)
        GthrLogger.i("dschsdsjds", "getRecentSellList: "+data.childrenCount)
        if(data.childrenCount>0){
            val list = mutableListOf<RecentSaleDomainModel>()
            data.children.forEach {
                list.add(it.getValue(RecentSaleModel::class.java)?.toDomainModel(it.key!!)!!)
            }

            GthrLogger.i("dschsdsjds", "list: "+list)
            val sortedList = list.sortedByDescending{
                val input = SimpleDateFormat(RECENT_SALE_DATE_DISPLAY_FORMAT)
                val date = input.parse(it.date)
                return@sortedByDescending date.time
            }
            GthrLogger.i("dschsdsjds", "sortedList: "+sortedList)
            val finalList = mutableListOf<RecentSaleDomainModel>()
            finalList.add(getEmptyRecentSaleDomainModel())
            finalList.addAll(sortedList)
            GthrLogger.i("dschsdsjds", "finalList : ${finalList}")
            emit(State.success(data = finalList))
        }
        else{
            GthrLogger.i("dschsdsjds", "Empty")
            emit(State.success(data = getEmptyRecentSaleDomainModelList()))
        }


    }.catch {
        // If exception is thrown, emit failed state along with message.
        GthrLogger.i("dschsdsjds", "error: "+it.message.toString())
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getRelatedProductList(value : String, type: ProductType) = flow<State<List<ProductDisplayModel>>>{
        emit(State.loading())
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

        val query = when(type){
            ProductType.MAGIC_THE_GATHERING -> ref.orderByChild(FirebaseRealtimeDatabase.SET_NAME)
            ProductType.YUGIOH -> ref.orderByChild(FirebaseRealtimeDatabase.SET)
            ProductType.POKEMON -> ref.orderByChild(FirebaseRealtimeDatabase.SET)
            ProductType.FUNKO -> ref.orderByChild(FirebaseRealtimeDatabase.LICENSE)
            ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> ref.orderByChild(FirebaseRealtimeDatabase.SET)
        }
        val await = query.equalTo(value).limitToFirst(10).get().await()

        if(await.childrenCount>0){
            val list = mutableListOf<ProductDisplayModel>()
            await.children.forEach {
                val productDetailsNetworkModel = it.getValue(networkModelType)
                val productDisplayModel = when (type) {
                    ProductType.MAGIC_THE_GATHERING -> ProductDisplayModel((productDetailsNetworkModel as MTGModel).toMTGDomainModel(it.key ?: ""))
                    ProductType.YUGIOH -> ProductDisplayModel((productDetailsNetworkModel as YugiohModel).toYugiohDomainModel(it.key ?: ""))
                    ProductType.POKEMON -> ProductDisplayModel((productDetailsNetworkModel as PokemonModel).toPokemonDomainModel(it.key ?: ""))
                    ProductType.FUNKO -> ProductDisplayModel((productDetailsNetworkModel as FunkoModel).toFunkoDomainModel(it.key ?: ""))
                    ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> ProductDisplayModel((productDetailsNetworkModel as SealedModel).toSealedDomainModel(it.key ?: ""))
                }
                list.add(productDisplayModel)
            }
            emit(State.success(data = list))
        } else {
            emit(State.success(data = listOf()))
        }
    }


    fun checkIfProductFavorite(productType: ProductType, objectId: String) = flow<State<Boolean>> {
        emit(State.loading())

        val favoriteListLink = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
            .child(GthrCollect.prefs?.getUserCollectionId()!!)
            .child(FirebaseRealtimeDatabase.FAVORITE_PRODUCT_LIST)

        //Get Fav list
        val favoriteList: MutableList<String> =
            favoriteListLink.get().await()
                .getValue(object : GenericTypeIndicator<MutableList<String>>() {})
                ?: mutableListOf()

        if (favoriteList.contains(objectId))
            emit(State.success(true))
        else
            emit(State.success(false))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun addProductToFavorites(productType: ProductType, objectId: String) = flow<State<Boolean>> {
        emit(State.loading())

        val favoriteListLink = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
            .child(GthrCollect.prefs?.getUserCollectionId()!!)
            .child(FirebaseRealtimeDatabase.FAVORITE_PRODUCT_LIST)

        //Get Fav list
        val favoriteList: MutableList<String> =
            favoriteListLink.get().await()
                .getValue(object : GenericTypeIndicator<MutableList<String>>() {})
                ?: mutableListOf()

        if (favoriteList.contains(objectId)) {
            emit(State.Failed("Product already favorited!"))
            return@flow
        }
        //Add objectId to list
        favoriteList.add(objectId)

        //Set List again
        favoriteListLink.setValue(favoriteList).await()

        val productTypeKey = when (productType) {
            ProductType.MAGIC_THE_GATHERING -> FirebaseRealtimeDatabase.MTG_MODEL
            ProductType.YUGIOH -> FirebaseRealtimeDatabase.YUGIOH_MODEL
            ProductType.POKEMON -> FirebaseRealtimeDatabase.POKEMON_MODEL
            ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG ->
                FirebaseRealtimeDatabase.SEALED_MODEL
            ProductType.FUNKO -> FirebaseRealtimeDatabase.FUNKO_MODEL
        }

//        val favTotalLink =

        var productModelKey = ""
        mFirebaseRD.child(productTypeKey).orderByChild(FirebaseRealtimeDatabase.OBJECT_ID)
            .equalTo(objectId).limitToFirst(1).get().await().children.forEach {
            productModelKey = it.key!!
        }

        GthrLogger.i("keyyyy", productTypeKey.toString())
        val link = mFirebaseRD.child(productTypeKey).child(productModelKey)
            .child(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
        var value: Int = link.get().await().getValue(Int::class.java) ?: 0
        value += 1
   //     updateFavoritesAlgolia(objectId, value)
        link.setValue(value).await()

        emit(State.success(true))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun removeProductFavorite(productType: ProductType, objectId: String) = flow<State<Boolean>> {
        emit(State.loading())

        val favoriteListLink = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
            .child(GthrCollect.prefs?.getUserCollectionId()!!)
            .child(FirebaseRealtimeDatabase.FAVORITE_PRODUCT_LIST)

        //Get Fav list
        val favoriteList: MutableList<String> =
            favoriteListLink.get().await()
                .getValue(object : GenericTypeIndicator<MutableList<String>>() {})
                ?: mutableListOf()

        if (!favoriteList.contains(objectId)) {
            emit(State.Failed("Product not favorited!"))
            return@flow
        }
        //Remove objectId from list
        favoriteList.remove(objectId)

        //Set List again
        favoriteListLink.setValue(favoriteList).await()

        val productTypeKey = when (productType) {
            ProductType.MAGIC_THE_GATHERING -> FirebaseRealtimeDatabase.MTG_MODEL
            ProductType.YUGIOH -> FirebaseRealtimeDatabase.YUGIOH_MODEL
            ProductType.POKEMON -> FirebaseRealtimeDatabase.POKEMON_MODEL
            ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG ->
                FirebaseRealtimeDatabase.SEALED_MODEL
            ProductType.FUNKO -> FirebaseRealtimeDatabase.FUNKO_MODEL
        }

        var productModelKey = ""
        mFirebaseRD.child(productTypeKey).orderByChild(FirebaseRealtimeDatabase.OBJECT_ID)
            .equalTo(objectId).limitToFirst(1).get().await().children.forEach {
            productModelKey = it.key!!
        }

        GthrLogger.i("keyyyy", productTypeKey.toString())
        val link = mFirebaseRD.child(productTypeKey).child(productModelKey)
            .child(FirebaseRealtimeDatabase.NUMBER_OF_FAVORITES)
        var value: Int = link.get().await().getValue(Int::class.java) ?: 0
        if (value > 0)
            value -= 1
  //      updateFavoritesAlgolia(objectId, value)
        link.setValue(value).await()

        emit(State.success(true))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun updateFavoritesAlgolia(objectID: String, noOfFavs: Int) {
        val scope = CoroutineScope(Dispatchers.IO).async {
            val client = ClientSearch(
                applicationID = ApplicationID(AlgoliaConstants.APP_ID),
                apiKey = APIKey(AlgoliaConstants.APIKey)
            )
            val indexName = IndexName(AlgoliaConstants.ITEM_NAME)
            val index = client.initIndex(indexName)

            val ob = listOf(
                ObjectID(objectID) to Partial.Update(
                    Attribute(AlgoliaConstants.NUMBER_OF_FAVORITES),
                    noOfFavs
                ),
            )
            index.partialUpdateObjects(ob, true)
        }
        scope.await()
    }
}
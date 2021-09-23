package com.gthr.gthrcollect.data.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.data.remote.fetchData
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.model.mapper.*
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.model.mapper.*
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.utils.constants.CloudFunctions
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.FirebaseStorage
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.getUserCollectionId
import com.gthr.gthrcollect.utils.extensions.updateCollectionInfoModelData
import com.gthr.gthrcollect.utils.getProductType
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import com.gthr.gthrcollect.utils.getProductCategory
import com.gthr.gthrcollect.utils.getProductType
import com.gthr.gthrcollect.utils.getProductTypeFromObjectId

class ProfileRepository {
    private val mFirestore = Firebase.firestore
    private val mFirebaseRD = Firebase.database.reference
    private val mStorageRef = Firebase.storage.reference

    fun fetchUserProfileData(collectionId: String) =
        flow<State<CollectionInfoDomainModel>> {
            emit(State.loading())
            val collectionInfo = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(collectionId).get().await().getValue(CollectionInfoModel::class.java)

            //If the user is Logged in user, then save the data
            if (collectionId == GthrCollect.prefs?.getUserCollectionId())
                collectionInfo?.let {
                    GthrCollect.prefs?.updateCollectionInfoModelData(it.toCollectionInfoDomainModel())
                }

            emit(State.Success(collectionInfo!!.toCollectionInfoDomainModel()))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun insertCollectionInfoInRD(collectionInfoModel: CollectionInfoModel) =
        flow<State<String>> {
            emit(State.loading())

            val data = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(GthrCollect.prefs?.userInfoModel?.collectionId.toString())
                .child(FirebaseRealtimeDatabase.ABOUT)
            data.setValue(collectionInfoModel.about).await()

            emit(State.success(data.key.toString()))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.d("Faileeed", it.message.toString())
        }.flowOn(Dispatchers.IO)

    fun uploadProfilePic(uri: Uri) = flow<State<Boolean>> {
        emit(State.loading())

        // upload image to Fb storage
        val ref = mStorageRef.child(FirebaseStorage.PROFILE_IMAGE)
            .child(GthrCollect.prefs?.userInfoModel?.collectionId.toString())
        ref.putFile(uri).await()

        // converting storage path to image download URL and save to Rd collection model
        val imageUrl = ref.downloadUrl.await()
        mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
            .child(GthrCollect.prefs?.userInfoModel?.collectionId.toString())
            .child(FirebaseRealtimeDatabase.PROFILE_URL_KEY).setValue(imageUrl.toString()).await()

        emit(State.success(true))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun fetchMyFollowing(collectionId: String) =
        flow<State<List<CollectionInfoDomainModel>>> {
            emit(State.loading())

            val followingData = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(collectionId)
                .child(FirebaseRealtimeDatabase.FAVORITE_COLLECTION_LIST).get().await()

            val arrayList = mutableListOf<CollectionInfoDomainModel>()

            if (followingData.hasChildren()) {
                val followingList = followingData.value as List<String>

                //Retrieve Following Users data with respect to its collection id
                followingList.forEach { collectionId ->
                    val collectionInfo: CollectionInfoDomainModel? =
                        mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                            .child(collectionId).get().await()
                            .getValue(
                                CollectionInfoModel::class.java
                            )?.toCollectionInfoDomainModel(collectionId)

                    if (collectionInfo != null) {
                        arrayList.add(collectionInfo)
                    }
                }
            }

            emit(State.Success(arrayList))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.e("Followers", it.message.toString())

        }.flowOn(Dispatchers.IO)

    fun fetchMyFollowersList(collectionId: String) =
        flow<State<List<CollectionInfoDomainModel>>> {

            emit(State.loading())

            val data = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(collectionId)
                .child(FirebaseRealtimeDatabase.FOLLOWERS_LIST).get().await()

            val arrayList = mutableListOf<CollectionInfoDomainModel>()

            if (data.hasChildren()) {
                val followingList = data.value as List<String>

                followingList.forEach { collectionId ->

                    val collectionInfo: CollectionInfoDomainModel? =
                        mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                            .child(collectionId).get().await()
                            .getValue(
                                CollectionInfoModel::class.java
                            )?.toCollectionInfoDomainModel(collectionId)

                    GthrLogger.e("MyFollowers", collectionId.toString())

                    if (collectionInfo != null) {
                        arrayList.add(collectionInfo)
                    }
                }
            }
            emit(State.Success(arrayList))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            print(it.cause?.message)
            GthrLogger.e("MyFollowers", it.message.toString())

        }.flowOn(Dispatchers.IO)

    fun followToUser(collectionId: String) =
        flow<State<String>> {
            emit(State.loading())

            val myCollectionId = GthrCollect.prefs?.getUserCollectionId().toString()
            val otherUserFollowerLink =
                mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                    .child(collectionId)
                    .child(FirebaseRealtimeDatabase.FOLLOWERS_LIST)
            val ourUserFollowingLink =
                mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                    .child(myCollectionId)
                    .child(FirebaseRealtimeDatabase.FAVORITE_COLLECTION_LIST)

            // Retrieve follower list of other user
            val otherUserFollowerList = otherUserFollowerLink.get().await()
            //Create an empty follower list
            val fList = mutableListOf<String>()
            //Check if other user has a list, then add our collection Id to it
            if (otherUserFollowerList.hasChildren()) {
                fList.addAll(otherUserFollowerList.value as ArrayList<String>)
            }
            fList.add(myCollectionId)
            //Update other user's follower List
            otherUserFollowerLink.setValue(fList).await()

            // Create an empty following list
            val foList = mutableListOf<String>()
            //Retrieve following list of our user
            val ourFollowingList = ourUserFollowingLink.get().await()
            //Check if our user has a list, then add other user's collection Id to it
            if (ourFollowingList.hasChildren()) {
                foList.addAll(ourFollowingList.value as ArrayList<String>)
            }
            foList.add(collectionId)
            // updating our following List
            ourUserFollowingLink.setValue(foList).await()

            emit(State.success("Followed"))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.d("Faileeed", it.message.toString())
        }.flowOn(Dispatchers.IO)

    fun unFollowToUser(collectionId: String) =
        flow<State<String>> {
            emit(State.loading())

            // Adding another user to my  followersList
            val fList = mutableListOf<String>()
            val myCollectionId = GthrCollect.prefs?.getUserCollectionId().toString()

            // Retriving Follower's list
            val getList = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(collectionId)
                .child(FirebaseRealtimeDatabase.FOLLOWERS_LIST).get().await().value
            fList.addAll(getList as ArrayList<String>)
            fList.remove(myCollectionId)

            // updating the List
            mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(collectionId)
                .child(FirebaseRealtimeDatabase.FOLLOWERS_LIST).setValue(fList).await()

            // Adding Me to  another user's to favoriteCollectionList
            val foList = mutableListOf<String>()

            // Retriving Following list
            val favList = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(myCollectionId)
                .child(FirebaseRealtimeDatabase.FAVORITE_COLLECTION_LIST).get().await().value

            foList.addAll(favList as ArrayList<String>)
            foList.remove(collectionId)

            // updating the List
            mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(myCollectionId).child(FirebaseRealtimeDatabase.FAVORITE_COLLECTION_LIST)
                .setValue(foList).await()

            emit(State.success("Un-Followed"))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.d("Faileeed", it.message.toString())
        }.flowOn(Dispatchers.IO)

    fun fetchFavProductsList(collectionId: String) =
        flow<State<List<ProductDisplayModel>>> {
            GthrLogger.e("ProductList", "id: ${collectionId}")
            emit(State.loading())

            val data = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(collectionId)
                .child(FirebaseRealtimeDatabase.FAVORITE_PRODUCT_LIST).get().await()

            val productList = mutableListOf<ProductDisplayModel>()


            if (data.hasChildren()) {
                val ProductList = data.value as List<String>

                GthrLogger.e("ProductList", "ProductList: ${ProductList}")

                ProductList.forEach { objectID ->

                    val productType = getProductTypeFromObjectId(objectID)

                    GthrLogger.e(
                        "pType",
                        productType.toString() + "_" + productType.title + " " + collectionId
                    )

                    when (productType) {
                        ProductType.MAGIC_THE_GATHERING -> {
                            val data = getProductDetailsByObjectId2<MTGDomainModel>(
                                objectID, productType
                            )
                            data?.let {
                                val prodDisplay = ProductDisplayModel(data)
                                productList.add(prodDisplay)
                            }
                        }
                        ProductType.YUGIOH -> {
                            val data = getProductDetailsByObjectId2<YugiohDomainModel>(
                                objectID!!,
                                productType
                            )
                            data?.let {
                                val prodDisplay = ProductDisplayModel(data)
                                productList.add(prodDisplay)
                            }
                        }
                        ProductType.POKEMON -> {
                            val data = getProductDetailsByObjectId2<PokemonDomainModel>(
                                objectID!!,
                                productType
                            )
                            data?.let {
                                val prodDisplay = ProductDisplayModel(data)
                                productList.add(prodDisplay)
                            }
                        }
                        ProductType.FUNKO -> {
                            val data = getProductDetailsByObjectId2<FunkoDomainModel>(
                                objectID!!,
                                productType
                            )
                            data?.let {
                                val prodDisplay = ProductDisplayModel(data)
                                productList.add(prodDisplay)
                            }
                        }
                        ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> {
                            val data = getProductDetailsByObjectId2<SealedDomainModel>(
                                objectID!!,
                                productType
                            )
                            data?.let {
                                val prodDisplay = ProductDisplayModel(data)
                                productList.add(prodDisplay)
                            }
                        }
                    }
                }
            }
            GthrLogger.e("productList", productList.toString())

            emit(State.Success(productList))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            print(it.cause?.message)
            GthrLogger.e("favproducts", it.message.toString())

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



    fun getCollectionProduct(map : Map<String, CollectionItemModel>) = flow<State<List<ProductDisplayModel>>> {
        val productList = mutableListOf<ProductDisplayModel>()
        map.keys.forEach {
            emit(State.loading())
            val data = map[it]
            val productType = data?.productType
            val objectID = data?.objectID
            val isForSale = data?.askRefKey!=null&&data?.askRefKey?.isNotEmpty()!!

            GthrLogger.i("jdshcjfds","isForSale "+(data?.askRefKey))
            GthrLogger.i("jdshcjfds","isForSale "+(data?.askRefKey!=null&&data?.askRefKey?.isNotEmpty()!!))

            when (getProductType(productType!!)) {
                ProductType.MAGIC_THE_GATHERING -> {
                    val data = getProductDetailsByObjectId<MTGDomainModel>(
                        objectID!!,
                        getProductType(productType,)!!
                    )
                    data?.let {
                        val prodDisplay = ProductDisplayModel(data,isForSale)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.YUGIOH -> {
                    val data = getProductDetailsByObjectId<YugiohDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay= ProductDisplayModel(data,isForSale)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.POKEMON ->{
                    val data = getProductDetailsByObjectId<PokemonDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay= ProductDisplayModel(data,isForSale)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.FUNKO -> {
                    val data = getProductDetailsByObjectId<FunkoDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay = ProductDisplayModel(data,isForSale)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> {
                    val data = getProductDetailsByObjectId<SealedDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay = ProductDisplayModel(data,isForSale)
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

    suspend fun <T> getProductDetailsByObjectId(id: String, type: ProductType): T? {
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

    fun fetchBidProducts(collectionId : String) = flow<State<List<ProductDisplayModel>>> {
        // Emit loading state
        emit(State.loading())

        val data = hashMapOf(
            CloudFunctions.COLLECTION_ID to (collectionId ?: ""),
        )

        GthrLogger.d("mayank", data.toString())

        val productData = fetchData<List<HashMap<String, String>>>(CloudFunctions.SEARCH_BIDS, data).await()
        val productList = mutableListOf<ProductDisplayModel>()

        productData.forEachIndexed { index, it ->
            val objectID = productData[index][FirebaseRealtimeDatabase.ITEM_OBJECT_ID]
            val productType = productData[index][FirebaseRealtimeDatabase.PRODUCT_TYPE]

            when (getProductType(productType!!)) {
                ProductType.MAGIC_THE_GATHERING -> {
                    val data = getProductDetailsByObjectId<MTGDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay = ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.YUGIOH -> {
                    val data = getProductDetailsByObjectId<YugiohDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay= ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.POKEMON ->{
                    val data = getProductDetailsByObjectId<PokemonDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay= ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.FUNKO -> {
                    val data = getProductDetailsByObjectId<FunkoDomainModel>(
                        objectID!!,
                        getProductType(productType)!!
                    )
                    data?.let {
                        val prodDisplay = ProductDisplayModel(data)
                        productList.add(prodDisplay)
                    }
                }
                ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> {
                    val data = getProductDetailsByObjectId<SealedDomainModel>(
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

}
package com.gthr.gthrcollect.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.model.mapper.*
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.Firestore
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.helper.getDomainModelFromNetworkModel
import com.gthr.gthrcollect.utils.helper.getFbRtModelNameFromProduct
import com.gthr.gthrcollect.utils.helper.getModelTypeFromProduct
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class ReceiptRepository {

    private val mFirebaseRD = Firebase.database.reference
    private val mFirestore = Firebase.firestore

    fun fetchProductDetail(productType: ProductType, objectID: String) = flow<State<ProductDisplayModel?>> {
            // Emit loading state
            emit(State.loading())

            val fbRtModelName = getFbRtModelNameFromProduct(productType)
            val modelType = getModelTypeFromProduct(productType)
            val snapShot = mFirebaseRD.child(fbRtModelName).orderByChild(FirebaseRealtimeDatabase.OBJECT_ID).equalTo(objectID).get().await().children.first()
            val networkData = snapShot.getValue(modelType)

            try {
                val data = when (productType) {
                    ProductType.MAGIC_THE_GATHERING ->
                        ProductDisplayModel(
                            getDomainModelFromNetworkModel<MTGDomainModel>(
                                productType,
                                snapShot.key!!,
                                networkData!!
                            )
                        )
                    ProductType.YUGIOH ->
                        ProductDisplayModel(
                            getDomainModelFromNetworkModel<YugiohDomainModel>(
                                productType,
                                snapShot.key!!,
                                networkData!!
                            )
                        )
                    ProductType.POKEMON ->
                        ProductDisplayModel(
                            getDomainModelFromNetworkModel<PokemonDomainModel>(
                                productType,
                                snapShot.key!!,
                                networkData!!
                            )
                        )
                    ProductType.FUNKO ->
                        ProductDisplayModel(
                            getDomainModelFromNetworkModel<FunkoDomainModel>(
                                productType,
                                snapShot.key!!,
                                networkData!!
                            )
                        )
                    ProductType.SEALED_POKEMON,
                    ProductType.SEALED_YUGIOH,
                    ProductType.SEALED_MTG ->
                        ProductDisplayModel(
                            getDomainModelFromNetworkModel<SealedDomainModel>(
                                productType,
                                snapShot.key!!,
                                networkData!!
                            )
                        )
                }
                emit(State.success(data))
            } catch (e: Exception) {
                emit(State.success(null))
            }
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    fun fetchAllReceipts(userId : String) = flow<State<List<ReceiptDisplayModel>>> {
        emit(State.loading())
        val allReceiptList = mutableListOf<ReceiptDisplayModel>()
        allReceiptList.addAll(getReceiptsItems(userId, true))
        allReceiptList.addAll(getReceiptsItems(userId, false))
        emit(State.success(allReceiptList))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    fun fetchSaleReceipts(userId : String) = flow<State<List<ReceiptDisplayModel>>> {
        emit(State.loading())
        val allReceiptList = mutableListOf<ReceiptDisplayModel>()
        allReceiptList.addAll(getReceiptsItems(userId, true))
        emit(State.success(allReceiptList))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun fetchBuyReceipts(userId : String) = flow<State<List<ReceiptDisplayModel>>> {
        emit(State.loading())
        val allReceiptList = mutableListOf<ReceiptDisplayModel>()
        allReceiptList.addAll(getReceiptsItems(userId, false))
        emit(State.success(allReceiptList))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun getReceiptsItems(userId : String, isSell : Boolean) : List<ReceiptDisplayModel>{
        val orderBy = if(isSell) FirebaseRealtimeDatabase.SELLER_UID else FirebaseRealtimeDatabase.BUYER_UID
        val snapShot = mFirebaseRD.child(FirebaseRealtimeDatabase.RECIEPT_MODEL).orderByChild(orderBy).equalTo(userId).get().await()
        GthrLogger.i("jdbcjsd","snapShot $snapShot")
        val list = mutableListOf<ReceiptDisplayModel>()

        if(snapShot.hasChildren()){
            snapShot.children.forEach {
                val receipt = it.getValue(ReceiptModel::class.java)
                val receiptDomainModel = receipt?.toReceiptDomainModel(it.key!!)

                if(receipt?.saleID != null){
                    val saleHistoryDomainModel = getSaleHistory(receipt?.saleID!!)

                    val objectID = receiptDomainModel?.objectID
                    val productType = receiptDomainModel?.productType

                    if(objectID!=null&&productType!=null){
                        val productDisplayModel = when (productType) {
                            ProductType.MAGIC_THE_GATHERING -> {
                                val data = getProductDetailsByObjectId<MTGDomainModel>(objectID!!, productType)
                                ProductDisplayModel(data!!)
                            }
                            ProductType.YUGIOH -> {
                                val data = getProductDetailsByObjectId<YugiohDomainModel>(objectID!!, productType)
                                ProductDisplayModel(data!!)
                            }
                            ProductType.POKEMON -> {
                                val data = getProductDetailsByObjectId<PokemonDomainModel>(objectID!!, productType)
                                ProductDisplayModel(data!!)
                            }
                            ProductType.FUNKO -> {
                                val data = getProductDetailsByObjectId<FunkoDomainModel>(objectID!!, productType)
                                ProductDisplayModel(data!!)
                            }
                            ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> {
                                val data = getProductDetailsByObjectId<SealedDomainModel>(objectID!!, productType)
                                ProductDisplayModel(data!!)
                            }
                            else -> null
                        }
                        GthrLogger.i("sdcsdnc","productDisplayModel ${productDisplayModel?.productType}")
                        GthrLogger.i("sdcsdnc","productDisplayModel ${productDisplayModel?.refKey}")
                        GthrLogger.i("jdbcjsd","receiptDomainModel $receiptDomainModel")
                        GthrLogger.i("jdbcjsd","productDisplayModel $productDisplayModel")
                        GthrLogger.i("jdbcjsd","saleHistoryDomainModel $saleHistoryDomainModel")
                        if(receiptDomainModel!=null&&productDisplayModel!=null&&saleHistoryDomainModel!=null){
                            val receiptDisplayDomainModel = ReceiptDisplayModel(receiptDomainModel,productDisplayModel,saleHistoryDomainModel)
                            GthrLogger.i("jdbcjsd","receiptDisplayDomainModel $receiptDisplayDomainModel")
                            list.add(receiptDisplayDomainModel)
                        }

                    }
                }
            }
        }

        return list
    }

    private suspend fun getSaleHistory(saleRef : String): SaleHistoryDomainModel?{
        val await = mFirebaseRD.child(FirebaseRealtimeDatabase.SALE_HISTORY_MODEL).child(saleRef).get().await()
        return await.getValue(SaleHistoryModel::class.java)?.toSaleHistoryDomainModel()
    }

    private suspend fun <T> getProductDetailsByObjectId(id: String, type: ProductType): T? {
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

    fun getCollectionInfo(userID : String) =
        flow<State<CollectionInfoDomainModel>> {
            emit(State.loading())
            val data = mFirestore.collection(Firestore.COLLECTION_USER_INFO).document(userID).get().await()
            val collectionId = data.data?.get(Firestore.COLLECTION_ID).toString()
            val collectionInfo = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL).child(collectionId).get().await().getValue(CollectionInfoModel::class.java)
            emit(State.Success(collectionInfo?.toCollectionInfoDomainModel(collectionId)!!))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)



}
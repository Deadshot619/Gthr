package com.gthr.gthrcollect.data.repository

import android.net.Uri
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.*
import com.algolia.search.model.indexing.Partial
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import com.gthr.gthrcollect.data.remote.fetchData
import com.gthr.gthrcollect.data.remote.fetchDataWithoutParameter
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ReceiptDomainModel
import com.gthr.gthrcollect.model.domain.ShippingInfoDomainModel
import com.gthr.gthrcollect.model.mapper.*
import com.gthr.gthrcollect.model.network.cloudfunction.StripePaymentModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.utils.constants.AlgoliaConstants
import com.gthr.gthrcollect.utils.constants.CloudFunctions
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.FirebaseStorage
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.helper.getFbRtModelNameFromProduct
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.io.File


class AskFlowRepository {

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

    fun fetchTier(productType: ProductType, refKey: String) = flow<State<String?>> {
        emit(State.loading())

        val modelName = getFbRtModelNameFromProduct(productType)
        val tier =
            mFirebaseRD.child(modelName).child(refKey).child(FirebaseRealtimeDatabase.TIER).get()
                .await().getValue(Any::class.java)

        emit(State.Success(tier?.toString()))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getShippingTierInfo(tier: String) = flow<State<ShippingInfoDomainModel>> {
        emit(State.loading())

        val tierData =
            mFirebaseRD.child(FirebaseRealtimeDatabase.SHIPPING_TIER).child(tier).get().await()
        val data = (tierData.getValue(ShippingInfoModel::class.java)
            ?: ShippingInfoModel()).toDomainModel()

        emit(State.Success(data))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun uploadCollectionImage(url: String, imageName: String, imageSide: String, uid: String) =
        flow<State<String>> {
            emit(State.loading())
            val file = Uri.fromFile(File(url))
            val ref =
                mStorageRef.child(FirebaseStorage.COLLECTION_IMAGE).child(uid).child(imageSide)
                    .child("${imageName}.png")
            ref.putFile(file).await()
            val url = ref.downloadUrl.await()
            emit(State.success(url.toString()))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            GthrLogger.d("sdjns", "error   " + it.message.toString())
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun insertCollection(collectionInfoId: String, mCollectionItemModel: CollectionItemModel) =
        flow<State<String>> {
            emit(State.loading())
            val push = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(collectionInfoId).child(FirebaseRealtimeDatabase.COLLECTION_LIST).push()
            push.setValue(mCollectionItemModel).await()
            emit(State.success(push.key!!))
        }.catch {
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun updateCollection(
        collectionInfoId: String,
        collectionKey: String,
        frontImageUrl: String,
        backImageUrl: String,
        askId: String? = null
    ) = flow<State<Boolean>> {
        emit(State.loading())
        val map = hashMapOf<String, Any>(
            FirebaseRealtimeDatabase.FRONT_IMAGE_URL to frontImageUrl,
            FirebaseRealtimeDatabase.BACK_IMAGE_URL to backImageUrl,
            FirebaseRealtimeDatabase.ASK_REF_KEY to askId!!,
        )
        val collectionInfoLink = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
            .child(collectionInfoId)
        //Add AskRefKey to User's CollectionList
        collectionInfoLink.child(FirebaseRealtimeDatabase.COLLECTION_LIST).child(collectionKey)
            .updateChildren(map).await()
        //Add AskRefKey to User's SellList
        val sellList = collectionInfoLink.child(FirebaseRealtimeDatabase.SELL_LIST).get().await()
            .getValue(object : GenericTypeIndicator<List<String>>() {})
        val newSellList = mutableListOf<String>()
        if (sellList.isNullOrEmpty())
            newSellList.add(askId)
        else {
            newSellList.addAll(sellList)
            newSellList.add(askId)
        }
        collectionInfoLink.child(FirebaseRealtimeDatabase.SELL_LIST).setValue(newSellList).await()

        emit(State.success(true))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun insertAsk(askItemModel: AskItemModel) = flow<State<String>> {
        emit(State.loading())
        val push = mFirebaseRD.child(FirebaseRealtimeDatabase.ASK_ITEM_MODEL).push()
        GthrLogger.d("sdcbsjdb", "ASkItemId ${push.key}")
        push.setValue(askItemModel).await()
        emit(State.success(push.key!!))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun editAsk(productType: ProductType, objectID: String, askRefKey: String, price: Double) =
        flow<State<Boolean>> {
            emit(State.loading())
            val updateAskPrice =
                mFirebaseRD.child(FirebaseRealtimeDatabase.ASK_ITEM_MODEL).child(askRefKey)
                    .child(FirebaseRealtimeDatabase.ASK_PRICE).setValue(price).await()

            val productFbModel = getFbRtModelNameFromProduct(productType)
            val itemRefKey =
                mFirebaseRD.child(productFbModel).orderByChild(FirebaseRealtimeDatabase.OBJECT_ID)
                    .equalTo(objectID).get().await().children.first().key.toString()
            val productModelLink = mFirebaseRD.child(productFbModel).child(itemRefKey)

            val lowestAskCost =
                productModelLink.child(FirebaseRealtimeDatabase.LOWEST_ASK_COST).get()
                    .await()?.getValue(Double::class.java) ?: 0.0
            val lowestAskID = productModelLink.child(FirebaseRealtimeDatabase.LOWEST_ASK_ID).get()
                .await().getValue(String::class.java).toString()

            if (price < lowestAskCost && lowestAskID != askRefKey) {
                val map = mapOf(
                    FirebaseRealtimeDatabase.LOWEST_ASK_COST to price,
                    FirebaseRealtimeDatabase.LOWEST_ASK_ID to askRefKey
                )
                productModelLink.updateChildren(map).await()
            } else if (lowestAskID == askRefKey && price > lowestAskCost) {
                var tempLowestPrice = lowestAskCost
                var tempLowestAskId = lowestAskID

                //Find the lowestAskCost for same objectID & update its product model
                mFirebaseRD.child(FirebaseRealtimeDatabase.ASK_ITEM_MODEL)
                    .orderByChild(FirebaseRealtimeDatabase.OBJECT_ID)
                    .equalTo(objectID).get().await().children.forEach { snap ->
                        snap?.child(FirebaseRealtimeDatabase.ASK_PRICE)
                            ?.getValue(Double::class.java)?.let {
                                if (it < tempLowestPrice) {
                                    tempLowestPrice = it
                                    tempLowestAskId = snap.key.toString()
                                }
                            }
                    }

                val map = mapOf(
                    FirebaseRealtimeDatabase.LOWEST_ASK_COST to tempLowestPrice,
                    FirebaseRealtimeDatabase.LOWEST_ASK_ID to tempLowestAskId
                )
                productModelLink.updateChildren(map).await()
            }

            emit(State.success(true))
        }.catch {
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun editBid(productType: ProductType, objectID: String, bidRefKey: String, price: Double) =
        flow<State<Boolean>> {
            emit(State.loading())
            val updateBidPrice =
                mFirebaseRD.child(FirebaseRealtimeDatabase.BID_ITEM_MODEL).child(bidRefKey)
                    .child(FirebaseRealtimeDatabase.BID_PRICE).setValue(price).await()

            val productFbModel = getFbRtModelNameFromProduct(productType)
            val itemRefKey =
                mFirebaseRD.child(productFbModel).orderByChild(FirebaseRealtimeDatabase.OBJECT_ID)
                    .equalTo(objectID).get().await().children.first().key.toString()
            val productModelLink = mFirebaseRD.child(productFbModel).child(itemRefKey)

            val highestBidCost =
                productModelLink.child(FirebaseRealtimeDatabase.HIGHEST_BID_COST).get()
                    .await()?.getValue(Double::class.java) ?: 0.0
            val highestBidID = productModelLink.child(FirebaseRealtimeDatabase.HIGHEST_BID_ID).get()
                .await().getValue(String::class.java).toString()

            if (price > highestBidCost && highestBidID != bidRefKey) {
                val map = mapOf(
                    FirebaseRealtimeDatabase.HIGHEST_BID_COST to price,
                    FirebaseRealtimeDatabase.HIGHEST_BID_ID to bidRefKey
                )
                productModelLink.updateChildren(map).await()
            } else if (highestBidID == bidRefKey && price < highestBidCost) {
                var tempHighestPrice = highestBidCost
                var tempHighestBidId = highestBidID

                //Find the lowestAskCost for same objectID & update its product model
                mFirebaseRD.child(FirebaseRealtimeDatabase.BID_ITEM_MODEL)
                    .orderByChild(FirebaseRealtimeDatabase.OBJECT_ID)
                    .equalTo(objectID).get().await().children.forEach { snap ->
                        snap?.child(FirebaseRealtimeDatabase.BID_PRICE)
                            ?.getValue(Double::class.java)?.let {
                                if (it > tempHighestPrice) {
                                    tempHighestPrice = it
                                    tempHighestBidId = snap.key.toString()
                                }
                            }
                    }

                val map = mapOf(
                    FirebaseRealtimeDatabase.HIGHEST_BID_COST to tempHighestPrice,
                    FirebaseRealtimeDatabase.HIGHEST_BID_ID to tempHighestBidId
                )
                productModelLink.updateChildren(map).await()
            }

            emit(State.success(true))
        }.catch {
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    fun updateProductForAsk(
        lowestAskCost: Int,
        lowestAskID: String,
        productType: ProductType,
        refKey: String,
        objectID: String
    ) = flow<State<Boolean>> {
        emit(State.loading())
        updateToAlgoliaForAsk(objectID, lowestAskCost, lowestAskID)
        var ref = mFirebaseRD
        ref = when (productType) {
            ProductType.MAGIC_THE_GATHERING -> ref.child(FirebaseRealtimeDatabase.MTG_MODEL)
            ProductType.YUGIOH -> ref.child(FirebaseRealtimeDatabase.YUGIOH_MODEL)
            ProductType.POKEMON -> ref.child(FirebaseRealtimeDatabase.POKEMON_MODEL)
            ProductType.FUNKO -> ref.child(FirebaseRealtimeDatabase.FUNKO_MODEL)
            ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> ref.child(
                FirebaseRealtimeDatabase.SEALED_MODEL
            )
        }
        val map = HashMap<String, Any>()
        map[FirebaseRealtimeDatabase.LOWEST_ASK_COST] = lowestAskCost
        map[FirebaseRealtimeDatabase.LOWEST_ASK_ID] = lowestAskID
        ref.child(refKey).updateChildren(map).await()
        emit(State.success(true))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun updateToAlgoliaForAsk(
        objectID: String,
        lowestAskCost: Int,
        lowestAskID: String
    ) {
        val scope = CoroutineScope(Dispatchers.IO).async {
            val client = ClientSearch(
                applicationID = ApplicationID(AlgoliaConstants.APP_ID),
                apiKey = APIKey(AlgoliaConstants.APIKey)
            )
            val indexName = IndexName(AlgoliaConstants.ITEM_NAME)
            val index = client.initIndex(indexName)

            val ob = listOf(
                ObjectID(objectID) to Partial.Update(
                    Attribute(AlgoliaConstants.LOWEST_ASK_ID),
                    lowestAskID
                ),
                ObjectID(objectID) to Partial.Update(
                    Attribute(AlgoliaConstants.LOWEST_ASK_COST),
                    lowestAskCost
                ),
            )
            index.partialUpdateObjects(ob, true)
        }
        scope.await()
    }

    fun insertBid(bidItemModel: BidItemModel) = flow<State<String>> {
        emit(State.loading())
        val push = mFirebaseRD.child(FirebaseRealtimeDatabase.BID_ITEM_MODEL).push()
        GthrLogger.d("sdcbsjdb", "bidItemId ${push.key}")
        push.setValue(bidItemModel).await()
        emit(State.success(push.key!!))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    fun insertBuy(collectionInfoId: String, bidItemId: String) = flow<State<String>> {
        emit(State.loading())
        val push = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
            .child(collectionInfoId).child(FirebaseRealtimeDatabase.BUY_LIST).push()
        push.setValue(bidItemId).await()
        emit(State.success(push.key!!))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun updateProductForBid(
        highestBidCost: Int,
        highestBidID: String,
        productType: ProductType,
        refKey: String,
        objectID: String
    ) = flow<State<Boolean>> {
        emit(State.loading())
        updateToAlgoliaForBid(objectID, highestBidCost, highestBidID)
        var ref = mFirebaseRD
        ref = when (productType) {
            ProductType.MAGIC_THE_GATHERING -> ref.child(FirebaseRealtimeDatabase.MTG_MODEL)
            ProductType.YUGIOH -> ref.child(FirebaseRealtimeDatabase.YUGIOH_MODEL)
            ProductType.POKEMON -> ref.child(FirebaseRealtimeDatabase.POKEMON_MODEL)
            ProductType.FUNKO -> ref.child(FirebaseRealtimeDatabase.FUNKO_MODEL)
            ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> ref.child(
                FirebaseRealtimeDatabase.SEALED_MODEL
            )
        }
        val map = HashMap<String, Any>()
        map[FirebaseRealtimeDatabase.HIGHEST_BID_COST] = highestBidCost
        map[FirebaseRealtimeDatabase.HIGHEST_BID_ID] = highestBidID
        ref.child(refKey).updateChildren(map).await()
        emit(State.success(true))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun updateToAlgoliaForBid(
        objectID: String,
        highestBidCost: Int,
        highestBidID: String
    ) {
        val scope = CoroutineScope(Dispatchers.IO).async {
            val client = ClientSearch(
                applicationID = ApplicationID(AlgoliaConstants.APP_ID),
                apiKey = APIKey(AlgoliaConstants.APIKey)
            )
            val indexName = IndexName(AlgoliaConstants.ITEM_NAME)
            val index = client.initIndex(indexName)

            val ob = listOf(
                ObjectID(objectID) to Partial.Update(
                    Attribute(AlgoliaConstants.HIGHEST_BID_ID),
                    highestBidID
                ),
                ObjectID(objectID) to Partial.Update(
                    Attribute(AlgoliaConstants.HIGHEST_BID_COST),
                    highestBidCost
                ),
            )
            index.partialUpdateObjects(ob, true)
        }
        scope.await()
    }

    fun getUserDisplayName(collectionID: String) = flow<State<String>> {
        emit(State.loading())
        val dataSnapshot =
            mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL).child(collectionID)
                .child(FirebaseRealtimeDatabase.COLLECTION_DISPLAY_NAME).get().await()
        emit(State.success(dataSnapshot.value.toString()))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getUserImage(collectionID: String) = flow<State<String>> {
        emit(State.loading())
        val ref = mStorageRef.child(FirebaseStorage.PROFILE_IMAGE).child(collectionID)
        val url = ref.downloadUrl.await()
        emit(State.success(url.toString()))
    }.catch {
        emit(State.failed(it.message.toString()))

    }.flowOn(Dispatchers.IO)

    fun authStripeAccount(userId: String? = null) = flow<State<Boolean>> {
        emit(State.loading())
        val dataSnapshot =
            mFirebaseRD.child(FirebaseRealtimeDatabase.STRIPE_ACCOUNT).child(userId!!)
                .child(FirebaseRealtimeDatabase.STRIPE_ACCOUNT_ID).get().await()

        GthrLogger.e("keyValue", dataSnapshot.toString())
        GthrLogger.e("keyValue", dataSnapshot.key.toString())
        GthrLogger.e("keyValue", dataSnapshot.value.toString())

        if (dataSnapshot.value == null) {
            emit(State.success(false))
        } else {
            emit(State.success(true))
        }

        GthrLogger.e("dataSnapshot", "${dataSnapshot.key}: " + dataSnapshot.value.toString())


    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getStripePayoutLink() =
        flow<State<String>> {
            // Emit loading state
            emit(State.loading())

            val payoutLink =
                fetchDataWithoutParameter<HashMap<String, *>>(CloudFunctions.UPDATED_PAYOUT_LINK).await()

            val url = payoutLink[CloudFunctions.NEW_PAYOUT_URL]

            GthrLogger.e("payoutLink", "${url}")

            emit(State.success(url.toString()))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.d("payoutLink", "${it.message}}")
        }.flowOn(Dispatchers.IO)


    // Generate Client Secret Ket for Stripe Payment
    fun generateClientSecret(askId: String? = null, shippingTier: String? = null) =
        flow<State<StripePaymentModel>> {
            // Emit loading state
            emit(State.loading())


            val data = hashMapOf(
                CloudFunctions.ASK_ID to askId,
                CloudFunctions.SHIPPING_TIER to shippingTier
            )


            val responsedata =
                fetchData<HashMap<String, *>>(CloudFunctions.CREATE_BUY_INTENT, data).await()


            val appFee = responsedata[CloudFunctions.APP_FEE].toString()
            val sellerPayout = responsedata[CloudFunctions.SELLER_PAYOUT].toString()
            val buyerCharge = responsedata[CloudFunctions.BUY_CHARGE].toString()
            val clientSecret = responsedata[CloudFunctions.CLIENT_SECRET].toString()

            val stripeModel = StripePaymentModel(appFee, sellerPayout, buyerCharge, clientSecret)
            GthrLogger.e("generateClientSecret", "${stripeModel}")

            emit(State.success(stripeModel))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            //     emit(State.failed("Something went wrong"))
            GthrLogger.d("payoutLink", "${it.message}}")
        }.flowOn(Dispatchers.IO)


    fun buyNow(
        askId: String? = null,
        buyerCharge: String? = null,
        buyerAddressKey: String? = null,
        sellerAddressKey: String? = null,
        shippingTierKey: String? = null,
        appFee: String? = null,
        paymentId: String? = null,
        sellerPayout: String? = null
    ) =
        flow<State<ReceiptDomainModel>> {
            // Emit loading state
            emit(State.loading())


            var addressKey =
                mFirebaseRD.child(FirebaseRealtimeDatabase.ASK_ITEM_MODEL).child(askId.toString())
                    .child(CloudFunctions.ADDRESS_KEY).get().await().value

            GthrLogger.d("keyValue", addressKey.toString())

            if (addressKey == null) {
                addressKey = "0"
            }

            val data = hashMapOf(
                CloudFunctions.ASKID to askId,
                CloudFunctions.BUY_CHARGE to buyerCharge,
                CloudFunctions.BUYER_ADDRESS_KEY to buyerAddressKey,
                CloudFunctions.SELLER_ADDRESS_KEY to addressKey.toString(),
                CloudFunctions.SHIPPING_TIER_KEY to shippingTierKey,
                CloudFunctions.APP_FEE to appFee?.toDouble(),
                CloudFunctions.PAYMENT_ID to paymentId,
                CloudFunctions.SELLER_PAYOUT to sellerPayout
            )

            print("${data}")

            GthrLogger.e("BuyNowData", "${data}")

            val responseData =
                fetchData<HashMap<String, String>>(CloudFunctions.BUY_NOW, data).await()

            GthrLogger.d("BuyNowResponse", responseData.toString())
            val reciept = responseData[CloudFunctions.RECIEPT] as HashMap<String, String>

            try {
                val gsonn = GsonBuilder().create()
                val json = gsonn.toJson(reciept)
                val dataRec: ReceiptModel = gsonn.fromJson(json, ReceiptModel::class.java).apply {
                    orderStatus = responseData["order_status"].toString()
                }
                emit(State.success(dataRec.toReceiptDomainModel()))

            } catch (ex: Exception) {
                emit(State.failed("${ex.message}"))
                GthrLogger.d("BuyNowResponse", "${ex.message}}")
            }


        }.catch {
            GthrLogger.d("BuyNowResponse", "${it.message}}")
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            //    emit(State.failed("Something went wrong"))
            it.printStackTrace()

        }.flowOn(Dispatchers.IO)

    fun deleteAsk(collectionId: String, askRefKey: String) = flow<State<Boolean>> {
        emit(State.loading())

        //Delete AskItemModel
        mFirebaseRD.child(FirebaseRealtimeDatabase.ASK_ITEM_MODEL).child(askRefKey)
            .removeValue().await()
        val collectionListLink =
            mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL).child(collectionId)
                .child(FirebaseRealtimeDatabase.COLLECTION_LIST)
        //Delete AskRefKey from CollectionList item
        val collectListRefKey =
            collectionListLink.orderByChild(FirebaseRealtimeDatabase.ASK_REF_KEY).equalTo(askRefKey)
                .get().await().children.first().key
        collectionListLink.child(collectListRefKey.toString())
            .child(FirebaseRealtimeDatabase.ASK_REF_KEY).removeValue().await()

        //Remove AskRefKey from User's SellList
        val sellList = collectionListLink.child(FirebaseRealtimeDatabase.SELL_LIST).get().await()
            .getValue(object : GenericTypeIndicator<List<String>>() {})
        val newSellList = mutableListOf<String>()
        if (!sellList.isNullOrEmpty()) {
            newSellList.addAll(sellList)
            newSellList.remove(askRefKey)
        }
        collectionListLink.child(FirebaseRealtimeDatabase.SELL_LIST).setValue(newSellList).await()

        emit(State.success(true))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
        GthrLogger.e("deleteAsk", "${it.message}}")
    }.flowOn(Dispatchers.IO)

    fun deleteCollectionItem(collectionId: String, collectionRefKey: String) =
        flow<State<Boolean>> {
            emit(State.loading())

            mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL).child(collectionId)
                .child(FirebaseRealtimeDatabase.COLLECTION_LIST).child(collectionRefKey)
                .removeValue().await()

            emit(State.success(true))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.e("deleteAsk", "${it.message}}")
        }.flowOn(Dispatchers.IO)

    fun deleteBidItemModel(collectionId: String, bidRefKey: String) = flow<State<Boolean>> {
        emit(State.loading())

        //Remove bitRefKey from BuyList in CollectionInfoModel
        val buyListLink =
            mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL).child(collectionId)
                .child(FirebaseRealtimeDatabase.BUY_LIST)
        buyListLink.get().await()
            .getValue(object : GenericTypeIndicator<HashMap<String, String>>() {})
            ?.forEach { (key, value) ->
                if (value == bidRefKey)
                    buyListLink.child(key).removeValue().await()
            }

        //Remove respective bidItemModel
        mFirebaseRD.child(FirebaseRealtimeDatabase.BID_ITEM_MODEL).child(bidRefKey).removeValue()
            .await()

        emit(State.success(true))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
        GthrLogger.e("deleteAsk", "${it.message}}")
    }.flowOn(Dispatchers.IO)

}



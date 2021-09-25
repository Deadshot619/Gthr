package com.gthr.gthrcollect.data.repository

import android.net.Uri
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.*
import com.algolia.search.model.indexing.Partial
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.data.remote.fetchData
import com.gthr.gthrcollect.data.remote.fetchDataWithoutParameter
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ShippingInfoDomainModel
import com.gthr.gthrcollect.model.mapper.*
import com.gthr.gthrcollect.model.network.firebaserealtimedb.*
import com.gthr.gthrcollect.utils.constants.AlgoliaConstants
import com.gthr.gthrcollect.utils.constants.CloudFunctions
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.FirebaseStorage
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.getUserCollectionId
import com.gthr.gthrcollect.utils.logger.GthrLogger
import io.ktor.util.*
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

    fun uploadCollectionImage(url: String,imageName : String ,imageSide: String, uid: String) = flow<State<String>> {
        emit(State.loading())
        val file = Uri.fromFile(File(url))
        val ref = mStorageRef.child(FirebaseStorage.COLLECTION_IMAGE).child(uid).child(imageSide).child("${imageName}.png")
        ref.putFile(file).await()
        val url = ref.downloadUrl.await()
        emit(State.success(url.toString()))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        GthrLogger.d("sdjns","error   "+it.message.toString())
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun insertCollection(collectionInfoId : String, mCollectionItemModel : CollectionItemModel) = flow<State<String>>{
        emit(State.loading())
        val push = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL).child(collectionInfoId).child(FirebaseRealtimeDatabase.COLLECTION_LIST).push()
        push.setValue(mCollectionItemModel).await()
        emit(State.success(push.key!!))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun updateCollection(collectionInfoId : String, collectionKey : String, frontImageUrl : String, backImageUrl : String,askId : String? = null ) = flow<State<Boolean>>{
        emit(State.loading())
        val map = hashMapOf<String,Any>(
            FirebaseRealtimeDatabase.FRONT_IMAGE_URL to frontImageUrl,
            FirebaseRealtimeDatabase.BACK_IMAGE_URL to backImageUrl,
            FirebaseRealtimeDatabase.ASK_REF_KEY to askId!!,
        )
        mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL).child(collectionInfoId)
            .child(FirebaseRealtimeDatabase.COLLECTION_LIST).child(collectionKey)
            .updateChildren(map).await()
        emit(State.success(true))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun insertAsk(askItemModel: AskItemModel) = flow<State<String>> {
        emit(State.loading())
        val push = mFirebaseRD.child(FirebaseRealtimeDatabase.ASK_ITEM_MODEL).push()
        GthrLogger.d("sdcbsjdb","ASkItemId ${push.key}")
        push.setValue(askItemModel).await()
        emit(State.success(push.key!!))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun updateProductForAsk(lowestAskCost : Int, lowestAskID : String, productType: ProductType, refKey : String, objectID: String) = flow<State<Boolean>> {
        emit(State.loading())
        updateToAlgoliaForAsk(objectID,lowestAskCost,lowestAskID)
        var ref = mFirebaseRD
        ref = when (productType) {
            ProductType.MAGIC_THE_GATHERING -> ref.child(FirebaseRealtimeDatabase.MTG_MODEL)
            ProductType.YUGIOH -> ref.child(FirebaseRealtimeDatabase.YUGIOH_MODEL)
            ProductType.POKEMON -> ref.child(FirebaseRealtimeDatabase.POKEMON_MODEL)
            ProductType.FUNKO -> ref.child(FirebaseRealtimeDatabase.FUNKO_MODEL)
            ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> ref.child(FirebaseRealtimeDatabase.SEALED_MODEL)
        }
        val map = HashMap<String,Any>()
        map[FirebaseRealtimeDatabase.LOWEST_ASK_COST] = lowestAskCost
        map[FirebaseRealtimeDatabase.LOWEST_ASK_ID] = lowestAskID
        ref.child(refKey).updateChildren(map).await()
        emit(State.success(true))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun updateToAlgoliaForAsk(objectID: String, lowestAskCost : Int, lowestAskID : String) {
        val scope = CoroutineScope(Dispatchers.IO).async {
            val client = ClientSearch(
                applicationID = ApplicationID(AlgoliaConstants.APP_ID),
                apiKey = APIKey(AlgoliaConstants.APIKey)
            )
            val indexName = IndexName(AlgoliaConstants.ITEM_NAME)
            val index = client.initIndex(indexName)

            val ob = listOf(
                ObjectID(objectID) to Partial.Update(Attribute(AlgoliaConstants.LOWEST_ASK_ID), lowestAskID),
                ObjectID(objectID) to Partial.Update(Attribute(AlgoliaConstants.LOWEST_ASK_COST), lowestAskCost),
            )
            index.partialUpdateObjects(ob,true)
        }
        scope.await()
    }

    fun insertBid(bidItemModel : BidItemModel) = flow<State<String>> {
        emit(State.loading())
        val push = mFirebaseRD.child(FirebaseRealtimeDatabase.BID_ITEM_MODEL).push()
        GthrLogger.d("sdcbsjdb","bidItemId ${push.key}")
        push.setValue(bidItemModel).await()
        emit(State.success(push.key!!))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    fun insertBuy(collectionInfoId : String, bidItemId : String) = flow<State<String>>{
        emit(State.loading())
        val push = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL).child(collectionInfoId).child(FirebaseRealtimeDatabase.BUY_LIST).push()
        push.setValue(bidItemId).await()
        emit(State.success(push.key!!))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun updateProductForBid(highestBidCost : Int, highestBidID : String, productType: ProductType, refKey : String, objectID: String) = flow<State<Boolean>> {
        emit(State.loading())
        updateToAlgoliaForBid(objectID,highestBidCost,highestBidID)
        var ref = mFirebaseRD
        ref = when (productType) {
            ProductType.MAGIC_THE_GATHERING -> ref.child(FirebaseRealtimeDatabase.MTG_MODEL)
            ProductType.YUGIOH -> ref.child(FirebaseRealtimeDatabase.YUGIOH_MODEL)
            ProductType.POKEMON -> ref.child(FirebaseRealtimeDatabase.POKEMON_MODEL)
            ProductType.FUNKO -> ref.child(FirebaseRealtimeDatabase.FUNKO_MODEL)
            ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> ref.child(FirebaseRealtimeDatabase.SEALED_MODEL)
        }
        val map = HashMap<String,Any>()
        map[FirebaseRealtimeDatabase.HIGHEST_BID_COST] = highestBidCost
        map[FirebaseRealtimeDatabase.HIGHEST_BID_ID] = highestBidID
        ref.child(refKey).updateChildren(map).await()
        emit(State.success(true))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    private suspend fun updateToAlgoliaForBid(objectID: String, highestBidCost : Int, highestBidID : String) {
        val scope = CoroutineScope(Dispatchers.IO).async {
            val client = ClientSearch(
                applicationID = ApplicationID(AlgoliaConstants.APP_ID),
                apiKey = APIKey(AlgoliaConstants.APIKey)
            )
            val indexName = IndexName(AlgoliaConstants.ITEM_NAME)
            val index = client.initIndex(indexName)

            val ob = listOf(
                ObjectID(objectID) to Partial.Update(Attribute(AlgoliaConstants.HIGHEST_BID_ID), highestBidID),
                ObjectID(objectID) to Partial.Update(Attribute(AlgoliaConstants.HIGHEST_BID_COST), highestBidCost),
            )
            index.partialUpdateObjects(ob,true)
        }
        scope.await()
    }

    //  "acct_1IIPTp2a5NtXmrBn"
    fun authStripeAccount(userId : String?=null) = flow<State<Boolean>>{
        emit(State.loading())
      val dataSnapshot =  mFirebaseRD.child(FirebaseRealtimeDatabase.STRIPE_ACCOUNT).child(userId!!).child(FirebaseRealtimeDatabase.STRIPE_ACCOUNT_ID).get().await()

        GthrLogger.e("keyValue",  dataSnapshot.toString())
        GthrLogger.e("keyValue",  dataSnapshot.key.toString())
        GthrLogger.e("keyValue",  dataSnapshot.value.toString())

        if (dataSnapshot.value== null){
            emit(State.success(false))
        }
        else{
            emit(State.success(true))
        }

        GthrLogger.e("dataSnapshot","${dataSnapshot.key}: "+dataSnapshot.value.toString())


    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getUserDisplayName(collectionID : String) = flow<State<String>>{
        emit(State.loading())
        val dataSnapshot = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL).child(collectionID).child(FirebaseRealtimeDatabase.COLLECTION_DISPLAY_NAME).get().await()
        emit(State.success(dataSnapshot.value.toString()))
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getUserImage(collectionID : String) = flow<State<String>>{
        emit(State.loading())
        val ref = mStorageRef.child(FirebaseStorage.PROFILE_IMAGE).child(collectionID)
        val url = ref.downloadUrl.await()
        emit(State.success(url.toString()))
    }.catch {
        emit(State.failed(it.message.toString()))

    }.flowOn(Dispatchers.IO)

    fun getStripePayoutLink() =
        flow<State<String>> {
            // Emit loading state
            emit(State.loading())

            val payoutLink = fetchDataWithoutParameter<HashMap<String,*>>(CloudFunctions.UPDATED_PAYOUT_LINK).await()

            val url=payoutLink[CloudFunctions.NEW_PAYOUT_URL]

            GthrLogger.e("payoutLink","${url}")

            emit(State.success(url.toString()))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.d("payoutLink", "${it.message}}")
        }.flowOn(Dispatchers.IO)

    fun CreateStripeAccount(code:String) =
        flow<State<String>> {
            // Emit loading state
            emit(State.loading())

            val data = hashMapOf(
                CloudFunctions.UID to GthrCollect.prefs?.getUserCollectionId(),
                CloudFunctions.CODE to code,
                CloudFunctions.STATE to "sv_53124"
            )

        //    val uid="&uid"
        //    val state="sv_53124"
        //    val finalUrl="?${CloudFunctions.CODE}=${code}&${CloudFunctions.STATE}=${state}${uid}=${GthrCollect.prefs?.getUserCollectionId()}"


            GthrLogger.e("finalUrl",code)

          //  https://us-central1-dlc-db-staging.cloudfunctions.net/createStripeAccount?code=ac_KI3undnNs4y1ifYVDq4vZB3Xyy75uKJZ&state=sv_53124&uid=-MfbQvrB1q0meIjvH2aO
            val endPoint=""

            val accountDeatils = fetchDataWithoutParameter<HashMap<String,*>>(code).await()


            GthrLogger.e("accountDeatils","${accountDeatils}")

            emit(State.success(accountDeatils.toString()))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.d("payoutLink", "${it.message}}")
        }.flowOn(Dispatchers.IO)

}



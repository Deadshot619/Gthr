package com.gthr.gthrcollect.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.helper.getDomainModelFromNetworkModel
import com.gthr.gthrcollect.utils.helper.getFbRtModelNameFromProduct
import com.gthr.gthrcollect.utils.helper.getModelTypeFromProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class ReceiptRepository {
    private val mFirebaseRD = Firebase.database.reference

    fun fetchProductDetail(productType: ProductType, objectRefKey: String) =
        flow<State<ProductDisplayModel?>> {
            // Emit loading state
            emit(State.loading())

            val fbRtModelName = getFbRtModelNameFromProduct(productType)
            val modelType = getModelTypeFromProduct(productType)
            val snapShot = mFirebaseRD.child(fbRtModelName).child(objectRefKey).get().await()
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

}
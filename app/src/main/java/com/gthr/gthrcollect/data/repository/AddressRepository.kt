package com.gthr.gthrcollect.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel
import com.gthr.gthrcollect.model.mapper.toShippingAddressDomainModelList
import com.gthr.gthrcollect.model.mapper.toUserInfoDomainModel
import com.gthr.gthrcollect.model.network.firestore.AddressFirestoreModel
import com.gthr.gthrcollect.model.network.firestore.UserInfoFirestoreModel
import com.gthr.gthrcollect.utils.constants.Firestore
import com.gthr.gthrcollect.utils.constants.Firestore.COLLECTION_ADDRESS_LIST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class AddressRepository {

    private val mFirestore = Firebase.firestore


    fun updateAddressListFirestore(list : List<AddressFirestoreModel>, id: String) =
        flow<State<List<ShippingAddressDomainModel>>> {
            emit(State.loading())
            val data = mFirestore.collection(Firestore.COLLECTION_USER_INFO)
                .document(id).update(COLLECTION_ADDRESS_LIST, list).await()
            emit (State.Success(list.toShippingAddressDomainModelList()))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)


    fun getAddressFirestore(uId : String) =
        flow<State<List<ShippingAddressDomainModel>>> {
            emit(State.loading())
            val res = mFirestore.collection(Firestore.COLLECTION_USER_INFO)
                .document(uId).get().await()
            val data = res.toObject(UserInfoFirestoreModel::class.java)!!
            emit(State.Success(data.toUserInfoDomainModel().addressList))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}
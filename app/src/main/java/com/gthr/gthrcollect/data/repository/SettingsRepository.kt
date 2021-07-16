package com.gthr.gthrcollect.data.repository

import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.EditAccInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toEditAccountDomainModel
import com.gthr.gthrcollect.model.network.firestore.EditAccInfoFireStoreModel
import com.gthr.gthrcollect.model.network.firestore.UserInfoFirestoreModel
import com.gthr.gthrcollect.utils.constants.Firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class SettingsRepository {

    private val mFirestore = Firebase.firestore

    fun updateUserDataFirestore(editAccInfoFirestoreModel: EditAccInfoFireStoreModel) =
        flow<State<Boolean>> {
            emit(State.loading())
            val data = mFirestore.collection(Firestore.COLLECTION_USER_INFO)
                .document(GthrCollect.prefs?.signedInUser!!.uid).set(editAccInfoFirestoreModel, SetOptions.merge()).await()
            emit(State.Success(true))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun getUserDataFirestore(uId : String) =
        flow<State<EditAccInfoDomainModel>> {
            emit(State.loading())
            val res = mFirestore.collection(Firestore.COLLECTION_USER_INFO)
                .document(uId).get().await()
            val data = res.toObject(UserInfoFirestoreModel::class.java)!!
            emit(State.Success(data.toEditAccountDomainModel()))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}
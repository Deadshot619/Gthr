package com.gthr.gthrcollect.ui.profile.editprofile

import android.net.Uri
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toCollectionInfoDomainModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.CollectionInfoModel
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.FirebaseStorage
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val mFirebaseRD = Firebase.database.reference
    private val mStorageRef = Firebase.storage.reference

    fun fetchUserProfileData() =
        flow<State<CollectionInfoDomainModel>> {
            emit(State.loading())
            val collectionInfo = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(GthrCollect.prefs?.userInfoModel?.collectionId.toString()).get().await()
                .getValue(
                    CollectionInfoModel::class.java
                )

            emit(State.Success(collectionInfo!!.toCollectionInfoDomainModel()))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun fetchUserProfilePic() =
        flow<State<String>> {
            emit(State.loading())

            val ref = mStorageRef.child(FirebaseStorage.PROFILE_IMAGE)
                .child(GthrCollect.prefs?.userInfoModel?.collectionId.toString())

            val url = ref.downloadUrl.addOnSuccessListener {
                GthrLogger.e("url_it", it.toString())
            }.await()

            emit(State.Success(url.toString()))
            GthrLogger.e("url", url.toString())

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun insertCollectionInfoInRD(collectionInfoModel: CollectionInfoModel) =
        flow<State<String>> {
            emit(State.loading())

            val data = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(GthrCollect.prefs?.userInfoModel?.collectionId.toString())
            data.setValue(collectionInfoModel)
            emit(State.success(data.key.toString()))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.d("Faileeed", it.message.toString())
        }.flowOn(Dispatchers.IO)

    fun uploadProfilePic(uri: Uri) = flow<State<Boolean>> {
        emit(State.loading())

        val ref = mStorageRef.child(FirebaseStorage.PROFILE_IMAGE)
            .child(GthrCollect.prefs?.userInfoModel?.collectionId.toString())
        ref.putFile(uri).await()

        emit(State.success(true))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}
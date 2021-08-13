package com.gthr.gthrcollect.data.repository

import android.net.Uri
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toCollectionInfoDomainModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.CollectionInfoModel
import com.gthr.gthrcollect.model.network.firestore.UserInfoFirestoreModel
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.FirebaseStorage
import com.gthr.gthrcollect.utils.constants.Firestore
import com.gthr.gthrcollect.utils.extensions.updateCollectionInfoModelData
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class ProfileRepository {
    private val mFirestore = Firebase.firestore
    private val mFirebaseRD = Firebase.database.reference
    private val mStorageRef = Firebase.storage.reference

    fun fetchOtherUserProfileData(userId: String) =
        flow<State<CollectionInfoDomainModel>> {
            emit(State.loading())

/*            val userInfo = mFirestore.collection(Firestore.COLLECTION_USER_INFO).document(userId).get().await().toObject(
                UserInfoFirestoreModel::class.java)*/

            val collectionInfo = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(userId).get().await()
                .getValue(CollectionInfoModel::class.java)

            emit(State.Success(collectionInfo!!.toCollectionInfoDomainModel()))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun fetchUserProfileData(collectionId: String) =
        flow<State<CollectionInfoDomainModel>> {
            emit(State.loading())
            val collectionInfo = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(collectionId).get().await()
                .getValue(
                    CollectionInfoModel::class.java
                )

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
            data.setValue(collectionInfoModel)

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
            .child(FirebaseRealtimeDatabase.PROFILE_URL_KEY).setValue(imageUrl.toString())

        emit(State.success(true))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun fetchUserFollowerList() =
        flow<State<List<CollectionInfoDomainModel>>> {

            emit(State.loading())

            val followersList = GthrCollect.prefs?.collectionInfoModel?.followersList
            val arrayList = mutableListOf<CollectionInfoDomainModel>()

            followersList?.forEach { collectionId ->

                val collectionInfo: CollectionInfoDomainModel? =
                    mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                        .child(collectionId).get().await()
                        .getValue(
                            CollectionInfoModel::class.java
                        )?.toCollectionInfoDomainModel(collectionId)

                GthrLogger.e("Followers", collectionId.toString())

                if (collectionInfo != null) {
                    arrayList.add(collectionInfo)
                }

            }
            emit(State.Success(arrayList))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.e("Followers", it.message.toString())

        }.flowOn(Dispatchers.IO)

    fun fetchUserFollowingList() =
        flow<State<List<CollectionInfoDomainModel>>> {

            emit(State.loading())
            val followingList = GthrCollect.prefs?.collectionInfoModel?.favoriteCollectionList
            val arrayList = mutableListOf<CollectionInfoDomainModel>()

            followingList?.forEach { collectionId ->

                val collectionInfo: CollectionInfoDomainModel? =
                    mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                        .child(collectionId).get().await()
                        .getValue(
                            CollectionInfoModel::class.java
                        )?.toCollectionInfoDomainModel(collectionId)

                GthrLogger.e("Followers", collectionId.toString())

                if (collectionInfo != null) {
                    arrayList.add(collectionInfo)
                }

            }
            emit(State.Success(arrayList))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.e("Followers", it.message.toString())

        }.flowOn(Dispatchers.IO)

    fun followToUser(collectionId: String) =
        flow<State<String>> {
            emit(State.loading())

            // Adding another user to my  followersList
            val fList = mutableListOf<String>()
            fList.add(collectionId)
            val data = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(GthrCollect.prefs?.userInfoModel?.collectionId.toString())
                .child(FirebaseRealtimeDatabase.FOLLOWERS_LIST)
            data.setValue(fList).await()

            // Adding Me to  another user's to favoriteCollectionList
            val foList = mutableListOf<String>()
            foList.add(GthrCollect.prefs?.userInfoModel?.collectionId.toString())
            val addToFllower = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(collectionId).child(FirebaseRealtimeDatabase.FAVORITE_COLLECTION_LIST)
            addToFllower.setValue(foList).await()

            emit(State.success(addToFllower.key.toString()))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.d("Faileeed", it.message.toString())
        }.flowOn(Dispatchers.IO)

}
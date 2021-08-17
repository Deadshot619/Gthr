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
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.FirebaseStorage
import com.gthr.gthrcollect.utils.extensions.getUserCollectionId
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
                .child(userId).get().await().getValue(CollectionInfoModel::class.java)

            emit(State.Success(collectionInfo!!.toCollectionInfoDomainModel()))
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun fetchUserProfileData(collectionId: String) =
        flow<State<CollectionInfoDomainModel>> {
            emit(State.loading())
            val collectionInfo = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
                .child(collectionId).get().await().getValue(CollectionInfoModel::class.java)

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
                .child(GthrCollect.prefs?.userInfoModel?.collectionId.toString()).child(FirebaseRealtimeDatabase.ABOUT)
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

            if (followingData.hasChildren()){
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
}
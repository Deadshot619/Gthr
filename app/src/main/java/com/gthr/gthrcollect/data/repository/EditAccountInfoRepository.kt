package com.gthr.gthrcollect.data.repository

import android.net.Uri
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.User
import com.gthr.gthrcollect.model.mapper.*
import com.gthr.gthrcollect.model.network.algolia.AlgoliaCollectionModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.CollectionInfoModel
import com.gthr.gthrcollect.model.network.firestore.UserInfoFirestoreModel
import com.gthr.gthrcollect.utils.constants.AlgoliaConstants
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.FirebaseStorage.GOVERNMENT_ID
import com.gthr.gthrcollect.utils.constants.Firestore
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.io.File

class EditAccountInfoRepository {
    private val mAuth = Firebase.auth
    private val mFirestore = Firebase.firestore
    private val mFirebaseRD = Firebase.database.reference
    private val mStorageRef = Firebase.storage.reference


    fun signInWithOtp(credential: PhoneAuthCredential) = flow<State<Boolean>> {
        // Emit loading state
        emit(State.loading())

        val auth = mAuth.signInWithCredential(credential).await()
        val user = auth.user

        user?.let {
            emit(State.success(true))
        } ?: emit(State.failed("Error occurred"))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    fun userSignUpEmail(email: String, password: String) = flow<State<User>> {
        // Emit loading state
        emit(State.loading())

        val auth = mAuth.createUserWithEmailAndPassword(email, password).await()
        val user = auth.user?.toUser()

        user?.let {
            emit(State.success(user))
        } ?: emit(State.failed("Error occurred"))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    fun insertUserDataFirestore(userInfoFirestoreModel: UserInfoFirestoreModel) =
        flow<State<Boolean>> {
            emit(State.loading())

            val data = mFirestore.collection(Firestore.COLLECTION_USER_INFO)
                .document(userInfoFirestoreModel.uid).set(userInfoFirestoreModel).await()
            emit(State.Success(true))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    /**
     * Method to insert User collection data into [FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL] in Firebase RealtimeDatabase
     */
    fun insertCollectionInfoInRD(collectionInfoModel: CollectionInfoModel) =
        flow<State<String>> {
            emit(State.loading())

            val data = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
            val key = data.push().key
            data.child(key!!).setValue(collectionInfoModel).await()

            uploadDataToAlgolia(collectionInfoModel)

            emit(State.success(key))

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.d("Faileeed", it.message.toString())
        }.flowOn(Dispatchers.IO)

    fun uploadGovtIds(url: String, imageSide: String, uid: String) = flow<State<Boolean>> {
        emit(State.loading())

        val file = Uri.fromFile(File(url))
        val ref = mStorageRef.child(GOVERNMENT_ID).child(imageSide).child(uid)
        ref.putFile(file).await()

        emit(State.success(true))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    suspend fun uploadDataToAlgolia(collectionInfoModel: CollectionInfoModel) {

        val client = ClientSearch(
            applicationID = ApplicationID(AlgoliaConstants.APP_ID),
            apiKey = APIKey(AlgoliaConstants.APIKey)
        )
        val indexName = IndexName(AlgoliaConstants.COLLECTION_INFO_MODEL)
        val index = client.initIndex(indexName)
        val collectionInfoList = listOf(
            collectionInfoModel.toAlgoliaCollectionModel()
        )

        index.saveObjects(
            AlgoliaCollectionModel.serializer(),
            collectionInfoList
        )
    }

}


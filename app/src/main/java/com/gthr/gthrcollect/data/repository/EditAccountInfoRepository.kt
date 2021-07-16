package com.gthr.gthrcollect.data.repository

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.User
import com.gthr.gthrcollect.model.mapper.toUser
import com.gthr.gthrcollect.model.network.firebaserealtimedb.CollectionInfoModel
import com.gthr.gthrcollect.model.network.firestore.UserInfoFirestoreModel
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.FirebaseStorage.FIREBASE_STORAGE_LINK
import com.gthr.gthrcollect.utils.constants.Firestore
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class EditAccountInfoRepository {
    private val mAuth = Firebase.auth
    private val mFirestore = Firebase.firestore
    private val mFirebaseRD = Firebase.database.reference
    private val mStorageRef = Firebase.storage(FIREBASE_STORAGE_LINK).reference

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
        callbackFlow<State<String>> {
            trySend(State.loading())

            val data = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL)
            val key = data.push().key

            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild(FirebaseRealtimeDatabase.USER_REF_KEY))
                        trySend(State.success(key!!))
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(State.failed(error.message))
                }
            }

            data.child(key!!).addValueEventListener(listener)
            data.child(key).setValue(collectionInfoModel)

            awaitClose {    //Have to implement this block & remove listener to avoid memory leaks when using callback & callback flow
                data.child(key).removeEventListener(listener)
            }
        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.d("Faileeed", it.message.toString())
        }.flowOn(Dispatchers.IO)
}
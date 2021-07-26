package com.gthr.gthrcollect.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.domain.User
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toCollectionInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toUser
import com.gthr.gthrcollect.model.mapper.toUserInfoDomainModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.CollectionInfoModel
import com.gthr.gthrcollect.model.network.firestore.UserInfoFirestoreModel
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.Firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class SignInFlowRepository {
    private val mAuth = Firebase.auth
    private val mFirestore = Firebase.firestore
    private val mFirebaseRD = Firebase.database.reference

    fun userSignIn(email: String, password: String) = flow<State<User>> {
        // Emit loading state
        emit(State.loading())

        val auth = mAuth.signInWithEmailAndPassword(email, password).await()
        val user = auth.user?.toUser()

        user?.let {
            emit(State.success(user))
        } ?: emit(State.failed("Error occurred"))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    fun checkIfUserExists(email: String) = flow<State<Boolean>> {
        emit(State.loading())

        val auth = mAuth.fetchSignInMethodsForEmail(email).await()
        val isNewUser: Boolean = auth.signInMethods?.isEmpty() ?: false
        emit(State.Success(isNewUser))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    /**
     * Method to get user's [UserInfoFirestoreModel] from Firestore & [CollectionInfoModel] from Firebase Realtime Database
     */
    fun getUserData(uid: String) = flow<State<Pair<UserInfoDomainModel, CollectionInfoDomainModel>>>{
        emit(State.loading())

        val userInfo = mFirestore.collection(Firestore.COLLECTION_USER_INFO).document(uid).get().await().toObject(UserInfoFirestoreModel::class.java)
        val collectionInfo = mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL).child(userInfo!!.collectionId).get().await().getValue(CollectionInfoModel::class.java)

        emit(State.success(Pair(userInfo.toUserInfoDomainModel(), collectionInfo!!.toCollectionInfoDomainModel())))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}
package com.gthr.gthrcollect.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.User
import com.gthr.gthrcollect.model.mapper.toUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class SignInFlowRepository {
    private val mAuth = Firebase.auth

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

}
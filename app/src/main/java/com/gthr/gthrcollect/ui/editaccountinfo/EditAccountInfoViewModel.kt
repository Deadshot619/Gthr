package com.gthr.gthrcollect.ui.editaccountinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.PhoneAuthCredential
import com.gthr.gthrcollect.data.repository.EditAccountInfoRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.User
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toFirestoreModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditAccountInfoViewModel(private val repository: EditAccountInfoRepository) :
    BaseViewModel() {

    //Variable to hold User data retrieved from FirebaseAuth
    private val _userInfo = MutableLiveData<State<User>>()
    val userInfo: LiveData<State<User>>
        get() = _userInfo

    //Variable to hold UserInfoDomainModel data
    private val _userInfoLiveData = MutableLiveData<UserInfoDomainModel>(UserInfoDomainModel())
    val userInfoLiveData: LiveData<UserInfoDomainModel>
        get() = _userInfoLiveData

    //Variable to indicate whether otp has been verified or not
    private val _otpVerified = MutableLiveData<Event<State<Boolean>>>()
    val otpVerified: LiveData<Event<State<Boolean>>>
        get() = _otpVerified

    //Variable to indicate whether user data has been added in Firestore
    private val _userDataAddedFirestore = MutableLiveData<Event<State<Boolean>>>()
    val userDataAddedFirestore: LiveData<Event<State<Boolean>>>
        get() = _userDataAddedFirestore

    fun setUserInfo(userInfoDomainModel: UserInfoDomainModel) {
        _userInfoLiveData.value = userInfoDomainModel
    }

    fun verifyOtp(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            repository.signInWithOtp(credential).collect {
                _otpVerified.value = Event(it)
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            repository.userSignUpEmail(email, password).collect {
                _userInfo.postValue(it)
            }
        }
    }

    fun addUserDataFirestore(userInfoDomainModel: UserInfoDomainModel) {
        viewModelScope.launch {
            repository.insertUserDataFirestore(userInfoDomainModel.toFirestoreModel()).collect {
                _userDataAddedFirestore.value = Event(it)
            }
        }
    }
}
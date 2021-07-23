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
import com.gthr.gthrcollect.model.mapper.toCollectionInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toFirestoreModel
import com.gthr.gthrcollect.model.mapper.toRealtimeDatabaseModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.utils.constants.FirebaseStorage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditAccountInfoViewModel(private val repository: EditAccountInfoRepository) :
    BaseViewModel() {
    private var uploadImageJob: Job? = null


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

    private val _userCollectionInfoModelKey = MutableLiveData<Event<State<String>>>()
    val userCollectionInfoModelKey: LiveData<Event<State<String>>>
        get() = _userCollectionInfoModelKey

    //Variable to indicate whether user front Id image uploaded
    private val _frontImageUpload = MutableLiveData<Event<State<Boolean>>>()
    val frontImageUpload: LiveData<Event<State<Boolean>>>
        get() = _frontImageUpload

    //Variable to indicate whether user back Id image uploaded
    private val _backImageUpload = MutableLiveData<Event<State<Boolean>>>()
    val backImageUpload: LiveData<Event<State<Boolean>>>
        get() = _backImageUpload


    fun setUserInfo(userInfoDomainModel: UserInfoDomainModel) {
        _userInfoLiveData.value = userInfoDomainModel
    }


    /*
     *  User will first verify Otp -> sign up using email -> create a collectionInfoModel in Firebase RD -> create a UserInfoModel in Firestore
     */
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

    fun addUserDataFirestore(userInfoDomainModel: UserInfoDomainModel, collectionId: String) {
        viewModelScope.launch {
            repository.insertUserDataFirestore(userInfoDomainModel.toFirestoreModel(collectionId))
                .collect {
                    _userDataAddedFirestore.value = Event(it)
                }
        }
    }

    fun addCollectionInfoModel(userInfoDomainModel: UserInfoDomainModel) {
        viewModelScope.launch {
            repository.insertCollectionInfoInRD(userInfoDomainModel.toRealtimeDatabaseModel())
                .collect {
                    _userCollectionInfoModelKey.value = Event(it)
                }
        }
    }

    fun uploadFrontImage(url: String, uid: String) {
        uploadImageJob?.cancel()
        uploadImageJob = viewModelScope.launch {
            repository.uploadGovtIds(url, FirebaseStorage.FRONT_ID, uid).collect {
                _frontImageUpload.value = Event(it)
            }
        }
    }

    fun uploadBackImage(url: String, uid: String) {
        uploadImageJob?.cancel()
        uploadImageJob = viewModelScope.launch {
            repository.uploadGovtIds(url, FirebaseStorage.BACK_ID, uid).collect {
                _backImageUpload.value = Event(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        uploadImageJob?.cancel()
    }
}
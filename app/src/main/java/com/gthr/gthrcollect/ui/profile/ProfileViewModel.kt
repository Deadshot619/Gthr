package com.gthr.gthrcollect.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toRealtimeDatabaseModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.ui.profile.editprofile.ProfileRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : BaseViewModel() {

    private var uploadImageJob: Job? = null

    //Variable to hold User data retrieved from FireStore
    private val _userCollectionInfo = MutableLiveData<Event<State<CollectionInfoDomainModel>>>()
    val userCollectionInfo: LiveData<Event<State<CollectionInfoDomainModel>>>
        get() = _userCollectionInfo

    private val _userProfilePic = MutableLiveData<Event<State<String>>>()
    val userProfilePic: LiveData<Event<State<String>>>
        get() = _userProfilePic

    private val _userCollectionInfoData = MutableLiveData<Event<State<String>>>()
    val userCollectionInfoData: LiveData<Event<State<String>>>
        get() = _userCollectionInfoData

    //Variable to indicate whether user back Id image uploaded
    private val _profileImageUpload = MutableLiveData<Event<State<Boolean>>>()
    val profileImageUpload: LiveData<Event<State<Boolean>>>
        get() = _profileImageUpload

    init {
        fetchUserProfileData()
        fetchUserProfilePic()
    }

    fun fetchUserProfileData() {
        viewModelScope.launch {
            repository.fetchUserProfileData().collect {
                _userCollectionInfo.value = Event(it)
            }
        }
    }

    fun fetchUserProfilePic() {
        viewModelScope.launch {
            repository.fetchUserProfilePic().collect {
                _userProfilePic.value = Event(it)
            }
        }
    }

    fun addCollectionInfoModel(userInfoDomainModel: UserInfoDomainModel) {
        viewModelScope.launch {
            repository.insertCollectionInfoInRD(userInfoDomainModel.toRealtimeDatabaseModel())
                .collect {
                    _userCollectionInfoData.value = Event(it)
                }
        }
    }

    fun uploadProfileImage(uri: Uri) {
        uploadImageJob?.cancel()
        uploadImageJob = viewModelScope.launch {
            repository.uploadProfilePic(uri).collect {
                _profileImageUpload.value = Event(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        uploadImageJob?.cancel()
    }

}
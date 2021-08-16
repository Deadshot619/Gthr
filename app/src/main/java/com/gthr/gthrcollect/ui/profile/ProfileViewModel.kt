package com.gthr.gthrcollect.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toRealtimeDatabaseModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.data.repository.ProfileRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository, private val otherUserId: String?) : BaseViewModel() {

    private var uploadImageJob: Job? = null

    //Variable to hold User data retrieved from FireStore
    private val _userCollectionInfo = MutableLiveData<Event<State<CollectionInfoDomainModel>>>()
    val userCollectionInfo: LiveData<Event<State<CollectionInfoDomainModel>>>
        get() = _userCollectionInfo

    //Variable to hold User data retrieved from FireStore
    private val _followersList = MutableLiveData<Event<State<List<CollectionInfoDomainModel>>>>()
    val followersList: LiveData<Event<State<List<CollectionInfoDomainModel>>>>
        get() = _followersList

    //Variable to hold User data retrieved from FireStore
    private val _followingList = MutableLiveData<Event<State<List<CollectionInfoDomainModel>>>>()
    val followingList: LiveData<Event<State<List<CollectionInfoDomainModel>>>>
        get() = _followingList

    private val _userCollectionInfoData = MutableLiveData<Event<State<String>>>()
    val userCollectionInfoData: LiveData<Event<State<String>>>
        get() = _userCollectionInfoData

    private val _followUser = MutableLiveData<Event<State<String>>>()
    val followUser: LiveData<Event<State<String>>>
        get() = _followUser

    private val _unFollowUser = MutableLiveData<Event<State<String>>>()
    val unFollowUser: LiveData<Event<State<String>>>
        get() = _unFollowUser

    //Variable to indicate whether user back Id image uploaded
    private val _profileImageUpload = MutableLiveData<Event<State<Boolean>>>()
    val profileImageUpload: LiveData<Event<State<Boolean>>>
        get() = _profileImageUpload

    init {
        if (otherUserId == null)
            fetchUserProfileData()
        else
            fetchCollectionDataFromUserId(otherUserId)
    }

    fun fetchCollectionDataFromUserId(userRefKey: String){
        viewModelScope.launch {
            repository.fetchOtherUserProfileData(userRefKey).collect {
                _userCollectionInfo.value = Event(it)
            }
        }
    }

    fun fetchUserProfileData(collectionId: String=GthrCollect.prefs?.userInfoModel?.collectionId.toString()) {
        viewModelScope.launch {
            repository.fetchUserProfileData(collectionId).collect {
                _userCollectionInfo.value = Event(it)
            }
        }
    }

    fun followersData() {
        viewModelScope.launch {
            repository.fetchUserFollowerList().collect {
                _followersList.value = Event(it)
            }
        }
    }

    fun followingsData() {
        viewModelScope.launch {
            repository.fetchUserFollowingList().collect {
                _followingList.value = Event(it)
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

    fun followToUser(collectionId: String) {
        viewModelScope.launch {
            repository.followToUser(collectionId).collect {
                _followUser.value = Event(it)
            }
        }
    }

    fun unFollowToUser(collectionId: String) {
        viewModelScope.launch {
            repository.unFollowToUser(collectionId).collect {
                _unFollowUser.value = Event(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        uploadImageJob?.cancel()
    }

}
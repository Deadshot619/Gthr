package com.gthr.gthrcollect.ui.editprofile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.ProfileRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toRealtimeDatabaseModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditProfileViewModel(private val repository: ProfileRepository): BaseViewModel() {

    private var uploadImageJob: Job? = null

    private val _userCollectionInfoData = MutableLiveData<Event<State<String>>>()
    val userCollectionInfoData: LiveData<Event<State<String>>>
        get() = _userCollectionInfoData

    //Variable to indicate whether user back Id image uploaded
    private val _profileImageUpload = MutableLiveData<Event<State<Boolean>>>()
    val profileImageUpload: LiveData<Event<State<Boolean>>>
        get() = _profileImageUpload

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
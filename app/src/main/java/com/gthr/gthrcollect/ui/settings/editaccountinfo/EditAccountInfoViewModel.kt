package com.gthr.gthrcollect.ui.settings.editaccountinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.data.repository.SettingsRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.EditAccInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toFirestoreModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditAccountInfoViewModel(private val repository: SettingsRepository) : BaseViewModel() {

    //Variable to hold User data retrieved from FireStore
    private val _userInfo = MutableLiveData<Event<State<EditAccInfoDomainModel>>>()
    val userInfo: LiveData<Event<State<EditAccInfoDomainModel>>>
        get() = _userInfo

    //Variable to indicate whether user data has been updated in Firestore
    private val _userDataUpdateFirestore = MutableLiveData<Event<State<Boolean>>>()
    val userDataUpdateFirestore: LiveData<Event<State<Boolean>>>
        get() = _userDataUpdateFirestore

    init {
        getUserDataFirestore(GthrCollect.prefs?.signedInUser!!.uid)
    }

    private fun getUserDataFirestore(uId : String) {
        viewModelScope.launch {
            repository.getUserDataFirestore(uId).collect {
                _userInfo.value = Event(it)
            }
        }
    }

    fun updateUserDataFirestore(editAccInfoDomainModel: EditAccInfoDomainModel) {
        viewModelScope.launch {
            repository.updateUserDataFirestore(editAccInfoDomainModel.toFirestoreModel()).collect {
                _userDataUpdateFirestore.value = Event(it)
            }
        }
    }
}
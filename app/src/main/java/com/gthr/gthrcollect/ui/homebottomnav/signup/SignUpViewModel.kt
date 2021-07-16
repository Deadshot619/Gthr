package com.gthr.gthrcollect.ui.homebottomnav.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.SignInFlowRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: SignInFlowRepository) : BaseViewModel() {

    private var mSignUpJob: Job? = null

    private val _newUser = MutableLiveData<Event<State<Boolean>>>()
    val newUser: LiveData<Event<State<Boolean>>>
        get() = _newUser

    fun checkIfUserExists(email: String) {
        mSignUpJob?.cancel()
        mSignUpJob = viewModelScope.launch {
            repository.checkIfUserExists(email).collect {
                _newUser.value = Event(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mSignUpJob?.cancel()
    }
}
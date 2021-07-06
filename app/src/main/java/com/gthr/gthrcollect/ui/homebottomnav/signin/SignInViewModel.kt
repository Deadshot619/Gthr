package com.gthr.gthrcollect.ui.homebottomnav.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.SignInFlowRepository
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.User
import com.gthr.gthrcollect.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignInViewModel(private val signInFlowRepository: SignInFlowRepository) : BaseViewModel() {

    private val _userInfo = MutableLiveData<State<User>>()
    val userInfo: LiveData<State<User>>
        get() = _userInfo

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInFlowRepository.userSignIn(email, password).collect {
                _userInfo.postValue(it)
            }
        }
    }
}
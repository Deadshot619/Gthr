package com.gthr.gthrcollect.ui.homebottomnav.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.SignInFlowRepository

class SignInViewModelFactory(private val repository: SignInFlowRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
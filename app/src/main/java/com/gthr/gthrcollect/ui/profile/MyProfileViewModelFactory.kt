package com.gthr.gthrcollect.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.ProfileRepository

class MyProfileViewModelFactory(private val repository: ProfileRepository, private val otherUserId: String?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository, otherUserId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
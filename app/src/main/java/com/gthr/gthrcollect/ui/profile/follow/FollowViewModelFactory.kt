package com.gthr.gthrcollect.ui.profile.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.ProfileRepository
import com.gthr.gthrcollect.data.repository.SearchRepository

class FollowViewModelFactory(private val repository: ProfileRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FollowViewModel::class.java)) {
            return FollowViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
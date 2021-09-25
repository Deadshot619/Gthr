package com.gthr.gthrcollect.ui.profile.favsold

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.ProfileRepository

class FavSoldViewModelFactory(private val repository: ProfileRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavSoldViewModel::class.java)) {
            return FavSoldViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
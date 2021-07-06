package com.gthr.gthrcollect.ui.editaccountinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.EditAccountInfoRepository

class EditAccountInfoViewModelFactory(private val repository: EditAccountInfoRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditAccountInfoViewModel::class.java)) {
            return EditAccountInfoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
package com.gthr.gthrcollect.ui.settings.settingsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.ui.settings.SettingsViewModel

class SettingsViewModelFactory(private val repository: AskFlowRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
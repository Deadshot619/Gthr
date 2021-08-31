package com.gthr.gthrcollect.ui.askflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.AskFlowRepository

class AskFlowViewModelFactory(private val repository: AskFlowRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AskFlowViewModel::class.java)) {
            return AskFlowViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
package com.gthr.gthrcollect.ui.homebottomnav.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.FeedRepository

class MarketViewModelFactory(private val repository: FeedRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarketViewModel::class.java)) {
            return MarketViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
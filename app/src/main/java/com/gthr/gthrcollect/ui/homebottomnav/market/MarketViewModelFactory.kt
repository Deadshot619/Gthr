package com.gthr.gthrcollect.ui.homebottomnav.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.FeedRepository
import com.gthr.gthrcollect.data.repository.SearchRepository

class MarketViewModelFactory(private val feedRepository: FeedRepository, private val searchRepository: SearchRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MarketViewModel::class.java)) {
            return MarketViewModel(feedRepository,searchRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
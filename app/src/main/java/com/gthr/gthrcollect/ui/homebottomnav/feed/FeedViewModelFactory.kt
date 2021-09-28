package com.gthr.gthrcollect.ui.homebottomnav.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.data.repository.FeedRepository

class FeedViewModelFactory(private val feedRepository: FeedRepository,private val dynamicLinkRepository : DynamicLinkRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            return FeedViewModel(feedRepository,dynamicLinkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
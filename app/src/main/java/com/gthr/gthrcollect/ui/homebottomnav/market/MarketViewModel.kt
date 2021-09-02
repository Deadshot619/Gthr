package com.gthr.gthrcollect.ui.homebottomnav.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.FeedRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MarketViewModel(private val repository: FeedRepository) : ViewModel() {

    private val _bannerImage = MutableLiveData<Event<State<String>>>()
    val bannerImage: LiveData<Event<State<String>>>
        get() = _bannerImage

    init {
        fetchBannerImage()
    }

    fun fetchBannerImage() {
        viewModelScope.launch {
            repository.fetchBannerImage().collect {
                _bannerImage.value = Event(it)
            }
        }
    }

}
package com.gthr.gthrcollect.ui.homebottomnav.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.FeedRepository
import com.gthr.gthrcollect.data.repository.SearchRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.model.domain.SearchCollection
import com.gthr.gthrcollect.utils.enums.ProductCategory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MarketViewModel(
    private val repository: FeedRepository, private val repositorySearch: SearchRepository
) : ViewModel() {


    private var searchCollectionJob: Job? = null
    private var searchAskJobLowest: Job? = null
    private var searchAskJobhighest: Job? = null


    private val _bannerImage = MutableLiveData<Event<State<String>>>()
    val bannerImage: LiveData<Event<State<String>>>
        get() = _bannerImage

    private val _collectionList = MutableLiveData<Event<State<List<SearchCollection>>>>()
    val collectionList: LiveData<Event<State<List<SearchCollection>>>>
        get() = _collectionList

    private val _lowestAskList = MutableLiveData<Event<State<List<ProductDisplayModel>>>>()
    val lowestAskList: LiveData<Event<State<List<ProductDisplayModel>>>>
        get() = _lowestAskList

    private val _heightAskList = MutableLiveData<Event<State<List<ProductDisplayModel>>>>()
    val heightAskList: LiveData<Event<State<List<ProductDisplayModel>>>>
        get() = _heightAskList


    init {
        fetchBannerImage()
        searchCollection(limit = ITEM_LIMIT)
    }

    fun fetchBannerImage() {
        viewModelScope.launch {
            repository.fetchBannerImage().collect {
                _bannerImage.value = Event(it)
            }
        }
    }

    fun searchCollection(searchTerm: String? = null, limit: Int? = null, page: Int? = null) {
        searchCollectionJob?.cancel()
        searchCollectionJob = viewModelScope.launch {
            repositorySearch.fetchCollection(searchTerm, limit, page).collect {
                _collectionList.value = Event(it)
            }
        }
    }
    fun searchLowestAsk(searchTerm: String? = null, limit: Int? = null, page: Int? = null, sortBy:String?=null,isAscending:Int?, productCategory: String? = null) {
        searchAskJobLowest?.cancel()
        searchAskJobLowest = viewModelScope.launch {
            repositorySearch.fetchSearchAsk(searchTerm, limit, page,sortBy,isAscending,productCategory).collect {
                _lowestAskList.value = Event(it)
            }
        }
    }

    fun searchHeightsAsk(searchTerm: String? = null, limit: Int? = null, page: Int? = null, sortBy:String?=null, isAscending:Int?,   productCategory: String? = null) {
        searchAskJobhighest?.cancel()
        searchAskJobhighest = viewModelScope.launch {
            repositorySearch.fetchSearchAsk(searchTerm, limit, page,sortBy,isAscending,productCategory).collect {
                _heightAskList.value = Event(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchCollectionJob?.cancel()
        searchAskJobLowest?.cancel()
        searchAskJobhighest?.cancel()

    }

    companion object {
        private const val ITEM_LIMIT = 10
    }

}
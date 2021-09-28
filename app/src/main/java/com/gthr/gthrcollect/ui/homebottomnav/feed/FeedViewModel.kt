package com.gthr.gthrcollect.ui.homebottomnav.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.data.repository.FeedRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.FeedDomainModel
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.addAllFeed
import com.gthr.gthrcollect.utils.extensions.addFeedLoadMore
import com.gthr.gthrcollect.utils.extensions.clear
import com.gthr.gthrcollect.utils.extensions.removeFeedLoadMore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FeedViewModel(private val feedRepository: FeedRepository,private val dynamicLinkRepository : DynamicLinkRepository) : ViewModel() {

    private var fetchFeed: Job? = null

    private val _mFeedList = MutableLiveData<Event<State<List<FeedDomainModel>>>>()
    val mFeedList: LiveData<Event<State<List<FeedDomainModel>>>>
        get() = _mFeedList

    fun fetchFeed(limit: Int?, page: Int?, productCategory: ProductCategory?, creatorUID: String?){
        fetchFeed?.cancel()
        fetchFeed = viewModelScope.launch {
            feedRepository.fetchFeed(limit,page,productCategory,creatorUID).collect {
                _mFeedList.value = Event(it)
            }
        }
    }

    private val _mFeedDisplayList = MutableLiveData<MutableList<FeedDomainModel>>()
    val mFeedDisplayList: LiveData<MutableList<FeedDomainModel>>
        get() = _mFeedDisplayList

    private val _mDynamicLink = MutableLiveData<Event<State<String>>>()
    val mDynamicLink: LiveData<Event<State<String>>>
        get() = _mDynamicLink

    // For Feed
    fun setFeedDisplayList(list : List<FeedDomainModel>){
        _mFeedDisplayList.addAllFeed(list)
    }

    fun clearFeedDisplayList(){
        _mFeedDisplayList.clear()
    }

    fun addFeedDisplayListLoadMore(){
        _mFeedDisplayList.addFeedLoadMore()
    }

    fun removeFeedDisplayListLoadMore(){
        _mFeedDisplayList.removeFeedLoadMore()
    }

    fun getProductDynamicLink(value : String, type: ProductType){
        viewModelScope.launch {
            dynamicLinkRepository.getProductDynamicLink(value, type).collect {
                _mDynamicLink.value = Event(it)
            }
        }
    }

    fun getCollectionsDynamicLink(value : String){
        viewModelScope.launch {
            dynamicLinkRepository.getCollectionsDynamicLink(value).collect {
                _mDynamicLink.value = Event(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        fetchFeed?.cancel()
    }
}
package com.gthr.gthrcollect.ui.homebottomnav.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.SearchRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.model.domain.SearchCollection
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.utils.extensions.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepository) : BaseViewModel() {

    private var searchProductJob: Job? = null
    private var searchCollectionJob: Job? = null

    private val _productList = MutableLiveData<Event<State<List<ProductDisplayModel>>>>()
    val productList: LiveData<Event<State<List<ProductDisplayModel>>>>
        get() = _productList

    private val _collectionList = MutableLiveData<Event<State<List<SearchCollection>>>>()
    val collectionList: LiveData<Event<State<List<SearchCollection>>>>
        get() = _collectionList

    private val _loadMore = MutableLiveData<Event<Boolean>>()
    val loadMore: LiveData<Event<Boolean>>
        get() = _loadMore


    private val _productDisplayList = MutableLiveData<MutableList<ProductDisplayModel>>()
    val productDisplayList: LiveData<MutableList<ProductDisplayModel>>
        get() = _productDisplayList

    private val _collectionDisplayList = MutableLiveData<MutableList<SearchCollection>>()
    val collectionDisplayList: LiveData<MutableList<SearchCollection>>
        get() = _collectionDisplayList

    fun setCollectionDisplayList(list : List<SearchCollection>){
        _collectionDisplayList.addAllSearchCollection(list)
    }

    fun clearCollectionDisplayList(){
        _collectionDisplayList.clear()
    }

    fun addSearchCollectionLoadMore(){
        _collectionDisplayList.addSearchCollectionLoadMore()
    }

    fun removeSearchCollectionLoadMore(){
        _collectionDisplayList.removeSearchCollectionLoadMore()
    }

    fun setProductDisplayList(list : List<ProductDisplayModel>){
        _productDisplayList.addAllProductDisplayModel(list)
    }

    fun clearProductDisplayList(){
        _productDisplayList.clear()
    }

    fun addProductDisplayModelLoadMore(){
        _productDisplayList.addProductDisplayModelLoadMore()
    }

    fun removeProductDisplayModelLoadMore(){
        _productDisplayList.removeProductDisplayModelLoadMore()
    }


    fun searchProducts(
        searchTerm: String? = null,
        productCategory: String? = null,
        productType: String? = null,
        limit: Int? = null,
        page: Int? = null
    ) {
        clearJobs()
        searchProductJob = viewModelScope.launch {
            repository.fetchProducts(searchTerm, productCategory, productType, limit, page)
                .collect {
                  _productList.value = Event(it)
                }
        }
    }

    fun searchCollection(searchTerm: String? = null, limit: Int? = null, page: Int? = null) {
        clearJobs()
        searchCollectionJob = viewModelScope.launch {
            repository.fetchCollection(searchTerm, limit, page).collect {
                _collectionList.value = Event(it)
            }
        }
    }

    private fun clearJobs() {
        searchProductJob?.cancel()
        searchCollectionJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        clearJobs()
    }
}
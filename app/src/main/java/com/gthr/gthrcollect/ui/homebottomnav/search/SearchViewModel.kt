package com.gthr.gthrcollect.ui.homebottomnav.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.SearchRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.model.domain.SearchCollection
import com.gthr.gthrcollect.model.network.cloudfunction.ForSaleItemModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.utils.extensions.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepository) : BaseViewModel() {

    private var searchProductJob: Job? = null
    private var searchCollectionJob: Job? = null
    private var searchAskJob: Job? = null


    private val _productList = MutableLiveData<Event<State<List<ProductDisplayModel>>>>()
    val productList: LiveData<Event<State<List<ProductDisplayModel>>>>
        get() = _productList

    private val _collectionList = MutableLiveData<Event<State<List<SearchCollection>>>>()
    val collectionList: LiveData<Event<State<List<SearchCollection>>>>
        get() = _collectionList

    private val _payment = MutableLiveData<Event<State<List<SearchCollection>>>>()
    val payment: LiveData<Event<State<List<SearchCollection>>>>
        get() = _payment

    init {
        stripPayment()
    }

    private val _loadMore = MutableLiveData<Event<Boolean>>()
    val loadMore: LiveData<Event<Boolean>>
        get() = _loadMore


    private val _productDisplayList = MutableLiveData<MutableList<ProductDisplayModel>>()
    val productDisplayList: LiveData<MutableList<ProductDisplayModel>>
        get() = _productDisplayList

    private val _collectionDisplayList = MutableLiveData<MutableList<SearchCollection>>()
    val collectionDisplayList: LiveData<MutableList<SearchCollection>>
        get() = _collectionDisplayList

    private val _forSaleListList = MutableLiveData<Event<State<List<ProductDisplayModel>>>>()
    val forSaleListList: LiveData<Event<State<List<ProductDisplayModel>>>>
        get() = _forSaleListList

    private val _saleDisplayList = MutableLiveData<MutableList<ProductDisplayModel>>()
    val saleDisplayList: LiveData<MutableList<ProductDisplayModel>>
        get() = _saleDisplayList

    // For Collections
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

    // For Products
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

    //  For Sale

    fun setSaleDisplayList(list : List<ProductDisplayModel>){
        _saleDisplayList.addAllProductDisplayModel(list)
    }

    fun clearSaleDisplayList(){
        _saleDisplayList.clear()
    }

    fun addSaleDisplayModelLoadMore(){
        _saleDisplayList.addProductDisplayModelLoadMore()
    }

    fun removeSaleDisplayModelLoadMore(){
        _saleDisplayList.removeProductDisplayModelLoadMore()
    }



    fun searchProductsList(
        searchTerm: String? = null,
        productCategory: String? = null,
        productType: String? = null,
        limit: Int? = null,
        page: Int? = null,
        isAscending:Int?=null,
        sortBy: String? = null
    ) {
        clearJobs()
        searchProductJob = viewModelScope.launch {
            repository.fetchProducts(searchTerm, productCategory, productType, limit, page,isAscending,sortBy)
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

    fun stripPayment() {

        viewModelScope.launch {
            repository.strip().collect {
                _payment.value = Event(it)
            }
        }
    }

    fun searchAsk(searchTerm: String? = null, limit: Int? = null, page: Int? = null, sortBy:String?=null,isAscending:Int?=null,productCategory:String?=null,productType:String?=null) {
        clearJobs()
        searchAskJob = viewModelScope.launch {
            repository.fetchSearchAsk(searchTerm, limit, page,sortBy,isAscending,productCategory,productType).collect {
                _forSaleListList.value = Event(it)
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
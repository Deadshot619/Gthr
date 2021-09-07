package com.gthr.gthrcollect.ui.homebottomnav.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.SearchRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.model.domain.SearchProductDomainModel
import com.gthr.gthrcollect.model.network.cloudfunction.SearchProductModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepository) : BaseViewModel() {

    private val _productList = MutableLiveData<Event<State< List<ProductDisplayModel>>>>()
    val productList: LiveData<Event<State< List<ProductDisplayModel>>>>
        get() = _productList


    fun searchProducts(searchTerm: String?=null, productCategory:String?=null, productType :String?=null, limit:Int?=null, page:Int?=null) {

      viewModelScope.launch {
            repository.fetchProducts(searchTerm,productCategory,productType,limit,page).collect {
                _productList.value = Event(it)
            }
        }
    }

}
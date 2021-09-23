package com.gthr.gthrcollect.ui.profile.follow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.ProfileRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toRealtimeDatabaseModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FollowViewModel(private val repository: ProfileRepository) : BaseViewModel() {


    //Variable to hold User data retrieved from FireStore
    private val _favProductdata = MutableLiveData<Event<State<List<ProductDisplayModel>>>>()
    val favProductdata: LiveData<Event<State<List<ProductDisplayModel>>>>
        get() = _favProductdata

    private val _mDisplayFavProduct = MutableLiveData<Event<List<ProductDisplayModel>>>()
    val mDisplayFavProduct: MutableLiveData<Event<List<ProductDisplayModel>>>
        get() = _mDisplayFavProduct

    var mAllFavProductList = listOf<ProductDisplayModel>()
        private set

    fun setAllFavProductList(list : List<ProductDisplayModel>){
        mAllFavProductList = list
    }

    fun setDisplayFavProducts(list : List<ProductDisplayModel>){
        _mDisplayFavProduct.value = Event(list)
    }

    fun fetchFollowingData(collectionId: String) {
        viewModelScope.launch {
            GthrLogger.e("ProductList", "id: ${collectionId}")
            repository.fetchFavProductsList(collectionId).collect {
                _favProductdata.value = Event(it)
            }
        }
    }
}
package com.gthr.gthrcollect.ui.profile.favsold

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.ProfileRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ItemDisplayDomainModel
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavSoldViewModel(private val repository: ProfileRepository) : BaseViewModel() {


    //Variable to hold User data retrieved from FireStore
    private val _mFavProduct = MutableLiveData<Event<State<List<ProductDisplayModel>>>>()
    val mFavProduct: LiveData<Event<State<List<ProductDisplayModel>>>>
        get() = _mFavProduct

    private val _mDisplayFavProduct = MutableLiveData<Event<List<ProductDisplayModel>>>()
    val mDisplayFavProduct: LiveData<Event<List<ProductDisplayModel>>>
        get() = _mDisplayFavProduct

    //Variable to hold sold data retrieved from FireStore
    private val _mSoldProduct = MutableLiveData<Event<State<List<ItemDisplayDomainModel>>>>()
    val mSoldProduct: LiveData<Event<State<List<ItemDisplayDomainModel>>>>
        get() = _mSoldProduct

    private val _mDisplaySoldProduct = MutableLiveData<Event<List<ItemDisplayDomainModel>>>()
    val mDisplaySoldProduct: LiveData<Event<List<ItemDisplayDomainModel>>>
        get() = _mDisplaySoldProduct

    var mAllFavProductList = listOf<ProductDisplayModel>()
        private set

    fun setAllFavProductList(list : List<ProductDisplayModel>){
        mAllFavProductList = list
    }

    fun setDisplayFavProducts(list : List<ProductDisplayModel>){
        _mDisplayFavProduct.value = Event(list)
    }

    var mAllSoldProductList = listOf<ItemDisplayDomainModel>()
        private set

    fun setAllSoldProductList(list : List<ItemDisplayDomainModel>){
        mAllSoldProductList = list
    }

    fun setDisplaySoldProducts(list : List<ItemDisplayDomainModel>){
        _mDisplaySoldProduct.value = Event(list)
    }

    fun fetchFollowingData(collectionId: String) {
        viewModelScope.launch {
            repository.fetchFavProductsList(collectionId).collect {
                _mFavProduct.value = Event(it)
            }
        }
    }

    fun fetchSoldData(collectionId: String) {
        _mSoldProduct.value = Event(State.loading())
        viewModelScope.launch {
            repository.fetchSoldProductsList(collectionId).collect {
                try{
                    _mSoldProduct.value = Event(it)
                }
                catch(e : Exception){
                    GthrLogger.i("dcbhjd","derror : ${e.message}")
                }

            }
        }
    }
}
package com.gthr.gthrcollect.ui.productdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.utils.enums.ProductType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductDetailsViewModel(private val repository : ProductDetailsRepository) : ViewModel() {

    private val _mRecentSaleList = MutableLiveData<Event<State<List<RecentSaleDomainModel>>>>()
    val mRecentSaleList: LiveData<Event<State<List<RecentSaleDomainModel>>>>
        get() = _mRecentSaleList

    private val _mMtgProductDetails = MutableLiveData<Event<State<MTGDomainModel>>>()
    val mMtgProductDetails: LiveData<Event<State<MTGDomainModel>>>
        get() = _mMtgProductDetails

    private val _mPokemonProductDetails = MutableLiveData<Event<State<PokemonDomainModel>>>()
    val mPokemonProductDetails: LiveData<Event<State<PokemonDomainModel>>>
        get() = _mPokemonProductDetails

    private val _mSealedProductDetails = MutableLiveData<Event<State<SealedDomainModel>>>()
    val mSealedProductDetails: LiveData<Event<State<SealedDomainModel>>>
        get() = _mSealedProductDetails

    private val _mFunkoProductDetails = MutableLiveData<Event<State<FunkoDomainModel>>>()
    val mFunkoProductDetails: LiveData<Event<State<FunkoDomainModel>>>
        get() = _mFunkoProductDetails

    private val _mYugiohProductDetails = MutableLiveData<Event<State<YugiohDomainModel>>>()
    val mYugiohProductDetails: LiveData<Event<State<YugiohDomainModel>>>
        get() = _mYugiohProductDetails

    private val _mRelatedProductList = MutableLiveData<Event<State<List<ProductDisplayModel>>>>()
    val mRelatedProductList: LiveData<Event<State<List<ProductDisplayModel>>>>
        get() = _mRelatedProductList

    fun getProductDetails(id : String,type : ProductType) {
        viewModelScope.launch {
            when(type){
                ProductType.MAGIC_THE_GATHERING -> repository.getProductDetails<MTGDomainModel>(id,ProductType.MAGIC_THE_GATHERING).collect {
                    _mMtgProductDetails.value = Event(it)
                }
                ProductType.YUGIOH -> repository.getProductDetails<YugiohDomainModel>(id,ProductType.YUGIOH).collect {
                    _mYugiohProductDetails.value = Event(it)
                }
                ProductType.POKEMON -> repository.getProductDetails<PokemonDomainModel>(id,ProductType.POKEMON).collect {
                    _mPokemonProductDetails.value = Event(it)
                }
                ProductType.FUNKO -> repository.getProductDetails<FunkoDomainModel>(id,ProductType.FUNKO).collect {
                    _mFunkoProductDetails.value = Event(it)
                }
                ProductType.SEALED_POKEMON -> repository.getProductDetails<SealedDomainModel>(id,ProductType.SEALED_POKEMON).collect {
                    _mSealedProductDetails.value = Event(it)
                }
                ProductType.SEALED_YUGIOH -> repository.getProductDetails<SealedDomainModel>(id,ProductType.SEALED_YUGIOH).collect {
                    _mSealedProductDetails.value = Event(it)
                }
                ProductType.SEALED_MTG -> repository.getProductDetails<SealedDomainModel>(id,ProductType.SEALED_MTG).collect {
                    _mSealedProductDetails.value = Event(it)
                }
            }
        }
    }

    fun getRecentSaleList(objectId : String) {
        viewModelScope.launch {
            repository.getRecentSellList(objectId).collect{
                _mRecentSaleList.value = Event(it)
            }
        }
    }

    fun getRelatedProductList(value : String, type: ProductType){
        viewModelScope.launch {
            repository.getRelatedProductList(value,type).collect {
                _mRelatedProductList.value = Event(it)
            }
        }
    }

}
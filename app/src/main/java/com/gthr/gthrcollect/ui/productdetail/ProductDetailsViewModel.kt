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


    private val _mtgProductDetails = MutableLiveData<Event<State<MTGDomainModel>>>()
    val mtgProductDetails: LiveData<Event<State<MTGDomainModel>>>
        get() = _mtgProductDetails

    private val _pokemonProductDetails = MutableLiveData<Event<State<PokemonDomainModel>>>()
    val pokemonProductDetails: LiveData<Event<State<PokemonDomainModel>>>
        get() = _pokemonProductDetails

    private val _sealedProductDetails = MutableLiveData<Event<State<SealedDomainModel>>>()
    val sealedProductDetails: LiveData<Event<State<SealedDomainModel>>>
        get() = _sealedProductDetails

    private val _funkoProductDetails = MutableLiveData<Event<State<FunkoDomainModel>>>()
    val funkoProductDetails: LiveData<Event<State<FunkoDomainModel>>>
        get() = _funkoProductDetails

    private val _yugiohProductDetails = MutableLiveData<Event<State<YugiohDomainModel>>>()
    val yugiohProductDetails: LiveData<Event<State<YugiohDomainModel>>>
        get() = _yugiohProductDetails

    fun <T> getMtgProductDetails(id : String,type : ProductType) = viewModelScope.launch {
        repository.getProductDetails<T>(id,type).collect {
            _mtgProductDetails.value = Event(it as State<MTGDomainModel>)
        }
    }

    fun <T> getPokemonProductDetails(id : String,type : ProductType) = viewModelScope.launch {
        repository.getProductDetails<T>(id,type).collect {
            _pokemonProductDetails.value = Event(it as State<PokemonDomainModel>)
        }
    }

    fun <T> getSealedProductDetails(id : String,type : ProductType) = viewModelScope.launch {
        repository.getProductDetails<T>(id,type).collect {
            _sealedProductDetails.value = Event(it as State<SealedDomainModel>)
        }
    }

    fun <T> getFunkoProductDetails(id : String,type : ProductType) = viewModelScope.launch {
        repository.getProductDetails<T>(id,type).collect {
            _funkoProductDetails.value = Event(it as State<FunkoDomainModel>)
        }
    }

    fun <T> getYugiohProductDetails(id : String,type : ProductType) = viewModelScope.launch {
        repository.getProductDetails<T>(id,type).collect {
            _yugiohProductDetails.value = Event(it as State<YugiohDomainModel>)
        }
    }
}
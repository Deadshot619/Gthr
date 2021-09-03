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

    private val _productImage = MutableLiveData<Event<State<String>>>()
    val productImage: LiveData<Event<State<String>>>
        get() = _productImage

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

    fun getProductDetails(id : String,type : ProductType) {
        viewModelScope.launch {
            when(type){
                ProductType.MAGIC_THE_GATHERING -> repository.getProductDetails<MTGDomainModel>(id,ProductType.MAGIC_THE_GATHERING).collect {
                    _mtgProductDetails.value = Event(it)
                }
                ProductType.YUGIOH -> repository.getProductDetails<YugiohDomainModel>(id,ProductType.YUGIOH).collect {
                    _yugiohProductDetails.value = Event(it)
                }
                ProductType.POKEMON -> repository.getProductDetails<PokemonDomainModel>(id,ProductType.POKEMON).collect {
                    _pokemonProductDetails.value = Event(it)
                }
                ProductType.FUNKO -> repository.getProductDetails<FunkoDomainModel>(id,ProductType.FUNKO).collect {
                    _funkoProductDetails.value = Event(it)
                }
                ProductType.SEALED_POKEMON -> repository.getProductDetails<SealedDomainModel>(id,ProductType.SEALED_POKEMON).collect {
                    _sealedProductDetails.value = Event(it)
                }
                ProductType.SEALED_YUGIOH -> repository.getProductDetails<SealedDomainModel>(id,ProductType.SEALED_YUGIOH).collect {
                    _sealedProductDetails.value = Event(it)
                }
                ProductType.SEALED_MTG -> repository.getProductDetails<SealedDomainModel>(id,ProductType.SEALED_MTG).collect {
                    _sealedProductDetails.value = Event(it)
                }
            }
        }
    }

    fun getProductImage(image : String) {
        viewModelScope.launch {
            repository.getProductImage(image).collect{
                _productImage.value = Event(it)
            }
        }
    }
}
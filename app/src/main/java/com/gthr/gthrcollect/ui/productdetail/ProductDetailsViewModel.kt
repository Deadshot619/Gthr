package com.gthr.gthrcollect.ui.productdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.utils.enums.ProductType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductDetailsViewModel(private val mProductDetailsRepository : ProductDetailsRepository,private val mDynamicLinkRepository : DynamicLinkRepository) : ViewModel() {

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

    private val _mProductDisplayModel = MutableLiveData<Event<ProductDisplayModel>>()
    val mProductDisplayModel: LiveData<Event<ProductDisplayModel>>
        get() = _mProductDisplayModel

    private val _mProductDynamicLink = MutableLiveData<Event<State<String>>>()
    val mProductDynamicLink: LiveData<Event<State<String>>>
        get() = _mProductDynamicLink

    fun getProductDetails(objectId : String, type : ProductType) {
        viewModelScope.launch {
            when(type){
                ProductType.MAGIC_THE_GATHERING -> mProductDetailsRepository.getProductDetailsByObjectId<MTGDomainModel>(objectId,ProductType.MAGIC_THE_GATHERING).collect {
                    _mMtgProductDetails.value = Event(it)
                }
                ProductType.YUGIOH -> mProductDetailsRepository.getProductDetailsByObjectId<YugiohDomainModel>(objectId,ProductType.YUGIOH).collect {
                    _mYugiohProductDetails.value = Event(it)
                }
                ProductType.POKEMON -> mProductDetailsRepository.getProductDetailsByObjectId<PokemonDomainModel>(objectId,ProductType.POKEMON).collect {
                    _mPokemonProductDetails.value = Event(it)
                }
                ProductType.FUNKO -> mProductDetailsRepository.getProductDetailsByObjectId<FunkoDomainModel>(objectId,ProductType.FUNKO).collect {
                    _mFunkoProductDetails.value = Event(it)
                }
                ProductType.SEALED_POKEMON -> mProductDetailsRepository.getProductDetailsByObjectId<SealedDomainModel>(objectId,ProductType.SEALED_POKEMON).collect {
                    _mSealedProductDetails.value = Event(it)
                }
                ProductType.SEALED_YUGIOH -> mProductDetailsRepository.getProductDetailsByObjectId<SealedDomainModel>(objectId,ProductType.SEALED_YUGIOH).collect {
                    _mSealedProductDetails.value = Event(it)
                }
                ProductType.SEALED_MTG -> mProductDetailsRepository.getProductDetailsByObjectId<SealedDomainModel>(objectId,ProductType.SEALED_MTG).collect {
                    _mSealedProductDetails.value = Event(it)
                }
            }
        }
    }

    fun getRecentSaleList(objectId : String) {
        viewModelScope.launch {
            mProductDetailsRepository.getRecentSellList(objectId).collect{
                _mRecentSaleList.value = Event(it)
            }
        }
    }

    fun getRelatedProductList(value : String, type: ProductType){
        viewModelScope.launch {
            mProductDetailsRepository.getRelatedProductList(value,type).collect {
                _mRelatedProductList.value = Event(it)
            }
        }
    }

    fun getProductDynamicLink(value : String, type: ProductType){
        viewModelScope.launch {
            mDynamicLinkRepository.getProductDynamicLink(value,type).collect {
                _mProductDynamicLink.value = Event(it)
            }
        }
    }

    fun setProductDisplayModel(productDisplayModel : ProductDisplayModel){
        _mProductDisplayModel.value = Event(productDisplayModel)
    }

}
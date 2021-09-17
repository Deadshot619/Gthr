package com.gthr.gthrcollect.ui.productdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.data.repository.SearchRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.utils.enums.ProductType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductDetailsViewModel(private val mProductDetailsRepository : ProductDetailsRepository,private val mDynamicLinkRepository : DynamicLinkRepository,private val repositorySearch: SearchRepository) : ViewModel() {

    private var searchAskJob: Job? = null


    var favoriteJob: Job? = null

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

    private val _upForSaleList = MutableLiveData<Event<State<List<ProductDisplayModel>>>>()
    val upForSaleList: LiveData<Event<State<List<ProductDisplayModel>>>>
        get() = _upForSaleList




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
            mDynamicLinkRepository.getProductDynamicLink(value, type).collect {
                _mProductDynamicLink.value = Event(it)
            }
        }
    }

    fun setProductDisplayModel(productDisplayModel: ProductDisplayModel) {
        _mProductDisplayModel.value = Event(productDisplayModel)
    }

    fun fetchUpForSale(searchTerm: String? = null, limit: Int? = null, page: Int? = null, sortBy:String?=null,isAscending:Int?, productCategory: String? = null,productType: String? = null, objectId: String? = null) {
        searchAskJob = viewModelScope.launch {
            repositorySearch.fetchSearchAsk(searchTerm, limit, page,sortBy,isAscending,productCategory,productType,objectId).collect {
                _upForSaleList.value = Event(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchAskJob?.cancel()

    }

    private val _isFavorite = MutableLiveData<Event<State<Boolean>>>()
    val isFavorite: LiveData<Event<State<Boolean>>>
        get() = _isFavorite

    private val _addFavorites = MutableLiveData<Event<State<Boolean>>>()
    val addFavorites: LiveData<Event<State<Boolean>>>
        get() = _addFavorites

    private val _removeFavorites = MutableLiveData<Event<State<Boolean>>>()
    val removeFavorites: LiveData<Event<State<Boolean>>>
        get() = _removeFavorites

    //Method to check if a product is already favorite
    fun checkProductFavorite(productType: ProductType, objectId: String) {
        favoriteJob?.cancel()
        favoriteJob = viewModelScope.launch {
            mProductDetailsRepository.checkIfProductFavorite(productType, objectId).collect {
                _isFavorite.value = Event(it)
            }
        }
    }

    fun addFavorites(productType: ProductType, objectId: String) {
        favoriteJob?.cancel()
        favoriteJob = viewModelScope.launch {
            mProductDetailsRepository.addProductToFavorites(productType, objectId).collect {
                _addFavorites.value = Event(it)
            }
        }
    }

    fun removeFavorites(productType: ProductType, objectId: String) {
        favoriteJob?.cancel()
        favoriteJob = viewModelScope.launch {
            mProductDetailsRepository.removeProductFavorite(productType, objectId).collect {
                _removeFavorites.value = Event(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        favoriteJob?.cancel()
    }
}
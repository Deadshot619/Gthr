package com.gthr.gthrcollect.ui.askflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.utils.enums.ConditionType
import com.gthr.gthrcollect.utils.enums.EditionType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.isValidPrice
import com.gthr.gthrcollect.utils.extensions.toTwoDecimal
import com.gthr.gthrcollect.utils.helper.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AskFlowViewModel(private val repository: AskFlowRepository) : BaseViewModel() {

    var shippingTierJob: Job? = null

    var productType: ProductType? = null
        private set

    var productDisplayModel: ProductDisplayModel? = null
        private set

    private val _isSell = MutableLiveData<Boolean>()
    val isSell: LiveData<Boolean>
        get() = _isSell

    private val _askPrice = MutableLiveData<Float>()
    val askPrice: LiveData<Float>
        get() = _askPrice

    private val _buyListPrice = MutableLiveData<Float>()
    val buyListPrice: LiveData<Float>
        get() = _buyListPrice

    private val _shippingTierInfo = MutableLiveData<Event<State<ShippingInfoDomainModel>>>()
    val shippingTierInfo: LiveData<Event<State<ShippingInfoDomainModel>>>
        get() = _shippingTierInfo

    val totalRate: Float
        get() = addRates(
            //Price
            askPrice.value?.toFloat(),
            //Shipping Price
            if (shippingTierInfo.value?.peekContent() == null)
                -0f
            else
                -((shippingTierInfo.value?.peekContent() as State.Success).data.frontEndShippingProcessing.toFloatOrNull()
                    ?: 0f),
            sellingFee,
            paymentProcessing
        )

    val sellingFee: Float
        get() = (((askPrice.value?.toFloat() ?: 0f) * PERCENT_SELLING_FEE) / 100).toTwoDecimal()

    val paymentProcessing: Float
        get() = (((askPrice.value?.toFloat()
            ?: 0f) * PERCENT_PAYMENT_PROCESSING) / 100).toTwoDecimal()

    private fun addRates(vararg rate: Float?): Float {
        var total = 0f
        rate.forEach {
            if (it != null)
                total += it
        }
        return total
    }

    /* Selected Language, Edition, Condition Title & Value */
    private val _selectedLanguage = MutableLiveData<Event<LanguageDomainModel>>()
    val selectedLanguage: LiveData<Event<LanguageDomainModel>>
        get() = _selectedLanguage

    private val _selectedEdition = MutableLiveData<Event<EditionType>>()
    val selectedEdition: LiveData<Event<EditionType>>
        get() = _selectedEdition

    private val _selectedConditionTitle = MutableLiveData<Event<ConditionType>>()
    val selectedConditionTitle: LiveData<Event<ConditionType>>
        get() = _selectedConditionTitle

    private val _selectedConditionValue = MutableLiveData<Event<ConditionDomainModel>>()
    val selectedConditionValue: LiveData<Event<ConditionDomainModel>>
        get() = _selectedConditionValue

    /* Front & Back Image */
    private val _frontImageUrl = MutableLiveData<String>()
    val frontImageUrl: LiveData<String>
        get() = _frontImageUrl

    private val _backImageUrl = MutableLiveData<String>()
    val backImageUrl: LiveData<String>
        get() = _backImageUrl


    /** Language, Edition & Condition Lists for [ProductCategory.CARDS] */
    private val _languageList = MutableLiveData<Event<List<LanguageDomainModel>>>()
    val languageList: LiveData<Event<List<LanguageDomainModel>>>
        get() = _languageList

    private val _editionList = MutableLiveData<Event<List<EditionType>>>()
    val editionList: LiveData<Event<List<EditionType>>>
        get() = _editionList

    private val _conditionList = MutableLiveData<Event<List<ConditionDomainModel>>>()
    val conditionList: LiveData<Event<List<ConditionDomainModel>>>
        get() = _conditionList

    init {
        setSelectedConditionTitle(ConditionType.UG)  //Default selection UG i.e. Raw
    }

    fun setProductType(productType: ProductType) {
        this.productType = productType
    }

    fun setProductDisplayModel(productDisplayModel: ProductDisplayModel) {
        this.productDisplayModel = productDisplayModel
    }

    fun setSell(isSell: Boolean) {
        _isSell.value = isSell
    }

    fun setFrontImage(bitmapUrl: String) {
        _frontImageUrl.value = bitmapUrl
    }

    fun setBackImage(bitmapUrl: String) {
        _backImageUrl.value = bitmapUrl
    }

    fun setAskPrice(price: Float) {
        _askPrice.value = price.toString().isValidPrice().toFloatOrNull() ?: 0f
    }

    fun setBuylistPrice(price: Float) {
        _buyListPrice.value = price.toString().isValidPrice().toFloatOrNull() ?: 0f
    }

    fun setSelectedLanguage(languageDomainModel: LanguageDomainModel) {
        _selectedLanguage.value = Event(languageDomainModel)
        retrieveEditionList(productType!!)
    }

    fun setSelectedEdition(editionType: EditionType) {
        _selectedEdition.value = Event(editionType)
        retrieveConditionList(ConditionType.UG)
    }

    fun setSelectedConditionTitle(conditionType: ConditionType) {
        _selectedConditionTitle.value = Event(conditionType)
        retrieveConditionList(conditionType)
    }

    fun setSelectedConditionValue(conditionDomainModel: ConditionDomainModel) {
        _selectedConditionValue.value = Event(conditionDomainModel)
    }

    /* Product Types */
    private val _mtgProductDetails = MutableLiveData<Event<State<MTGDomainModel>>>()
    val mtgProductDetails: LiveData<Event<State<MTGDomainModel>>>
        get() = _mtgProductDetails

    private val _pokemonProductDetails = MutableLiveData<Event<State<PokemonDomainModel>>>()
    val pokemonProductDetails: LiveData<Event<State<PokemonDomainModel>>>
        get() = _pokemonProductDetails

    private val _yugiohProductDetails = MutableLiveData<Event<State<YugiohDomainModel>>>()
    val yugiohProductDetails: LiveData<Event<State<YugiohDomainModel>>>
        get() = _yugiohProductDetails

    private val _sealedProductDetails = MutableLiveData<Event<State<SealedDomainModel>>>()
    val sealedProductDetails: LiveData<Event<State<SealedDomainModel>>>
        get() = _sealedProductDetails

    private val _funkoProductDetails = MutableLiveData<Event<State<FunkoDomainModel>>>()
    val funkoProductDetails: LiveData<Event<State<FunkoDomainModel>>>
        get() = _funkoProductDetails

    fun getProductDetails(id: String, type: ProductType) {
        viewModelScope.launch {
            when (type) {
                ProductType.MAGIC_THE_GATHERING -> repository.getProductDetails<MTGDomainModel>(
                    id,
                    ProductType.MAGIC_THE_GATHERING
                ).collect {
                    _mtgProductDetails.value = Event(it)
                }
                ProductType.YUGIOH -> repository.getProductDetails<YugiohDomainModel>(
                    id,
                    ProductType.YUGIOH
                ).collect {
                    _yugiohProductDetails.value = Event(it)
                }
                ProductType.POKEMON -> repository.getProductDetails<PokemonDomainModel>(
                    id,
                    ProductType.POKEMON
                ).collect {
                    _pokemonProductDetails.value = Event(it)
                }
                ProductType.FUNKO -> repository.getProductDetails<FunkoDomainModel>(
                    id,
                    ProductType.FUNKO
                ).collect {
                    _funkoProductDetails.value = Event(it)
                }
                ProductType.SEALED_POKEMON -> repository.getProductDetails<SealedDomainModel>(
                    id,
                    ProductType.SEALED_POKEMON
                ).collect {
                    _sealedProductDetails.value = Event(it)
                }
                ProductType.SEALED_YUGIOH -> repository.getProductDetails<SealedDomainModel>(
                    id,
                    ProductType.SEALED_YUGIOH
                ).collect {
                    _sealedProductDetails.value = Event(it)
                }
                ProductType.SEALED_MTG -> repository.getProductDetails<SealedDomainModel>(
                    id,
                    ProductType.SEALED_MTG
                ).collect {
                    _sealedProductDetails.value = Event(it)
                }
            }
        }
    }

    fun getShippingTierInfo(tier: String) {
        shippingTierJob?.cancel()
        shippingTierJob = viewModelScope.launch {
            repository.getShippingTierInfo(tier).collect {
                _shippingTierInfo.value = Event(it)
            }
        }
    }

    fun retrieveLanguageList(type: ProductType) {
        viewModelScope.launch {
            _languageList.value = when (type) {
                ProductType.POKEMON -> Event(getPokemonLangList((pokemonProductDetails.value!!.peekContent() as State.Success).data))
                ProductType.YUGIOH -> Event(getYugiohLangList((yugiohProductDetails.value!!.peekContent() as State.Success).data))
                ProductType.MAGIC_THE_GATHERING -> Event(getMTGLangList())
                else -> Event(listOf())
            }
        }
    }

    fun retrieveEditionList(type: ProductType) {
        viewModelScope.launch {
            _editionList.value = when (type) {
                ProductType.POKEMON ->
                    Event(
                        getPokemonEditionList(
                            getPokemonEditionKey(
                                selectedLanguage.value!!.peekContent().key,
                                (pokemonProductDetails.value!!.peekContent() as State.Success).data
                            )
                        )
                    )
                ProductType.YUGIOH ->
                    Event(
                        getYugiohEditionList(
                            getYugiohEditionKey(
                                selectedLanguage.value!!.peekContent().key,
                                (yugiohProductDetails.value!!.peekContent() as State.Success).data
                            )
                        )
                    )
                ProductType.MAGIC_THE_GATHERING ->
                    Event(getMTGEditionList())
                else -> Event(listOf())
            }
        }
    }

    fun retrieveConditionList(type: ConditionType) {
        viewModelScope.launch {
            _conditionList.value = Event(getConditionList(type))
        }
    }

    override fun onCleared() {
        super.onCleared()
        shippingTierJob?.cancel()
    }

    companion object {
        private const val PERCENT_SELLING_FEE = 8.5f
        private const val PERCENT_PAYMENT_PROCESSING = 2.9f
    }
}
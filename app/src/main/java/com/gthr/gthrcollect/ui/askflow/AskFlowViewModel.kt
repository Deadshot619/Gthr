package com.gthr.gthrcollect.ui.askflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.*
import com.gthr.gthrcollect.model.mapper.toRealtimeDatabaseModel
import com.gthr.gthrcollect.model.network.cloudfunction.StripePaymentModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.utils.constants.FirebaseStorage
import com.gthr.gthrcollect.utils.enums.ConditionType
import com.gthr.gthrcollect.utils.enums.EditionType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.getUserCollectionId
import com.gthr.gthrcollect.utils.extensions.getUserUID
import com.gthr.gthrcollect.utils.extensions.isValidPrice
import com.gthr.gthrcollect.utils.extensions.toTwoDecimal
import com.gthr.gthrcollect.utils.getProductCategory
import com.gthr.gthrcollect.utils.helper.*
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AskFlowViewModel(private val repository: AskFlowRepository) : BaseViewModel() {

    var shippingTierJob: Job? = null
    var stripeJob: Job? = null

    var productType: ProductType? = null
        private set

    var productDisplayModel: ProductDisplayModel? = null
        private set

    //Variable to indicate whether this AskFlow is for editing or not
    var isEdit: Boolean = false
        private set

    fun setIsEdit(value: Boolean) {
        isEdit = value
    }

    private val _isSell = MutableLiveData<Boolean>()
    val isSell: LiveData<Boolean>
        get() = _isSell

    private val _askPrice = MutableLiveData<Double>()
    val askPrice: LiveData<Double>
        get() = _askPrice

    private val _buyListPrice = MutableLiveData<Double>()
    val buyListPrice: LiveData<Double>
        get() = _buyListPrice

    private val _tierForBuyDirectly = MutableLiveData<Event<State<String?>>>()
    val tierForBuyDirectly: LiveData<Event<State<String?>>>
        get() = _tierForBuyDirectly

    private val _shippingTierInfo = MutableLiveData<Event<State<ShippingInfoDomainModel>>>()
    val shippingTierInfo: LiveData<Event<State<ShippingInfoDomainModel>>>
        get() = _shippingTierInfo

    val totalPayoutRate: Double //Used in AskFlow
        get() = addRates(
            //Price
            askPrice.value?.toDouble(),
            //Shipping Price
            +shippingProcessing,
            -sellingFee,
            -paymentProcessing
        )

    val totalPaymentRate: Double    //Used in Buy Directly from someone
        get() = addRates(
            mBuyingDirFromSomeOneProPrice.value?.toDouble(),
            shippingProcessing,
            salesTax
        )

    val shippingProcessing: Double
        get() = if (shippingTierInfo.value?.peekContent() == null)
            0.00
        else
            ((shippingTierInfo.value?.peekContent() as State.Success).data.frontEndShippingProcessing.toDoubleOrNull()
                ?: 0.00)

    val sellingFee: Double
        get() = askPrice.value.getSellingFee()

    val paymentProcessing: Double
        get() = askPrice.value.getPaymentProcessing()

    val salesTax: Double = SALES_TAX_VALUE

    private fun addRates(vararg rate: Double?): Double {
        var total = 0.00
        rate.forEach {
            if (it != null)
                total += it
        }
        return total.toTwoDecimal()
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

    private val _selectedCondition = MutableLiveData<Event<ConditionDomainModel>>()
    val selectedCondition: LiveData<Event<ConditionDomainModel>>
        get() = _selectedCondition

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
        //setSelectedConditionTitle(ConditionType.UG)  //Default selection UG i.e. Raw
        val addressList = GthrCollect.prefs?.userInfoModel?.addressList
        addressList?.let {
            for (index in addressList.indices){
                val address= addressList[index]
                address.addresKey=index
                if (address.isSelected) {
                    mAddress = address
                    break
                }
            }
        }

        checkUserStripeAccId(GthrCollect.prefs?.getUserUID())
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

    fun setAskPrice(price: Double) {
        _askPrice.value = price.toString().isValidPrice().toDoubleOrNull() ?: 0.00
    }

    fun setBuylistPrice(price: Double) {
        _buyListPrice.value = price.toString().isValidPrice().toDoubleOrNull() ?: 0.00
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
        _selectedCondition.value = Event(conditionDomainModel)
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

    fun getTierByRef(productType: ProductType, refKey: String) {
        viewModelScope.launch {
            repository.fetchTier(productType, refKey).collect {
                _tierForBuyDirectly.value = Event(it)
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

    fun setStaticShippingInfo() {
        _shippingTierInfo.value = Event(
            State.success(
                ShippingInfoDomainModel(
                    billing = 0,
                    frontEndShippingProcessing = "0.5",
                    service = "",
                    refKey = null
                )
            )
        )
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


    var mFrontImageDownloadUrl = ""
        private set

    var mBackImageDownloadUrl = ""
        private set

    //Collection Item key use to store image in firebase Storage in collectionImage folder
    var mCollectionItemKey = ""
        private set

    var mAskId = ""
        private set

    var mAddress: ShippingAddressDomainModel? = null
        private set

    var mIsPayoutAuth: Boolean = false
        private set

    var mBidId = ""
        private set

    var mBuyKey = ""
        private set

    fun setBidId(id: String) {
        mBidId = id
    }

    fun setBuyKey(key: String) {
        mBuyKey = key
    }

    fun setFrontImageDownloadUrl(url: String) {
        mFrontImageDownloadUrl = url
    }

    fun setBackImageDownloadUrl(url: String) {
        mBackImageDownloadUrl = url
    }

    fun setCollectionItemKey(key: String) {
        mCollectionItemKey = key
    }

    fun setAskId(key: String) {
        mAskId = key
    }

    fun setAddress(address: ShippingAddressDomainModel) {
        mAddress = address
    }

    fun setPayoutAuth(auth: Boolean) {
        mIsPayoutAuth = auth
    }


    //Variable to indicate whether Collection data has been added in Firebase
    private val _insertCollectionRDB = MutableLiveData<Event<State<String>>>()
    val insertCollectionRDB: LiveData<Event<State<String>>>
        get() = _insertCollectionRDB

    fun insertCollection() {
        viewModelScope.launch {
            val data: CollectionItemDomainModel? = when (productType) {
                ProductType.MAGIC_THE_GATHERING, ProductType.YUGIOH, ProductType.POKEMON -> {
                    CollectionItemDomainModel(
                        marketCost = 0.0,
                        productType = productType,
                        edition = selectedEdition.value?.peekContent(),
                        frontImageURL = null,
                        backImageURL = null,
                        askRefKey = null,
                        id = null,
                        itemRefKey = productDisplayModel?.refKey,
                        language = selectedLanguage.value?.peekContent(),
                        condition = selectedCondition.value?.peekContent(),
                        objectID = productDisplayModel?.objectID
                    )
                }
                ProductType.FUNKO, ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> {
                    CollectionItemDomainModel(
                        marketCost = 0.0,
                        productType = productType,
                        edition = null, frontImageURL = null,
                        backImageURL = null, askRefKey = null,
                        id = null, itemRefKey = productDisplayModel?.refKey, language = null,
                        condition = null,
                        objectID = productDisplayModel?.objectID
                    )
                }
                null -> null
            }
            repository.insertCollection(
                GthrCollect.prefs?.getUserCollectionId()!!,
                data?.toRealtimeDatabaseModel()!!
            ).collect {
                _insertCollectionRDB.value = Event(it)
            }
        }
    }

    //Variable to indicate whether Collection data has been updated in Firebase
    private val _updateCollectionRDB = MutableLiveData<Event<State<Boolean>>>()
    val updateCollectionRDB: LiveData<Event<State<Boolean>>>
        get() = _updateCollectionRDB

    fun updateCollection() {
        viewModelScope.launch {
            repository.updateCollection(
                GthrCollect.prefs?.getUserCollectionId()!!,
                mCollectionItemKey,
                mFrontImageDownloadUrl,
                mBackImageDownloadUrl,
                mAskId
            ).collect {
                _updateCollectionRDB.value = Event(it)
            }
        }
    }

    //Variable to indicate whether user front Id image uploaded
    private val _frontImageUpload = MutableLiveData<Event<State<String>>>()
    val frontImageUpload: LiveData<Event<State<String>>>
        get() = _frontImageUpload

    fun uploadFrontImage() {
        viewModelScope.launch {
            repository.uploadCollectionImage(
                frontImageUrl.value!!,
                mCollectionItemKey,
                FirebaseStorage.FRONT_IMAGE,
                GthrCollect.prefs!!.signedInUser!!.uid
            ).collect {
                _frontImageUpload.value = Event(it)
            }
        }
    }

    //Variable to indicate whether user back Id image uploaded
    private val _backImageUpload = MutableLiveData<Event<State<String>>>()
    val backImageUpload: LiveData<Event<State<String>>>
        get() = _backImageUpload

    fun uploadBackImage() {
        viewModelScope.launch {
            repository.uploadCollectionImage(
                backImageUrl.value!!,
                mCollectionItemKey,
                FirebaseStorage.BACK_IMAGE,
                GthrCollect.prefs!!.signedInUser!!.uid
            ).collect {
                _backImageUpload.value = Event(it)
            }
        }
    }

    //Variable to indicate whether Ask data has been added in Firebase
    private val _insertAskRDB = MutableLiveData<Event<State<String>>>()
    val insertAskRDB: LiveData<Event<State<String>>>
        get() = _insertAskRDB

    fun insertAsk() {
        viewModelScope.launch {
            val data = if (isEdit) {
                AskItemDomainModel(
                    refKey = "",
                    duration = "",
                    itemRefKey = productDisplayModel?.forsaleItemNodel?.productFirebaseRef!!,
                    creatorUID = GthrCollect.prefs?.signedInUser?.uid!!,
                    askPrice = askPrice.value.toString(),
                    totalPayout = totalPayoutRate.toString(),
                    itemObjectID = productDisplayModel?.objectID!!,
                    productType = productType,
                    productCategory = getProductCategory(productType!!),
                    edition = getEditionTypeFromRowType(productDisplayModel?.forsaleItemNodel?.edition.toString()),
                    condition = productDisplayModel?.forsaleItemNodel?.condition,
                    language = productDisplayModel?.forsaleItemNodel?.language,
                    returnName = mAddress?.firstName,
                    returnAddressLine1 = mAddress?.addressLine1,
                    returnAddressLine2 = mAddress?.addressLine2,
                    returnCity = mAddress?.city,
                    returnState = mAddress?.state,
                    returnZipCode = mAddress?.postalCode,
                    returnCountry = mAddress?.country,
                    frontImageURL = mFrontImageDownloadUrl,
                    backImageURL = mBackImageDownloadUrl,
                    addresskey = mAddress?.addresKey
                )
            } else
                when (productType) {
                    ProductType.MAGIC_THE_GATHERING, ProductType.YUGIOH, ProductType.POKEMON -> {
                        AskItemDomainModel(
                            refKey = "",
                            duration = "",
                            itemRefKey = productDisplayModel?.refKey!!,
                            creatorUID = GthrCollect.prefs?.signedInUser?.uid!!,
                            askPrice = askPrice.value.toString(),
                            totalPayout = totalPayoutRate.toString(),
                            itemObjectID = productDisplayModel?.objectID!!,
                            productType = productType,
                            productCategory = getProductCategory(productType!!),
                            edition = selectedEdition.value?.peekContent(),
                            condition = selectedCondition.value?.peekContent(),
                            language = selectedLanguage.value?.peekContent(),
                            returnName = mAddress?.firstName,
                            returnAddressLine1 = mAddress?.addressLine1,
                            returnAddressLine2 = mAddress?.addressLine2,
                            returnCity = mAddress?.city,
                            returnState = mAddress?.state,
                            returnZipCode = mAddress?.postalCode,
                            returnCountry = mAddress?.country,
                            frontImageURL = mFrontImageDownloadUrl,
                            backImageURL = mBackImageDownloadUrl,
                            addresskey = mAddress?.addresKey
                        )
                }
                ProductType.FUNKO, ProductType.SEALED_POKEMON, ProductType.SEALED_YUGIOH, ProductType.SEALED_MTG -> {
                    AskItemDomainModel(
                        refKey = "",
                        duration = "",
                        itemRefKey = productDisplayModel?.refKey!!,
                        creatorUID = GthrCollect.prefs?.signedInUser?.uid!!,
                        askPrice = askPrice.value.toString(),
                        totalPayout = totalPayoutRate.toString(),
                        itemObjectID = productDisplayModel?.objectID!!,
                        productType = productType,
                        productCategory = getProductCategory(productType!!),
                        edition = null,
                        condition = null,
                        language = null,
                        returnName = mAddress?.firstName,
                        returnAddressLine1 = mAddress?.addressLine1,
                        returnAddressLine2 = mAddress?.addressLine2,
                        returnCity = mAddress?.city,
                        returnState = mAddress?.state,
                        returnZipCode = mAddress?.postalCode,
                        returnCountry = mAddress?.country,
                        frontImageURL = mFrontImageDownloadUrl,
                        backImageURL = mBackImageDownloadUrl,
                        addresskey = mAddress?.addresKey
                    )
                }
                else -> null
            }

            repository.insertAsk(data?.toRealtimeDatabaseModel()!!).collect {
                _insertAskRDB.value = Event(it)
            }
        }
    }

    //Variable to indicate whether Product data has been updated in Firebase
    private val _updateProductForAskRDB = MutableLiveData<Event<State<Boolean>>>()
    val updateProductForAskRDB: LiveData<Event<State<Boolean>>>
        get() = _updateProductForAskRDB

    fun updateProductForAsk() {
        viewModelScope.launch {
            GthrLogger.i("shdbchjsdb", "productType:  $productType}")
            GthrLogger.i(
                "shdbchjsdb",
                "productDisplayModel?.refKey: ${productDisplayModel?.refKey}"
            )
            repository.updateProductForAsk(
                askPrice.value!!.toInt(),
                mAskId,
                productType!!,
                productDisplayModel?.refKey!!,
                productDisplayModel?.objectID!!
            ).collect {
                _updateProductForAskRDB.value = Event(it)
            }
        }
    }

    //==========Bid===========

    //Variable to indicate whether Bid data has been added in Firebase
    private val _insertBidRDB = MutableLiveData<Event<State<String>>>()
    val insertBidRDB: LiveData<Event<State<String>>>
        get() = _insertBidRDB

    fun insertBid() {
        viewModelScope.launch {
            val data = BidItemDomainModel(
                bidPrice = buyListPrice.value.toString(),
                creatorUID = GthrCollect.prefs?.signedInUser?.uid!!,
                itemObjectID = productDisplayModel?.objectID!!,
                productType = productType,
                productCategory = getProductCategory(productType!!),
                itemRefKey = productDisplayModel?.refKey,
                totalCost = buyListPrice.value.toString(),
                language = selectedLanguage.value?.peekContent(),
                condition = selectedCondition.value?.peekContent(),
                edition = selectedEdition.value?.peekContent()
            )
            repository.insertBid(data.toRealtimeDatabaseModel()).collect {
                _insertBidRDB.value = Event(it)
            }
        }
    }

    //Variable to indicate whether Buy data has been added in Firebase
    private val _insertBuyRDB = MutableLiveData<Event<State<String>>>()
    val insertBuyRDB: LiveData<Event<State<String>>>
        get() = _insertBuyRDB

    fun insertBuy() {
        viewModelScope.launch {
            repository.insertBuy(GthrCollect.prefs?.getUserCollectionId()!!, mBidId).collect {
                _insertBuyRDB.value = Event(it)
            }
        }
    }

    //Variable to indicate whether Product data has been updated in Firebase
    private val _updateProductForBidRDB = MutableLiveData<Event<State<Boolean>>>()
    val updateProductForBidRDB: LiveData<Event<State<Boolean>>>
        get() = _updateProductForBidRDB

    fun updateProductForBid() {
        viewModelScope.launch {
            GthrLogger.i("shdbchjsdb", "productType:  $productType}")
            GthrLogger.i(
                "shdbchjsdb",
                "productDisplayModel?.refKey: ${productDisplayModel?.refKey}"
            )
            repository.updateProductForBid(
                buyListPrice.value?.toInt()!!,
                mBidId,
                productType!!,
                productDisplayModel?.refKey!!,
                productDisplayModel?.objectID!!
            ).collect {
                _updateProductForBidRDB.value = Event(it)
            }
        }
    }

    //Variable to indicate whether Collection data has been updated in Firebase
    private val _stripeAccId = MutableLiveData<Event<State<Boolean>>>()
    val stripeAccId: LiveData<Event<State<Boolean>>>
        get() = _stripeAccId

    fun checkStripeAccId(userId: String? = null) {
        viewModelScope.launch {
            repository.authStripeAccount(userId).collect {
                _stripeAccId.value = Event(it)
            }
        }
    }

    //Check User Stripe Account id Status
    private val _stripeAccStatus = MutableLiveData<Event<State<Boolean>>>()
    val stripeAccStatus: LiveData<Event<State<Boolean>>>
        get() = _stripeAccStatus

    private fun checkUserStripeAccId(userId: String? = null) {
        stripeJob?.cancel()
        stripeJob = viewModelScope.launch {
            repository.authStripeAccount(userId).collect {
                _stripeAccStatus.value = Event(it)
            }
        }
    }

    //Variable to get Updated Payout Link of Stripe
    private val _payoutLink = MutableLiveData<Event<State<String>>>()
    val payoutLink: LiveData<Event<State<String>>>
        get() = _payoutLink

    fun getPayoutLink() {
        viewModelScope.launch {
            repository.getStripePayoutLink().collect {
                _payoutLink.value = Event(it)
            }
        }
    }

    //Variable to get Updated Payout Link of Stripe
    private val _paymentData = MutableLiveData<Event<State<StripePaymentModel>>>()
    val paymentData: LiveData<Event<State<StripePaymentModel>>>
        get() = _paymentData

    fun generateClientSecret(askKey: String, shippingTier :String) {
        viewModelScope.launch {
            repository.generateClientSecret(askKey,shippingTier).collect {
                _paymentData.value = Event(it)
            }
        }
    }

    //Variable to create Buy Now
    private val _buyNowData = MutableLiveData<Event<State<ReceiptDomainModel>>>()
    val buyNowData: LiveData<Event<State<ReceiptDomainModel>>>
        get() = _buyNowData

    fun createBuyNow( askId: String? = null,
                      buyerCharge: String? = null,
                      buyerAddressKey: String? = null,
                      sellerAddressKey: String? = null,
                      shippingTierKey: String? = null,
                      appFee: String? = null,
                      paymentId: String? = null,
                      sellerPayout: String? = null) {
        viewModelScope.launch {
            repository.buyNow(askId,buyerCharge,buyerAddressKey,sellerAddressKey,shippingTierKey,appFee,paymentId,sellerPayout).collect {
                _buyNowData.value = Event(it)
            }
        }
    }


    //Variable to indicate whether Collection data has been updated in Firebase
    private val _mDisplayName = MutableLiveData<Event<State<String>>>()
    val mDisplayName: LiveData<Event<State<String>>>
        get() = _mDisplayName

    fun getUserDisplayName(collectionID : String){
        viewModelScope.launch {
            repository.getUserDisplayName(collectionID).collect {
                _mDisplayName.value = Event(it)
            }
        }
    }

    //Variable to indicate whether user image has been updated in Firebase
    private val _mUserImage = MutableLiveData<Event<State<String>>>()
    val mUserImage: LiveData<Event<State<String>>>
        get() = _mUserImage

    fun getUserImage(collectionID : String){
        viewModelScope.launch {
            repository.getUserImage(collectionID).collect {
                _mUserImage.value = Event(it)
            }
        }
    }

    private val _mBuyingDirFromSomeOneProPrice = MutableLiveData<Double>()
    val mBuyingDirFromSomeOneProPrice: LiveData<Double>
        get() = _mBuyingDirFromSomeOneProPrice

    fun setBuyingDirFromSomeOneProPrice(price: Double) {
        _mBuyingDirFromSomeOneProPrice.value = price
    }

    private val _deleteAsk = MutableLiveData<Event<State<Boolean>>>()
    val deleteAsk: LiveData<Event<State<Boolean>>>
        get() = _deleteAsk

    private val _deleteCollection = MutableLiveData<Event<State<Boolean>>>()
    val deleteCollection: LiveData<Event<State<Boolean>>>
        get() = _deleteCollection

    private val _deleteBid = MutableLiveData<Event<State<Boolean>>>()
    val deleteBid: LiveData<Event<State<Boolean>>>
        get() = _deleteBid

    fun deleteAsk(askRefKey: String) {
        viewModelScope.launch {
            repository.deleteAsk(GthrCollect.prefs?.getUserCollectionId().toString(), askRefKey)
                .collect {
                    _deleteAsk.value = Event(it)
                }
        }
    }

    fun deleteCollection(askRefKey: String) {
        viewModelScope.launch {
            repository.deleteCollectionItem(
                GthrCollect.prefs?.getUserCollectionId().toString(),
                askRefKey
            )
                .collect {
                    _deleteCollection.value = Event(it)
                }
        }
    }

    fun deleteBid(bidRefKey: String) {
        viewModelScope.launch {
            repository.deleteBidItemModel(
                GthrCollect.prefs?.getUserCollectionId().toString(),
                bidRefKey
            ).collect {
                _deleteBid.value = Event(it)
            }
        }
    }

    private val _editAsk = MutableLiveData<Event<State<Boolean>>>()
    val editAsk: LiveData<Event<State<Boolean>>>
        get() = _editAsk

    private val _editBid = MutableLiveData<Event<State<Boolean>>>()
    val editBid: LiveData<Event<State<Boolean>>>
        get() = _editBid

    fun editAsk(productDisplayModel: ProductDisplayModel, price: Double) {
        viewModelScope.launch {
            repository.editAsk(
                productType = productDisplayModel.productType!!,
                objectID = productDisplayModel.forsaleItemNodel?.itemObjectID.toString(),
                askRefKey = productDisplayModel.forsaleItemNodel?.askRefKey.toString(),
                price = askPrice.value!!
            ).collect {
                _editAsk.value = Event(it)
            }
        }
    }

    fun editBid(productDisplayModel: ProductDisplayModel, price: Double) {
        viewModelScope.launch {
            repository.editBid(
                productType = productDisplayModel.productType!!,
                objectID = productDisplayModel.searchBidsDomainModel?.itemObjectID.toString(),
                bidRefKey = productDisplayModel.searchBidsDomainModel?.bidRefKey.toString(),
                price = price
            ).collect {
                _editBid.value = Event(it)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        shippingTierJob?.cancel()
        stripeJob?.cancel()
    }

    companion object {
        private const val SALES_TAX_VALUE = 0.0
    }
}
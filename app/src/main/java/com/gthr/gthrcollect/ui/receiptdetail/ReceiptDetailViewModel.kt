package com.gthr.gthrcollect.ui.receiptdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.algolia.search.model.ObjectID
import com.gthr.gthrcollect.data.repository.ReceiptRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.domain.ProductDisplayModel
import com.gthr.gthrcollect.model.domain.ReceiptDomainModel
import com.gthr.gthrcollect.model.domain.ShippingInfoDomainModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.utils.enums.ProductType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ReceiptDetailViewModel(private val repository: ReceiptRepository) : BaseViewModel() {

    var shippingTierJob: Job? = null
    var mProductDetailJob: Job? = null

    var receiptModel: ReceiptDomainModel? = null
        private set

    var productImageUrl: String? = null
        private set

    fun setReceiptModel(receiptDomainModel: ReceiptDomainModel) {
        receiptModel = receiptDomainModel
    }

    fun setProductImage(url: String) {
        productImageUrl = url
    }

    private val _productDetailModel = MutableLiveData<Event<State<ProductDisplayModel?>>>()
    val productDetailModel: LiveData<Event<State<ProductDisplayModel?>>>
        get() = _productDetailModel

    fun getProductDetailData(productType: ProductType, objectID: String) {
        mProductDetailJob?.cancel()
        mProductDetailJob = viewModelScope.launch {
            repository.fetchProductDetail(productType, objectID).collect {
                _productDetailModel.value = Event(it)
            }
        }
    }

    var collectionInfoDomainModel: CollectionInfoDomainModel? = null
        private set

    fun setCollectionInfoDomainModel(model: CollectionInfoDomainModel){
        collectionInfoDomainModel = model
    }

    private val _mCollectionInfoDomainModel = MutableLiveData<Event<State<CollectionInfoDomainModel>>>()
    val mCollectionInfoDomainModel: LiveData<Event<State<CollectionInfoDomainModel>>>
        get() = _mCollectionInfoDomainModel

    fun getCollectionInfo(userID : String){
        viewModelScope.launch {
            repository.getCollectionInfo(userID).collect {
                _mCollectionInfoDomainModel.value = Event(it)
            }
        }
    }

    private val _shippingTierInfo = MutableLiveData<Event<State<ShippingInfoDomainModel>>>()
    val shippingTierInfo: LiveData<Event<State<ShippingInfoDomainModel>>>
        get() = _shippingTierInfo

    fun getShippingTierInfo(tier: String) {
        shippingTierJob?.cancel()
        shippingTierJob = viewModelScope.launch {
            repository.getShippingTierInfo(tier).collect {
                _shippingTierInfo.value = Event(it)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        shippingTierJob?.cancel()
        mProductDetailJob?.cancel()
    }
}
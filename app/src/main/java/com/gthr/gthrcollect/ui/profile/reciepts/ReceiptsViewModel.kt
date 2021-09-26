package com.gthr.gthrcollect.ui.profile.reciepts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.data.repository.ReceiptRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ReceiptDisplayModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ReceiptsViewModel(val repository: ReceiptRepository) : ViewModel() {

    private val _mDisplayReceiptList = MutableLiveData<Event<List<ReceiptDisplayModel>>>()
    val mDisplayReceiptList: MutableLiveData<Event<List<ReceiptDisplayModel>>>
        get() = _mDisplayReceiptList

    private val _mSaleReceipt = MutableLiveData<Event<State<List<ReceiptDisplayModel>>>>()
    val mSaleReceipt: MutableLiveData<Event<State<List<ReceiptDisplayModel>>>>
        get() = _mSaleReceipt

    private val _mBuyReceipt = MutableLiveData<Event<State<List<ReceiptDisplayModel>>>>()
    val mBuyReceipt: MutableLiveData<Event<State<List<ReceiptDisplayModel>>>>
        get() = _mBuyReceipt

    var mSaleReceiptList = listOf<ReceiptDisplayModel>()
      private set

    var mBuyReceiptList = listOf<ReceiptDisplayModel>()
        private set

    fun setDisplayReceiptList(list : List<ReceiptDisplayModel>){
        _mDisplayReceiptList.value = Event(list)
    }

    fun setSaleReceiptList(list : List<ReceiptDisplayModel>){
        mSaleReceiptList = list
    }

    fun setBuyReceiptList(list : List<ReceiptDisplayModel>){
        mBuyReceiptList = list
    }

    fun fetchSaleReceipts(userId : String) = viewModelScope.launch {
        repository.fetchSaleReceipts(userId).collect {
            _mSaleReceipt.value = Event(it)
        }
    }

    fun fetchBuyReceipts(userId : String) = viewModelScope.launch {
        repository.fetchBuyReceipts(userId).collect {
            _mBuyReceipt.value = Event(it)
        }
    }

}
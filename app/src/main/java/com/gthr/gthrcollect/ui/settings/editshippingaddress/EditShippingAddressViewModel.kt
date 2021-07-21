package com.gthr.gthrcollect.ui.settings.editshippingaddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.data.repository.AddressRepository
import com.gthr.gthrcollect.model.Event
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel
import com.gthr.gthrcollect.model.mapper.toFirestoreModel
import com.gthr.gthrcollect.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditShippingAddressViewModel(val repository : AddressRepository) : BaseViewModel() {

    private val _mShippingAddressList = MutableLiveData<Event<State<List<ShippingAddressDomainModel>>>>()
    val mShippingAddressDomainModelList: LiveData<Event<State<List<ShippingAddressDomainModel>>>>
        get() = _mShippingAddressList




    fun getAllShippingAddress(uId : String){
        viewModelScope.launch {
            repository.getAddressFirestore(uId).collect {
                _mShippingAddressList.value = Event(it)
            }
        }
    }

    fun updateAddressList(list : List<ShippingAddressDomainModel>){
        viewModelScope.launch {
            repository.updateAddressListFirestore(list.toFirestoreModel(),GthrCollect.prefs?.signedInUser!!.uid).collect {
                _mShippingAddressList.value = Event(it)
            }
        }
    }

    fun deleteAddress(shippingAddressDomainModel : ShippingAddressDomainModel){
        val list = (_mShippingAddressList.value?.peekContent() as State.Success).data.toMutableList()
        list.remove(shippingAddressDomainModel)
        if(list.size>0&&shippingAddressDomainModel.isSelected){
            val item = list[0].copy()
            item.isSelected = true
            list[0] = item
        }
        updateAddressList(list)
    }



    fun changeAddressSelectedStatus(shippingAddressDomainModel: ShippingAddressDomainModel) {

    }

    fun deletedAddress() {

    }
}
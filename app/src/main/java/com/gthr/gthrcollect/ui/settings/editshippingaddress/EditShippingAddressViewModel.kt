package com.gthr.gthrcollect.ui.settings.editshippingaddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.model.domain.ShippingAddress
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.utils.extensions.fromJsonString
import com.gthr.gthrcollect.utils.extensions.gson

class EditShippingAddressViewModel : BaseViewModel() {

    private val _mShippingAddressList = MutableLiveData<List<ShippingAddress>>()
    val mShippingAddressList: LiveData<List<ShippingAddress>>
        get() = _mShippingAddressList

    init {
        getShippingAddress()
    }

    private fun getShippingAddress() {
        _mShippingAddressList.value =
            gson.fromJsonString<List<ShippingAddress>>(GthrCollect.prefs?.shippingAddresses!!)
    }

    fun changeAddressSelectedStatus(shippingAddress: ShippingAddress) {
//        if (shippingAddress)
    }

    fun deletedAddress() {

    }
}
package com.gthr.gthrcollect.ui.settings.addnewaddress

import com.google.gson.Gson
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.model.domain.ShippingAddress
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.utils.extensions.fromJsonString
import com.gthr.gthrcollect.utils.extensions.gson
import com.gthr.gthrcollect.utils.extensions.toJsonString

class AddNewAddressViewModel : BaseViewModel() {


    fun setShippingAddress(shippingAddress: ShippingAddress) {
        val addresses = GthrCollect.prefs?.shippingAddresses ?: "[]"
        val listOfAddress: MutableList<ShippingAddress> =
            Gson().fromJsonString<List<ShippingAddress>>(addresses)?.toMutableList()
                ?: mutableListOf()
        listOfAddress.add(shippingAddress)
        listOfAddress.toList()
        GthrCollect.prefs?.shippingAddresses = gson.toJsonString(listOfAddress)
    }
}
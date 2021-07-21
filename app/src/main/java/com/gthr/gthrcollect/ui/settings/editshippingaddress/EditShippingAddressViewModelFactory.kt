package com.gthr.gthrcollect.ui.settings.editshippingaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.AddressRepository

class EditShippingAddressViewModelFactory(private val repository: AddressRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditShippingAddressViewModel::class.java)) {
            return EditShippingAddressViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
package com.gthr.gthrcollect.ui.settings.addnewaddress

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.AddressRepository


class AddNewAddressViewModelFactory(private val context: Context, private val repository: AddressRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewAddressViewModel::class.java)) {
            return AddNewAddressViewModel(context,repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

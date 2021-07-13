package com.gthr.gthrcollect.ui.settings.addnewaddress

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AddNewAddressViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewAddressViewModel::class.java)) {
            return AddNewAddressViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

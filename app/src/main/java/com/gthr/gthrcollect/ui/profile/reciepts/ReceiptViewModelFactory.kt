package com.gthr.gthrcollect.ui.profile.reciepts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.ReceiptRepository

class ReceiptViewModelFactory(private val receiptRepository: ReceiptRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceiptsViewModel::class.java)) {
            return ReceiptsViewModel(receiptRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
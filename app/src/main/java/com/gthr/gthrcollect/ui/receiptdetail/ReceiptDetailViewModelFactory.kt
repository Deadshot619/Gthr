package com.gthr.gthrcollect.ui.receiptdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.ReceiptRepository

class ReceiptDetailViewModelFactory(private val repository: ReceiptRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceiptDetailViewModel::class.java)) {
            return ReceiptDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
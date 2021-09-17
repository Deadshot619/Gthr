package com.gthr.gthrcollect.ui.productdetail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.ui.editprofile.EditProfileViewModel

class ProductDetailsViewModelFactory(private val mProductDetailsRepository : ProductDetailsRepository,private val mDynamicLinkRepository : DynamicLinkRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            return ProductDetailsViewModel(mProductDetailsRepository,mDynamicLinkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
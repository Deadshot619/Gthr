package com.gthr.gthrcollect.ui.askflow

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gthr.gthrcollect.ui.base.BaseViewModel

class AskFlowViewModel: BaseViewModel() {

    private val _isSell = MutableLiveData<Boolean>()
    val isSell: LiveData<Boolean>
        get() = _isSell

    private val _askPrice = MutableLiveData<Float>()
    val askPrice: LiveData<Float>
        get() = _askPrice

    private val _frontImageBitmap = MutableLiveData<Bitmap>()
    val frontImageBitmap: LiveData<Bitmap>
        get() = _frontImageBitmap

    private val _backImageBitmap = MutableLiveData<Bitmap>()
    val backImageBitmap: LiveData<Bitmap>
        get() = _backImageBitmap

    fun setSell(isSell: Boolean) {
        _isSell.value = isSell
    }

    fun setFrontImage(bitmap: Bitmap) {
        _frontImageBitmap.value = bitmap
    }

    fun setBackImage(bitmap: Bitmap) {
        _backImageBitmap.value = bitmap
    }

    fun setAskPrice(price: Float) {
        _askPrice.value = price
    }
}
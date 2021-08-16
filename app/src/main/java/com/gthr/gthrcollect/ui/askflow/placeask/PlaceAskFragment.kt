package com.gthr.gthrcollect.ui.askflow.placeask

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.PlaceAskFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class PlaceAskFragment : BaseFragment<PlaceAskViewModel, PlaceAskFragmentBinding>() {
    override val mViewModel: PlaceAskViewModel by viewModels()
    override fun getViewBinding() = PlaceAskFragmentBinding.inflate(layoutInflater)

    override fun onBinding() {

    }
}
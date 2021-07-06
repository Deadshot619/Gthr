package com.gthr.gthrcollect.ui.settings.editshippingaddress

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.EditShippingAddressFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class EditShippingAddressFragment :
    BaseFragment<EditShippingAddressViewModel, EditShippingAddressFragmentBinding>() {
    override fun getViewBinding() = EditShippingAddressFragmentBinding.inflate(layoutInflater)
    override val mViewModel: EditShippingAddressViewModel by viewModels()

    override fun onBinding() {}
}
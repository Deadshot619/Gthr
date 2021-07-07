package com.gthr.gthrcollect.ui.settings.editshippingaddress

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.EditShippingAddressFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomImageTextButton

class EditShippingAddressFragment :
    BaseFragment<EditShippingAddressViewModel, EditShippingAddressFragmentBinding>() {
    override fun getViewBinding() = EditShippingAddressFragmentBinding.inflate(layoutInflater)
    override val mViewModel: EditShippingAddressViewModel by viewModels()

    private lateinit var mBtnAddNewAddress: CustomImageTextButton

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        //TODO 7/7/2021 : Have to use recyclerview to fill Address data
    }

    fun initViews() {
        mViewBinding.run {
            mBtnAddNewAddress = mViewBinding.btnAddNewAddress
        }
    }

    private fun setUpClickListeners() {
        mBtnAddNewAddress.setOnClickListener {
//            findNavController().navigate()
        }
    }
}
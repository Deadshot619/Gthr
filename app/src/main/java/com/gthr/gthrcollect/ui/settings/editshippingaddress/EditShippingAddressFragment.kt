package com.gthr.gthrcollect.ui.settings.editshippingaddress

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.EditShippingAddressFragmentBinding
import com.gthr.gthrcollect.model.domain.ShippingAddress
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomImageTextButton
import com.gthr.gthrcollect.utils.extensions.showToast

class EditShippingAddressFragment :
    BaseFragment<EditShippingAddressViewModel, EditShippingAddressFragmentBinding>() {
    override fun getViewBinding() = EditShippingAddressFragmentBinding.inflate(layoutInflater)
    override val mViewModel: EditShippingAddressViewModel by viewModels()

    private lateinit var mBtnAddNewAddress: CustomImageTextButton
    private lateinit var mRvShippingAddress: RecyclerView
    private lateinit var mRvAdapter: ShippingAddressAdapter

    override fun onBinding() {
        mViewBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }
        initViews()
        setUpClickListeners()
        setUpRecyclerView()
    }

    fun initViews() {
        mViewBinding.run {
            mBtnAddNewAddress = mViewBinding.btnAddNewAddress
            mRvShippingAddress = mViewBinding.rvShippingAddress
            mRvAdapter = ShippingAddressAdapter(object : ShippingAddressAdapter.SAClickListener {
                override fun onClickAddress(shippingAddress: ShippingAddress) {
                    showToast("Address click")
                }

                override fun onClickEdit(shippingAddress: ShippingAddress) {
                    showToast("Edit click")
                }

                override fun onClickDelete(shippingAddress: ShippingAddress) {
                    showToast("Delete click")
                }
            })
        }
    }

    private fun setUpClickListeners() {
        mBtnAddNewAddress.setOnClickListener {
            findNavController().navigate(EditShippingAddressFragmentDirections.actionEditShippingAddressFragmentToAddNewAddressFragment())
        }
    }

    private fun setUpRecyclerView() {
        mRvShippingAddress.adapter = mRvAdapter
    }
}
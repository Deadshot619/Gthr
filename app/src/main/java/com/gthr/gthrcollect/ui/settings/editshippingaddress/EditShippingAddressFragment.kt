package com.gthr.gthrcollect.ui.settings.editshippingaddress

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.gthr.gthrcollect.databinding.EditShippingAddressFragmentBinding
import com.gthr.gthrcollect.model.domain.ShippingAddress
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.settings.addnewaddress.AddNewAddressViewModelFactory
import com.gthr.gthrcollect.utils.customviews.CustomImageTextButton
import com.gthr.gthrcollect.utils.extensions.showToast

class EditShippingAddressFragment :
    BaseFragment<EditShippingAddressViewModel, EditShippingAddressFragmentBinding>() {
    override fun getViewBinding() = EditShippingAddressFragmentBinding.inflate(layoutInflater)
    override val mViewModel: EditShippingAddressViewModel by viewModels ()

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

                    val action =
                        EditShippingAddressFragmentDirections.actionEditShippingAddressFragmentToAddNewAddressFragment(
                            shippingAddress,
                            true
                        )
                    findNavController().navigate(action)
                }

                override fun onClickDelete(shippingAddress: ShippingAddress) {
                    showToast("Delete click")
                }
            })
        }
    }

    private fun setUpClickListeners() {
        mBtnAddNewAddress.setOnClickListener {
            val action =
                EditShippingAddressFragmentDirections.actionEditShippingAddressFragmentToAddNewAddressFragment(
                    null,
                    false
                )
            findNavController().navigate(action)
        }
    }

    private fun setUpRecyclerView() {
        mRvShippingAddress.adapter = mRvAdapter
    }


    override fun onStart() {
        super.onStart()
        mViewModel.getShippingAddress()
    }
}



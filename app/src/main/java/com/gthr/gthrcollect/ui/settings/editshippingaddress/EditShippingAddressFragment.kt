package com.gthr.gthrcollect.ui.settings.editshippingaddress

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AddressRepository
import com.gthr.gthrcollect.databinding.EditShippingAddressFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomImageTextButton
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel

class EditShippingAddressFragment :
    BaseFragment<EditShippingAddressViewModel, EditShippingAddressFragmentBinding>() {
    override fun getViewBinding() = EditShippingAddressFragmentBinding.inflate(layoutInflater)

    private val addressRepository = AddressRepository()
    override val mViewModel: EditShippingAddressViewModel by viewModels {
        EditShippingAddressViewModelFactory(
             addressRepository
        )
    }
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
        setUpObservers()
    }

    private fun setUpObservers() {

        mViewModel.mShippingAddressDomainModelList.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        mRvAdapter.submitList(it.data)
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }
    }

        fun initViews() {
        mViewBinding.run {
            mBtnAddNewAddress = mViewBinding.btnAddNewAddress
            mRvShippingAddress = mViewBinding.rvShippingAddress
            mRvAdapter = ShippingAddressAdapter(object : ShippingAddressAdapter.SAClickListener {
                override fun onClickAddress(shippingAddressDomainModel: ShippingAddressDomainModel) {
                    mViewModel.updateAddressList(mRvAdapter.currentList.setDefault(shippingAddressDomainModel))
                }
                override fun onClickEdit(shippingAddressDomainModel: ShippingAddressDomainModel) {
                    val action = EditShippingAddressFragmentDirections.actionEditShippingAddressFragmentToAddNewAddressFragment(
                        shippingAddressDomainModel,
                        true,
                        mRvAdapter.currentList.toTypedArray()
                    )
                    findNavController().navigate(action)
                }
                override fun onClickDelete(shippingAddressDomainModel: ShippingAddressDomainModel) {
                    showDeleteDialog{
                        mViewModel.deleteAddress(shippingAddressDomainModel)
                    }
                }
            })
            initProgressBar(layoutProgress)
        }
    }

    private fun setUpClickListeners() {
        mBtnAddNewAddress.setOnClickListener {
            val action =
                EditShippingAddressFragmentDirections.actionEditShippingAddressFragmentToAddNewAddressFragment(
                    null,
                    false,
                    mRvAdapter.currentList.toTypedArray()
                )
            findNavController().navigate(action)
        }
    }

    private fun setUpRecyclerView() {
        mRvShippingAddress.adapter = mRvAdapter
    }


    override fun onStart() {
        super.onStart()
        mViewModel.getAllShippingAddress(GthrCollect.prefs?.signedInUser!!.uid)
    }

    private fun showDeleteDialog(callback :()->Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_title_confirm_delete))
            .setMessage(getString(R.string.dialog_msg_delete_address_note))
            .setPositiveButton(getString(R.string.dialog_btn_delete)) { _, _ ->
                callback()
            }
            .setNegativeButton(getString(R.string.dialog_btn_cancel)) { _, _ ->
                showToast(getString(R.string.dialog_btn_cancel))
            }.show();
    }
    
    fun List<ShippingAddressDomainModel>.setDefault(model : ShippingAddressDomainModel) : List<ShippingAddressDomainModel>{
       return this.map{ shippingAddressDomainModel ->
           shippingAddressDomainModel.copy(isSelected = shippingAddressDomainModel==model)
        }
    }
}



package com.gthr.gthrcollect.ui.settings.editshippingaddress

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gthr.gthrcollect.R
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
                    showDeleteDialog()
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

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_title_confirm_delete))
            .setMessage(getString(R.string.dialog_msg_delete_address_note))
            .setPositiveButton(getString(R.string.dialog_btn_delete)) { dialog, _ ->
                showToast(getString(R.string.dialog_btn_delete))
            }
            .setNegativeButton(getString(R.string.dialog_btn_cancel)) { _, _ ->
                showToast(getString(R.string.dialog_btn_cancel))
            }.show();
    }
}



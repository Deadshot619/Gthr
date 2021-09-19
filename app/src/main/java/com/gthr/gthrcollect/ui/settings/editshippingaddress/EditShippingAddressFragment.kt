package com.gthr.gthrcollect.ui.settings.editshippingaddress

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.GthrCollect.Companion.prefs
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AddressRepository
import com.gthr.gthrcollect.databinding.EditShippingAddressFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomImageTextButton
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel
import com.gthr.gthrcollect.ui.askflow.afplaceyourask.AfPlaceYourAskFragment.Companion.getReturnIntent
import com.gthr.gthrcollect.ui.editaccountinfo.eaotp.EaOtpFragmentArgs
import com.gthr.gthrcollect.utils.extensions.updateShippingAddress

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

    private val args by navArgs<EditShippingAddressFragmentArgs>()

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
                        prefs?.updateShippingAddress(it.data)
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }
        mViewModel.mDefaultAddress.observe(viewLifecycleOwner){
            it.contentIfNotHandled?.let {
                if(args.fromAskFlow&&it!=null){
                    activity?.setResult(Activity.RESULT_OK,getReturnIntent(it))
                    activity?.finish();
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
                    if(args.fromAskFlow)
                        mViewModel.updateAddressList(mRvAdapter.currentList.setDefault(shippingAddressDomainModel),shippingAddressDomainModel)
                    else
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



package com.gthr.gthrcollect.ui.settings.addnewaddress

import android.text.InputType
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AddressRepository
import com.gthr.gthrcollect.databinding.AddNewAddressFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.settings.SettingsActivity
import com.gthr.gthrcollect.ui.settings.addnewaddress.adapter.SpinnerAdapter
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton
import com.gthr.gthrcollect.utils.customviews.CustomEditText
import com.gthr.gthrcollect.utils.customviews.CustomSpinner
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.selected
import com.gthr.gthrcollect.utils.extensions.showToast


class AddNewAddressFragment : BaseFragment<AddNewAddressViewModel, AddNewAddressFragmentBinding>() {

    override fun getViewBinding() = AddNewAddressFragmentBinding.inflate(layoutInflater)

    private val addressRepository = AddressRepository()
    override val mViewModel: AddNewAddressViewModel by viewModels {
        AddNewAddressViewModelFactory(
            requireContext(), addressRepository
        )
    }
    private val args by navArgs<AddNewAddressFragmentArgs>()

    private lateinit var mTvTitle: TextView
    private lateinit var mEtFirstName: CustomEditText
    private lateinit var mEtLastName: CustomEditText
    private lateinit var mEtAdd1: CustomEditText
    private lateinit var mEtAdd2: CustomEditText
    private lateinit var mEtCity: CustomEditText
    private lateinit var mEtPostalCode: CustomEditText
    private lateinit var mSpnCountry: CustomSpinner
    private lateinit var mSpnState: CustomSpinner
    private lateinit var mBtnAddNewAdd: CustomAuthenticationButton

    private var mSpnCountryFirstTimeFlag = true
    private var mSpnStateFirstTimeFlag = true
    private val mAllAddressList = arrayListOf<ShippingAddressDomainModel>()


    override fun onBinding() {

        initViews()
        setUpClickListeners()
        setUpOnItemSelectedListeners()
        setUpTextChangeListeners()
        setUpInputType()
        setUpObservers()
        populateCountryState()

        args.addressList?.let {
            mAllAddressList.addAll(it)
            if (args.isEdit) {
                setViewWithValues(args.item!!)
            } else {
                mBtnAddNewAdd.text = getString(R.string.add_new_address)
            }
        }

    }

    private fun initViews() {
        mTvTitle = mViewBinding.tvTitle
        mEtFirstName = mViewBinding.etFirstName
        mEtLastName = mViewBinding.etLastName
        mEtAdd1 = mViewBinding.etAdd1
        mEtAdd2 = mViewBinding.etAdd2
        mEtCity = mViewBinding.etCity
        mSpnCountry = mViewBinding.spnCountry
        mSpnState = mViewBinding.spnState
        mBtnAddNewAdd = mViewBinding.btnAddNewAdd
        mEtPostalCode = mViewBinding.etPostalCode
        initProgressBar(mViewBinding.layoutProgress)
    }

    private fun setViewWithValues(shippingAddressDomainModel: ShippingAddressDomainModel) {
        mBtnAddNewAdd.text = getString(R.string.text_save_changes)
        mTvTitle.text = getString(R.string.edit_shipping_address_text)
        (activity as SettingsActivity).setToolbarTitle(getString(R.string.edit_shipping_address_text))

        mEtFirstName.mEtMain.setText(shippingAddressDomainModel.firstName)
        mEtLastName.mEtMain.setText(shippingAddressDomainModel.lastName)
        mEtAdd1.mEtMain.setText(shippingAddressDomainModel.addressLine1)
        mEtAdd2.mEtMain.setText(shippingAddressDomainModel.addressLine2)
        mEtCity.mEtMain.setText(shippingAddressDomainModel.city)
        mEtPostalCode.mEtMain.setText(shippingAddressDomainModel.postalCode)


        for (index in mViewModel.countryList!!.indices) {
            if (mViewModel.countryList!![index] == shippingAddressDomainModel.country) {
                mSpnCountry.mSpnMain.setSelection(index)
                break
            }
        }


    }

    private fun populateCountryState() {
        mSpnCountry.mSpnMain.adapter = SpinnerAdapter(mViewModel.countryList!!)
    }

    private fun setUpInputType() {
        mEtPostalCode.mEtMain.inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun setUpTextChangeListeners() {
        mEtFirstName.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtFirstName.setSuccess()
                if (validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            } else {
                mEtFirstName.setInitial()
                mBtnAddNewAdd.disableAuthButton()
            }
        }

        mEtLastName.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtLastName.setSuccess()
                if (validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            } else {
                mEtLastName.setInitial()
                mBtnAddNewAdd.disableAuthButton()
            }
        }

        mEtAdd1.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtAdd1.setSuccess()
                if (validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            } else {
                mEtAdd1.setInitial()
                mBtnAddNewAdd.disableAuthButton()
            }
        }

        mEtAdd2.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtAdd2.setSuccess()
            } else {
                mEtAdd2.setInitial()
            }
        }


        mEtCity.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtCity.setSuccess()
                if (validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            } else {
                mEtCity.setInitial()
                mBtnAddNewAdd.disableAuthButton()
            }
        }

        mEtPostalCode.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtPostalCode.setSuccess()
                if (validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            } else {
                mEtPostalCode.setInitial()
                mBtnAddNewAdd.disableAuthButton()
            }

        }
    }

    private fun setUpOnItemSelectedListeners() {
        mSpnState.mSpnMain.selected {
            if (it != 0) {
                mSpnState.setSuccess()
                if (validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            } else {
                mSpnState.setInitial()
                if (mSpnStateFirstTimeFlag)
                    mSpnStateFirstTimeFlag = false
                else
                    mBtnAddNewAdd.disableAuthButton()
            }
        }

        mSpnCountry.mSpnMain.selected {

            if (it != 0) {
                mSpnCountry.setSuccess()
                if (validate(false)) {
                    mBtnAddNewAdd.enableAuthButton()
                } else {
                    mBtnAddNewAdd.disableAuthButton()
                    mViewModel.getStates(mSpnCountry.mSpnMain.selectedItem.toString())
                }
            } else {
                mViewModel.getStates(mSpnCountry.mSpnMain.selectedItem.toString())
                mSpnCountry.setInitial()
                if (mSpnCountryFirstTimeFlag)
                    mSpnCountryFirstTimeFlag = false
                else
                    mBtnAddNewAdd.disableAuthButton()
            }
        }
    }

    private fun setUpObservers() {
        mViewModel.stateList.observe(viewLifecycleOwner, {
            mSpnState.mSpnMain.adapter = SpinnerAdapter(it)
            if (args.isEdit) {
                for (index in it.indices) {
                    if (it[index] == args.item?.state) {
                        mSpnState.mSpnMain.setSelection(index)
                        break
                    }
                }
            }
        })

        mViewModel.addAddress.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        Navigation.findNavController(mViewBinding.root).navigateUp()
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }
    }

    private fun setUpClickListeners() {
        mBtnAddNewAdd.setOnClickListener {
            if (validate(true)) {
                if(args.isEdit){
                    val shippingAddressDomainModel = args.item
                    shippingAddressDomainModel?.let {
                        it.uid = GthrCollect.prefs?.signedInUser!!.uid
                        it.firstName = mEtFirstName.mEtMain.text.toString().trim()
                        it.lastName = mEtLastName.mEtMain.text.toString().trim()
                        it.addressLine1 = mEtAdd1.mEtMain.text.toString().trim()
                        it.addressLine2 = mEtAdd2.mEtMain.text.toString().trim()
                        it.country = mSpnCountry.mSpnMain.selectedItem.toString()
                        it.state = mSpnState.mSpnMain.selectedItem.toString()
                        it.city = mEtCity.mEtMain.text.toString().trim()
                        it.postalCode = mEtPostalCode.mEtMain.text.toString().trim()
                        mAllAddressList[shippingAddressDomainModel.id] = shippingAddressDomainModel
                        mViewModel.updateAddressList(mAllAddressList)
                    }

                }
                else{
                    mAllAddressList.add(ShippingAddressDomainModel(
                        uid = GthrCollect.prefs?.signedInUser!!.uid,
                        firstName = mEtFirstName.mEtMain.text.toString().trim(),
                        lastName = mEtLastName.mEtMain.text.toString().trim(),
                        addressLine1 = mEtAdd1.mEtMain.text.toString().trim(),
                        addressLine2 = mEtAdd2.mEtMain.text.toString().trim(),
                        country = mSpnCountry.mSpnMain.selectedItem.toString(),
                        state = mSpnState.mSpnMain.selectedItem.toString(),
                        city = mEtCity.mEtMain.text.toString().trim(),
                        postalCode = mEtPostalCode.mEtMain.text.toString().trim(),
                        isSelected = mAllAddressList.isEmpty()
                    ))
                    mViewModel.updateAddressList(mAllAddressList)
                }
            }
        }
    }

    private fun validate(isSetError: Boolean): Boolean {
        var isValidate = true
        if (mEtFirstName.mEtMain.text.toString().isEmpty()) {
            if (isSetError) mEtFirstName.setError(getString(R.string.add_new_address_name_error))
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mEtLastName.mEtMain.text.toString().isEmpty()) {
            if (isSetError) mEtLastName.setError(getString(R.string.add_new_address_last_error))
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mEtAdd1.mEtMain.text.toString().isEmpty()) {
            if (isSetError) mEtAdd1.setError(getString(R.string.add_new_address_add1_error))
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mEtCity.mEtMain.text.toString().isEmpty()) {
            if (isSetError) mEtCity.setError(getString(R.string.add_new_address_city_error))
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mSpnState.mSpnMain.selectedItemPosition == 0) {
            if (isSetError) mSpnState.setError()
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mSpnCountry.mSpnMain.selectedItemPosition == 0) {
            if (isSetError) mSpnCountry.setError()
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mEtPostalCode.mEtMain.text.toString().isEmpty()) {
            if (isSetError) mEtPostalCode.setError(getString(R.string.add_new_address_postal_code_error))
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        return isValidate
    }
}
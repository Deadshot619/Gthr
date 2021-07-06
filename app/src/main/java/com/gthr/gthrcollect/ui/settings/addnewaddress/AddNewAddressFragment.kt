package com.gthr.gthrcollect.ui.settings.addnewaddress

import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AddNewAddressFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.settings.addnewaddress.adapter.SpinnerAdapter
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton
import com.gthr.gthrcollect.utils.customviews.CustomEditText
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.customviews.CustomSpinner
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.selected


class AddNewAddressFragment : BaseFragment<AddNewAddressViewModel, AddNewAddressFragmentBinding>(){

    override fun getViewBinding() = AddNewAddressFragmentBinding.inflate(layoutInflater)
    override val mViewModel: AddNewAddressViewModel by viewModels()

    private lateinit var mEtFirstName: CustomEditText
    private lateinit var mEtLastName: CustomEditText
    private lateinit var mEtAdd1: CustomEditText
    private lateinit var mEtAdd2: CustomEditText
    private lateinit var mEtCity: CustomEditText
    private lateinit var mEtPostalCode: CustomEditText
    private lateinit var mSpnCountry: CustomSpinner
    private lateinit var mSpnState: CustomSpinner
    private lateinit var mBtnAddNewAdd: CustomAuthenticationButton

    private var mSpnCountryFirstTimeFlag  = true
    private var mSpnStateFirstTimeFlag = true


    override fun onBinding() {

        initViews()
        setUpClickListeners()
        setUpOnItemSelectedListeners()
        setUpTextChangeListeners()
        setUpInputType()

        val country = arrayListOf<String>()
        country.add("Select Country")
        country.add("Usa")
        mSpnCountry.mSpnMain.adapter = SpinnerAdapter(country)

        val state = arrayListOf<String>()
        state.add("Select state/province")
        state.add("Alabama")
        state.add("Alaska")
        state.add("Arizona")
        state.add("Arkansas")
        state.add("California")
        mSpnState.mSpnMain.adapter = SpinnerAdapter(state)

    }

    private fun setUpInputType() {
        mEtPostalCode.mEtMain.inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun setUpTextChangeListeners(){
        mEtFirstName.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtFirstName.setSuccess()
                if(validate(false)) mBtnAddNewAdd.enableAuthButton()  else mBtnAddNewAdd.disableAuthButton()
            }
            else{
                mEtFirstName.setInitial()
                mBtnAddNewAdd.disableAuthButton()
            }
        }

        mEtLastName.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtLastName.setSuccess()
                if(validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            }
            else{
                mEtLastName.setInitial()
                mBtnAddNewAdd.disableAuthButton()
            }
        }

        mEtAdd1.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtAdd1.setSuccess()
                if(validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            }
            else{
                mEtAdd1.setInitial()
                mBtnAddNewAdd.disableAuthButton()
            }
        }

        mEtAdd2.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtAdd2.setSuccess()
                if(validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            }
            else{
                mEtAdd2.setInitial()
                mBtnAddNewAdd.disableAuthButton()
            }
        }

        mEtCity.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtCity.setSuccess()
                if(validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            }
            else{
                mEtCity.setInitial()
                mBtnAddNewAdd.disableAuthButton()
            }
        }
        mEtPostalCode.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtPostalCode.setSuccess()
                if(validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            }
            else{
                mEtPostalCode.setInitial()
                mBtnAddNewAdd.disableAuthButton()
            }

        }
    }

    private fun setUpOnItemSelectedListeners() {
        mSpnState.mSpnMain.selected {
            if(it!=0){
                mSpnState.setSuccess()
                if(validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            }
            else{
                mSpnState.setInitial()
                if(mSpnStateFirstTimeFlag)
                    mSpnStateFirstTimeFlag = false
                else
                    mBtnAddNewAdd.disableAuthButton()
            }

        }

        mSpnCountry.mSpnMain.selected {
            if(it!=0){
                mSpnCountry.setSuccess()
                if(validate(false)) mBtnAddNewAdd.enableAuthButton() else mBtnAddNewAdd.disableAuthButton()
            }
            else{
                mSpnCountry.setInitial()
                if(mSpnCountryFirstTimeFlag)
                    mSpnCountryFirstTimeFlag = false
                else
                    mBtnAddNewAdd.disableAuthButton()
            }

        }
    }

    private fun initViews() {
        mEtFirstName = mViewBinding.etFirstName
        mEtLastName = mViewBinding.etLastName
        mEtAdd1 = mViewBinding.etAdd1
        mEtAdd2 = mViewBinding.etAdd2
        mEtCity = mViewBinding.etCity
        mSpnCountry = mViewBinding.spnCountry
        mSpnState = mViewBinding.spnState
        mBtnAddNewAdd = mViewBinding.btnAddNewAdd
        mEtPostalCode = mViewBinding.etPostalCode
    }

    private fun setUpClickListeners() {
        mBtnAddNewAdd.setOnClickListener {
            if (validate(true)){

            }
        }
    }

    private fun validate(isSetError: Boolean) : Boolean{
        var isValidate = true
        if (mEtFirstName.mEtMain.text.toString().isEmpty()) {
            if(isSetError) mEtFirstName.setError(getString(R.string.add_new_address_name_error))
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mEtLastName.mEtMain.text.toString().isEmpty()) {
            if(isSetError)mEtLastName.setError(getString(R.string.add_new_address_last_error))
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mEtAdd1.mEtMain.text.toString().isEmpty()) {
            if(isSetError)mEtAdd1.setError(getString(R.string.add_new_address_add1_error))
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mEtAdd2.mEtMain.text.toString().isEmpty()) {
            if(isSetError)mEtAdd2.setError(getString(R.string.add_new_address_add12error))
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mEtCity.mEtMain.text.toString().isEmpty()) {
            if(isSetError)mEtCity.setError(getString(R.string.add_new_address_city_error))
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mSpnState.mSpnMain.selectedItemPosition==0) {
            if(isSetError)mSpnState.setError()
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mSpnCountry.mSpnMain.selectedItemPosition==0) {
            if(isSetError)mSpnCountry.setError()
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }
        if (mEtPostalCode.mEtMain.text.toString().isEmpty()) {
            if(isSetError)mEtPostalCode.setError(getString(R.string.add_new_address_postal_code_error))
            mBtnAddNewAdd.disableAuthButton()
            isValidate = false
        }


        return isValidate

    }


}
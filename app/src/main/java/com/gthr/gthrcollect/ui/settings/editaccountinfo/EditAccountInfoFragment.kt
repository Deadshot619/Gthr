package com.gthr.gthrcollect.ui.settings.editaccountinfo

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.DialogDeleteAccountBinding
import com.gthr.gthrcollect.databinding.EditAccountInfoFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.editaccountinfo.eauserinfo.EaUserInfoFragmentDirections
import com.gthr.gthrcollect.utils.constants.SimpleDateFormatConstants
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton
import com.gthr.gthrcollect.utils.customviews.CustomEditText
import com.gthr.gthrcollect.utils.customviews.CustomPhoneNoEditText
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.extensions.*
import java.text.SimpleDateFormat
import java.util.*

class EditAccountInfoFragment :
    BaseFragment<EditAccountInfoViewModel, EditAccountInfoFragmentBinding>() {
    override fun getViewBinding() = EditAccountInfoFragmentBinding.inflate(layoutInflater)
    override val mViewModel: EditAccountInfoViewModel by viewModels()

    private lateinit var mEtFirstName: CustomEditText
    private lateinit var mEtLastName: CustomEditText
    private lateinit var mEtMM: CustomEditText
    private lateinit var mEtDD: CustomEditText
    private lateinit var mEtYYYY: CustomEditText
    private lateinit var mEtPhoneNo: CustomPhoneNoEditText
    private lateinit var mEtEmail: CustomEditText
    private lateinit var mTvDeleteAccount: TextView
    private lateinit var mBtnSaveChanges: CustomAuthenticationButton

    private var selectedDate = Calendar.getInstance()


    override fun onBinding() {

        initValue()
        setUpBirthdayEditText()
        setTextChangeListeners()
        setUpClickListeners()

    }

    private fun initValue() {
        mEtFirstName = mViewBinding.etFirstName
        mEtLastName = mViewBinding.etLastName
        mEtMM = mViewBinding.etMm
        mEtDD = mViewBinding.etDd
        mEtYYYY = mViewBinding.etYear
        mEtPhoneNo = mViewBinding.etCustomPhoneNo
        mEtEmail = mViewBinding.etEmail
        mTvDeleteAccount = mViewBinding.tvDeleteAccount
        mBtnSaveChanges = mViewBinding.btnSaveChanges
    }

    private fun setUpBirthdayEditText() {
        mEtMM.mEtMain.isClickable = false
        mEtMM.mEtMain.isCursorVisible = false
        mEtMM.mEtMain.isFocusable = false
        mEtMM.mEtMain.isFocusableInTouchMode = false

        mEtDD.mEtMain.isClickable = false
        mEtDD.mEtMain.isCursorVisible = false
        mEtDD.mEtMain.isFocusable = false
        mEtDD.mEtMain.isFocusableInTouchMode = false

        mEtYYYY.mEtMain.isClickable = false
        mEtYYYY.mEtMain.isCursorVisible = false
        mEtYYYY.mEtMain.isFocusable = false
        mEtYYYY.mEtMain.isFocusableInTouchMode = false

        mEtMM.mEtMain.setOnClickListener {
            showDatePicker()
        }

        mEtDD.mEtMain.setOnClickListener {
            showDatePicker()
        }

        mEtYYYY.mEtMain.setOnClickListener {
            showDatePicker()
        }


    }

    private fun showDatePicker() {

        showBirthDayPicker(selectedDate.timeInMillis) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = it
            selectedDate = cal

            mEtMM.mEtMain.setText(SimpleDateFormat(SimpleDateFormatConstants.MONTH).format(cal.time))
            mEtDD.mEtMain.setText(SimpleDateFormat(SimpleDateFormatConstants.DATE).format(cal.time))
            mEtYYYY.mEtMain.setText(SimpleDateFormat(SimpleDateFormatConstants.YEAR).format(cal.time))

            mEtMM.setInitial()
            mEtDD.setInitial()
            mEtYYYY.setInitial()
        }


    }

    private fun setUpClickListeners() {
        mBtnSaveChanges.setOnClickListener {
            if (validate())
                showToast("Done.")
        }

        mTvDeleteAccount.setOnClickListener{
            customDialog()
        }

    }

    private fun setTextChangeListeners() {
        mEtFirstName.mEtMain.afterTextChanged {
            mEtFirstName.setInitial()
        }

        mEtLastName.mEtMain.afterTextChanged {
            mEtLastName.setInitial()
        }

        mEtPhoneNo.mEtPhoneNo.afterTextChanged {
            mEtPhoneNo.setInitial()
        }

        mEtPhoneNo.mEtPhoneNo.setOnClickListener {
            mEtPhoneNo.setInitial()
        }
    }



    private fun validate(): Boolean {
        var isValidate = true
        if (mEtFirstName.mEtMain.text.toString().isEmpty()) {
            mEtFirstName.setError(getString(R.string.edit_acc_name_error))
            isValidate = false
        }
        if (mEtLastName.mEtMain.text.toString().isEmpty()) {
            mEtLastName.setError(getString(R.string.edit_acc_last_error))
            isValidate = false
        }
        if (!selectedDate.isValidBirthDayDate()) {
            showToast(getString(R.string.edit_acc_birthday_error))
            mEtMM.setError(null)
            mEtDD.setError(null)
            mEtYYYY.setError(null)
            isValidate = false
        }
        if (mEtPhoneNo.mEtPhoneNo.text.toString().length != 10) {
            mEtPhoneNo.setError(getString(R.string.edit_acc_phone_no_error))
            isValidate = false
        }
        if (mEtEmail.mEtMain.text.toString().isEmpty()) {
            mEtEmail.setError(getString(R.string.edit_acc_empty_email_error))
            isValidate = false
        }
        else if (!mEtEmail.mEtMain.text.toString().isValidEmail()) {
            mEtEmail.setError(getString(R.string.edit_acc_email_error))
            isValidate = false
        }
        return isValidate
    }


    private fun customDialog(){
        val dialogBinding : DialogDeleteAccountBinding = DialogDeleteAccountBinding.inflate(LayoutInflater.from(requireContext()))

        dialogBinding.tvTitle.text = getString(R.string.acc_delete)
        dialogBinding.etMain.gone()



        dialogBinding.etMain.afterTextChanged{
            if(it=="Delete"){
                dialogBinding.positive.isEnabled = true
                dialogBinding.positive.setTextColor(getResolvedColor(R.color.blue))
            }
            else{
                dialogBinding.positive.isEnabled = false
                dialogBinding.positive.setTextColor(getResolvedColor(R.color.disable))
            }

        }

        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext()).setView(dialogBinding.root)
        val dialog = materialAlertDialogBuilder.show()

        dialogBinding.positive.setOnClickListener {
            if(dialogBinding.etMain.isVisible){
                if(dialogBinding.etMain.text.toString()=="Delete")
                    dialog.dismiss()
            }
            else{
                dialogBinding.positive.isEnabled = false
                dialogBinding.positive.setTextColor(getResolvedColor(R.color.disable))
                dialogBinding.etMain.visible()
                dialogBinding.tvTitle.text = getString(R.string.delete_acc_title_msg)
            }
        }

        dialogBinding.negative.setOnClickListener {
            dialog.dismiss()
        }
    }


}
package com.gthr.gthrcollect.ui.settings.editaccountinfo

import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.SettingsRepository
import com.gthr.gthrcollect.databinding.DialogDeleteAccountBinding
import com.gthr.gthrcollect.databinding.EditAccountInfoFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.EditAccInfoDomainModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.constants.CalendarConstants.MIN_AGE
import com.gthr.gthrcollect.utils.constants.SimpleDateFormatConstants
import com.gthr.gthrcollect.utils.customviews.CustomAuthenticationButton
import com.gthr.gthrcollect.utils.customviews.CustomEditText
import com.gthr.gthrcollect.utils.customviews.CustomPhoneNoEditText
import com.gthr.gthrcollect.utils.extensions.*
import java.text.SimpleDateFormat
import java.util.*

class EditAccountInfoFragment :
    BaseFragment<EditAccountInfoViewModel, EditAccountInfoFragmentBinding>() {

    override fun getViewBinding() = EditAccountInfoFragmentBinding.inflate(layoutInflater)

    private val repository = SettingsRepository()
    override val mViewModel: EditAccountInfoViewModel by viewModels {
        EditAccountInfoViewModelFactory(
            repository
        )
    }

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
        selectedDate.add(Calendar.YEAR, MIN_AGE)
        initView()
        setUpBirthdayEditText()
        setTextChangeListeners()
        setUpClickListeners()
        setUpObservers()
    }

    private fun setUpObservers() {
        mViewModel.userInfo.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        setData(it.data)
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.userDataUpdateFirestore.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        showToast(getString(R.string.text_user_data_update_success))
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }
    }

    private fun setData(data: EditAccInfoDomainModel) {
        mEtFirstName.mEtMain.setText(data.firstName)
        mEtLastName.mEtMain.setText(data.lastName)
        mEtEmail.mEtMain.setText(data.emailId)
        mEtPhoneNo.mEtPhoneNo.setText(data.mobile)
        mEtPhoneNo.mCcp.setCountryForPhoneCode(data.countryCode)
        mEtMM.mEtMain.setText(data.mm)
        mEtDD.mEtMain.setText(data.dd)
        mEtYYYY.mEtMain.setText(data.yyyy)
    }

    private fun initView() {
        mEtFirstName = mViewBinding.etFirstName
        mEtLastName = mViewBinding.etLastName
        mEtMM = mViewBinding.etMm
        mEtDD = mViewBinding.etDd
        mEtYYYY = mViewBinding.etYear
        mEtPhoneNo = mViewBinding.etCustomPhoneNo
        mEtEmail = mViewBinding.etEmail
        mTvDeleteAccount = mViewBinding.tvDeleteAccount
        mBtnSaveChanges = mViewBinding.btnSaveChanges

        mEtEmail.mEtMain.isClickable = false
        mEtEmail.mEtMain.isCursorVisible = false
        mEtEmail.mEtMain.isFocusable = false
        mEtEmail.mEtMain.isFocusableInTouchMode = false

        mEtPhoneNo.mEtPhoneNo.isClickable = false
        mEtPhoneNo.mEtPhoneNo.isCursorVisible = false
        mEtPhoneNo.mEtPhoneNo.isFocusable = false
        mEtPhoneNo.mEtPhoneNo.isFocusableInTouchMode = false

        mEtPhoneNo.mCcp.setCcpClickable(false)
        initProgressBar(mViewBinding.layoutProgress)
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

        mEtMM.mEtMain.setOnClickListener { showDatePicker() }
        mEtDD.mEtMain.setOnClickListener { showDatePicker() }
        mEtYYYY.mEtMain.setOnClickListener { showDatePicker() }
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
                mViewModel.updateUserDataFirestore(
                    EditAccInfoDomainModel(
                        mEtFirstName.mEtMain.text.toString(),
                        mEtLastName.mEtMain.text.toString(),
                        mEtDD.mEtMain.text.toString(),
                        mEtMM.mEtMain.text.toString(),
                        mEtYYYY.mEtMain.text.toString(),
                    )
                )
        }

        mTvDeleteAccount.setOnClickListener { customDeleteDialog() }
    }

    private fun setTextChangeListeners() {
        mEtFirstName.mEtMain.afterTextChanged { mEtFirstName.setInitial() }
        mEtLastName.mEtMain.afterTextChanged { mEtLastName.setInitial() }

        mEtPhoneNo.mEtPhoneNo.afterTextChanged { mEtPhoneNo.setInitial() }
        mEtPhoneNo.mEtPhoneNo.setOnClickListener { mEtPhoneNo.setInitial() }
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
        } else if (!mEtEmail.mEtMain.text.toString().isValidEmail()) {
            mEtEmail.setError(getString(R.string.edit_acc_email_error))
            isValidate = false
        }
        return isValidate
    }


    private fun customDeleteDialog() {
        val dialogBinding: DialogDeleteAccountBinding =
            DialogDeleteAccountBinding.inflate(LayoutInflater.from(requireContext()))
        dialogBinding.apply {
            tvTitle.text = getString(R.string.acc_delete)
            etMain.gone()

            etMain.afterTextChanged {
                if (it == "Delete") {
                    positive.isEnabled = true
                    positive.setTextColor(getResolvedColor(R.color.blue))
                } else {
                    positive.isEnabled = false
                    positive.setTextColor(getResolvedColor(R.color.disable))
                }
            }
        }

        val materialAlertDialogBuilder =
            MaterialAlertDialogBuilder(requireContext()).setView(dialogBinding.root)
        val dialog = materialAlertDialogBuilder.show()

        dialogBinding.positive.setOnClickListener {
            if (dialogBinding.etMain.isVisible) {
                if (dialogBinding.etMain.text.toString() == "Delete")
                    dialog.dismiss()
            } else {
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
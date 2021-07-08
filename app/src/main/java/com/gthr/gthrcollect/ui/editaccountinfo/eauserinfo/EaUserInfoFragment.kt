package com.gthr.gthrcollect.ui.editaccountinfo.eauserinfo

import android.annotation.SuppressLint
import android.os.Build
import android.text.InputFilter
import android.text.InputType
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.EaUserInfoFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.constants.SimpleDateFormatConstants.DATE
import com.gthr.gthrcollect.utils.constants.SimpleDateFormatConstants.MONTH
import com.gthr.gthrcollect.utils.constants.SimpleDateFormatConstants.YEAR
import com.gthr.gthrcollect.utils.customviews.CustomEditText
import com.gthr.gthrcollect.utils.customviews.CustomPhoneNoEditText
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.extensions.*
import java.text.SimpleDateFormat
import java.util.*


@RequiresApi(Build.VERSION_CODES.M)
class EaUserInfoFragment : BaseFragment<EaUserInfoViewModel, EaUserInfoFragmentBinding>() {
    override val mViewModel: EaUserInfoViewModel by viewModels()
    override fun getViewBinding() = EaUserInfoFragmentBinding.inflate(layoutInflater)


    private lateinit var mEtFirstName: CustomEditText
    private lateinit var mEtLastName: CustomEditText
    private lateinit var mBtnNext: CustomSecondaryButton
    private lateinit var mEtMM: CustomEditText
    private lateinit var mEtDD: CustomEditText
    private lateinit var mEtYYYY: CustomEditText
    private lateinit var mEtPhoneNo: CustomPhoneNoEditText
    private lateinit var mIvTermsAndConditions: AppCompatImageView

    private var isTnCCheked = false
    private var selectedDate = Calendar.getInstance()

    override fun onBinding() {

        selectedDate.add(Calendar.YEAR, -18)

        mViewBinding.lifecycleOwner = viewLifecycleOwner
        initValue()
        setUpClickListeners()
        setInputType()
        setUpBirthdayEditText()
        setTextChangeListeners()
        setUpObservers()
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


    @SuppressLint("SimpleDateFormat")
    private fun showDatePicker() {


        showBirthDayPicker(selectedDate.timeInMillis) {

            val cal = Calendar.getInstance()
            cal.timeInMillis = it
            selectedDate = cal

            mEtMM.mEtMain.setText(SimpleDateFormat(MONTH).format(cal.time))
            mEtDD.mEtMain.setText(SimpleDateFormat(DATE).format(cal.time))
            mEtYYYY.mEtMain.setText(SimpleDateFormat(YEAR).format(cal.time))

            if (cal.isValidBirthDayDate()) {
                mEtMM.setSuccess()
                mEtDD.setSuccess()
                mEtYYYY.setSuccess()
                if (validate()) mBtnNext.setState(CustomSecondaryButton.State.GREEN)
            } else {
                mEtMM.setError(null)
                mEtDD.setError(null)
                mEtYYYY.setError(null)
                showToast(getString(R.string.birthday_error_text))
                mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
            }
        }
    }

    private fun setInputType() {
        mEtMM.mEtMain.inputType = InputType.TYPE_CLASS_NUMBER
        mEtMM.mEtMain.filters = arrayOf(InputFilter.LengthFilter(2))

        mEtDD.mEtMain.inputType = InputType.TYPE_CLASS_NUMBER
        mEtDD.mEtMain.filters = arrayOf(InputFilter.LengthFilter(2))

        mEtYYYY.mEtMain.inputType = InputType.TYPE_CLASS_NUMBER
        mEtYYYY.mEtMain.filters = arrayOf(InputFilter.LengthFilter(4))
    }

    private fun initValue() {
        mEtFirstName = mViewBinding.etFirstName
        mEtLastName = mViewBinding.etLastName
        mBtnNext = mViewBinding.btnNext
        mEtMM = mViewBinding.etMm
        mEtDD = mViewBinding.etDd
        mEtYYYY = mViewBinding.etYear
        mEtPhoneNo = mViewBinding.etCustomPhoneNo
        mIvTermsAndConditions = mViewBinding.ivTermsAndConditions
    }

    private fun setUpClickListeners() {

        mViewBinding.btnNext.setOnClickListener {

            val userModel = UserInfoModel(
                mEtFirstName.mEtMain.text.toString().trim(),
                mEtLastName.mEtMain.text.toString().trim(),
                mEtDD.mEtMain.text.toString().trim(),
                mEtMM.mEtMain.text.toString().trim(),
                mEtYYYY.mEtMain.text.toString().trim(),
                mEtPhoneNo.mEtPhoneNo.text.toString().trim(),
                mEtPhoneNo.mCcp.selectedCountryCode,
                true
            )

            mViewModel.setUserInfo(userModel)
            findNavController().navigate(EaUserInfoFragmentDirections.actionEaUserInfoFragmentToEaOtpFragment())
        }

        mIvTermsAndConditions.setOnClickListener {
            if (!isTnCCheked) {
                isTnCCheked = !isTnCCheked
                if (validate()) mBtnNext.setState(CustomSecondaryButton.State.GREEN)
                toggleTnC(true)
            } else {
                isTnCCheked = !isTnCCheked
                mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                toggleTnC(false)
            }
        }
    }

    private fun toggleTnC(toggleOn: Boolean) {
        if (toggleOn) {
            mIvTermsAndConditions.setImageResource(R.drawable.ic_term_con_ckeck)
        } else {
            mIvTermsAndConditions.setImageResource(R.drawable.ic_terms_and_conditions)
        }
    }

    private fun setUpObservers() {
        mViewModel.userInfoLiveData.observe(viewLifecycleOwner, {

            if (it != null) {

                mEtFirstName.mEtMain.setText(it.fName)
                mEtLastName.mEtMain.setText(it.lName)
                mEtMM.mEtMain.setText(it.mm)
                mEtDD.mEtMain.setText(it.dd)
                mEtYYYY.mEtMain.setText(it.yyyy)
                mEtPhoneNo.mEtPhoneNo.setText(it.mobile)
                mEtPhoneNo.mCcp.setCountryForPhoneCode(it.country.toInt())
                isTnCCheked = it.tnc

                toggleTnC(isTnCCheked)

                mEtMM.setSuccess()
                mEtDD.setSuccess()
                mEtYYYY.setSuccess()
            }


        })

    }


    private fun setTextChangeListeners() {

        mEtFirstName.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtFirstName.setSuccess()


                if (validate()) mBtnNext.setState(CustomSecondaryButton.State.GREEN)

            } else {
                mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                mEtFirstName.setInitial()
            }

        }

        mEtLastName.mEtMain.afterTextChanged {
            if (it.isNotEmpty()) {
                mEtLastName.setSuccess()
                //    mEtLastName.mEtMain.setText(it.toString().trim())
                if (validate()) mBtnNext.setState(CustomSecondaryButton.State.GREEN)
            } else {
                mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                mEtLastName.setInitial()
            }

        }



        mEtPhoneNo.mEtPhoneNo.afterTextChanged {
            if (it.isValidPhoneNumber()) {
                mEtPhoneNo.setSuccess()
                //   mEtPhoneNo.mEtPhoneNo.setText(it.toString().trim())

                if (validate()) mBtnNext.setState(CustomSecondaryButton.State.GREEN)
            } else {
                mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                mEtPhoneNo.setInitial()
            }
        }
    }

    fun validate(): Boolean = when {
        mEtFirstName.mEtMain.text.toString().trim().isEmpty() -> false

        mEtLastName.mEtMain.text.toString().trim().isEmpty() -> false
        !selectedDate.isValidBirthDayDate() -> false
        !isTnCCheked -> false
        else -> mEtPhoneNo.mEtPhoneNo.text.toString().length == 10
    }
}
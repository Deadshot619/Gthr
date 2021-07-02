package com.gthr.gthrcollect.ui.eauserinfo

import android.os.Build
import android.text.InputFilter
import android.text.InputType
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.EaUserInfoFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomEditText
import com.gthr.gthrcollect.utils.customviews.CustomPhoneNoEditText
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.isValidPhoneNumber

@RequiresApi(Build.VERSION_CODES.M)
class EaUserInfoFragment : BaseFragment<EaUserInfoViewModel, EaUserInfoFragmentBinding>() {
    override val mViewModel: EaUserInfoViewModel by viewModels()
    override fun getViewBinding() = EaUserInfoFragmentBinding.inflate(layoutInflater)

    private lateinit var mEtFirstName : CustomEditText
    private lateinit var mEtLastName : CustomEditText
    private lateinit var mBtnNext : CustomSecondaryButton
    private lateinit var mEtMM : CustomEditText
    private lateinit var mEtDD : CustomEditText
    private lateinit var mEtYYYY : CustomEditText
    private lateinit var mEtPhoneNo : CustomPhoneNoEditText
    private lateinit var mIvTermsAndConditions : AppCompatImageView

    private var isTermsAndConditionsChecked = false

    override fun onBinding() {
        initValue()
        setUpClickListeners()
        setInputType()
        setTextChangeListeners()
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
            findNavController().navigate(EaUserInfoFragmentDirections.actionEaUserInfoFragmentToEaIdVerificationFragment("",""))
        }

        mIvTermsAndConditions.setOnClickListener {
            if(!isTermsAndConditionsChecked){
                isTermsAndConditionsChecked = !isTermsAndConditionsChecked
                mIvTermsAndConditions.setImageResource(R.drawable.ic_term_con_ckeck)
                if(validate()) mBtnNext.setState(CustomSecondaryButton.State.GREEN)
            }
            else{
                isTermsAndConditionsChecked = !isTermsAndConditionsChecked
                mIvTermsAndConditions.setImageResource(R.drawable.ic_terms_and_conditions)
                mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
            }

        }

    }


    private fun setTextChangeListeners() {
        mEtFirstName.mEtMain.afterTextChanged{
            if(it.isNotEmpty()){
                mEtFirstName.setSuccess()
                if(validate()) mBtnNext.setState(CustomSecondaryButton.State.GREEN)
            }
            else{
                mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                mEtFirstName.setInitial()
            }

        }

        mEtLastName.mEtMain.afterTextChanged{
            if(it.isNotEmpty()){
                mEtLastName.setSuccess()
                if(validate()) mBtnNext.setState(CustomSecondaryButton.State.GREEN)
            }
            else{
               mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                mEtLastName.setInitial()
            }

        }

        mEtMM.mEtMain.afterTextChanged{
            if(it.length==2){
                mEtMM.setSuccess()
                if(validate()) mBtnNext.setState(CustomSecondaryButton.State.GREEN)
            }
            else{
                mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                mEtMM.setInitial()
            }

        }

        mEtDD.mEtMain.afterTextChanged{
            if(it.length==2){
                mEtDD.setSuccess()
                if(validate()) mBtnNext.setState(CustomSecondaryButton.State.GREEN)
            }
            else{
               mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                mEtDD.setInitial()
            }

        }

        mEtYYYY.mEtMain.afterTextChanged{
            if(it.length == 4){
                mEtYYYY.setSuccess()
                if(validate()) mBtnNext.setState(CustomSecondaryButton.State.GREEN)
            }
            else{
                mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                mEtYYYY.setInitial()
            }

        }

        mEtPhoneNo.mEtPhoneNo.afterTextChanged {
            if(it.isValidPhoneNumber()){
                mEtPhoneNo.setSuccess()
                if(validate()) mBtnNext.setState(CustomSecondaryButton.State.GREEN)
            }
            else{
                mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                mEtPhoneNo.setError("")
            }

        }
    }

    fun validate():Boolean{
        return when {
            mEtFirstName.mEtMain.text.toString().isEmpty() -> false
            mEtLastName.mEtMain.text.toString().isEmpty() -> false
            mEtMM.mEtMain.text.toString().length!=2 -> false
            mEtDD.mEtMain.text.toString().length!=2 -> false
            mEtYYYY.mEtMain.text.toString().length!=4 -> false
            !isTermsAndConditionsChecked -> false
            else -> mEtPhoneNo.mEtPhoneNo.text.toString().length == 10
        }
    }
}
package com.gthr.gthrcollect.ui.eaprofile

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.EaProfileFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomEditText
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.isValidDisplayName

@RequiresApi(Build.VERSION_CODES.M)
class EaProfileFragment : BaseFragment<EaProfileViewModel, EaProfileFragmentBinding>() {
    override val mViewModel: EaProfileViewModel by viewModels()
    override fun getViewBinding() = EaProfileFragmentBinding.inflate(layoutInflater)

    private lateinit var mEtDisplayName : CustomEditText
    private lateinit var mBtnNext : CustomSecondaryButton
    private lateinit var mEtBio : EditText

    private var isValidate = false



    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setTextChangeListeners()





    }

    private fun initViews() {
        mEtDisplayName = mViewBinding.etDisplayName
        mBtnNext = mViewBinding.btnNext
        mEtBio = mViewBinding.etBio

    }

    private fun setUpClickListeners() {
        mViewBinding.btnNext.setOnClickListener {
            findNavController().navigate(EaProfileFragmentDirections.actionEaProfileFragmentToEaUserInfoFragment())
        }
    }


    private fun setTextChangeListeners() {
        mEtDisplayName.mEtMain.afterTextChanged{
            when {
                it.isValidDisplayName() -> {
                    mEtDisplayName.setSuccess()
                    mBtnNext.setState(CustomSecondaryButton.State.BLUE)
                }
                it.isNotEmpty() -> {
                    mEtDisplayName.setError(getString(R.string.display_name_error_msg))
                    mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                }
                else -> {
                    mEtDisplayName.setInitial()
                    mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                }
            }
        }
    }


}
package com.gthr.gthrcollect.ui.editaccountinfo.eaprofile

import android.os.Build
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.EditAccountInfoRepository
import com.gthr.gthrcollect.databinding.EaProfileFragmentBinding
import com.gthr.gthrcollect.model.domain.UserInfoDomainModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.editaccountinfo.EditAccountInfoViewModel
import com.gthr.gthrcollect.ui.editaccountinfo.EditAccountInfoViewModelFactory
import com.gthr.gthrcollect.utils.customviews.CustomEditText
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.extensions.afterTextChanged
import com.gthr.gthrcollect.utils.extensions.isValidDisplayName

@RequiresApi(Build.VERSION_CODES.M)
class EaProfileFragment : BaseFragment<EditAccountInfoViewModel, EaProfileFragmentBinding>() {

    private val repository = EditAccountInfoRepository()

    override val mViewModel: EditAccountInfoViewModel by activityViewModels {
        EditAccountInfoViewModelFactory(
            repository
        )
    }

    override fun getViewBinding() = EaProfileFragmentBinding.inflate(layoutInflater)

    private lateinit var mEtDisplayName: CustomEditText
    private lateinit var mBtnNext: CustomSecondaryButton
    private lateinit var mEtBio: EditText

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setTextChangeListeners()
        setUpObservers()
    }

    private fun initViews() {
        mEtDisplayName = mViewBinding.etDisplayName
        mBtnNext = mViewBinding.btnNext
        mEtBio = mViewBinding.etBio
    }

    private fun setUpClickListeners() {
        mViewBinding.btnNext.setOnClickListener {
            val userModel = mViewModel.userInfoLiveData.value?.apply {
                this.displayName = mEtDisplayName.mEtMain.text.toString().trim()
                this.bio = mEtBio.text.toString().trim()
            } ?: UserInfoDomainModel()

            mViewModel.setUserInfo(userModel)
            findNavController().navigate(EaProfileFragmentDirections.actionEaProfileFragmentToEaUserInfoFragment())
        }
    }

    private fun setUpObservers() {
        mViewModel.userInfoLiveData.observe(viewLifecycleOwner, {
            it?.let {
                if (it.displayName.isNotEmpty()) {
                    mEtDisplayName.mEtMain.setText(it.displayName)
                    mEtBio.setText(it.bio)
                }
            }
        })
    }

    private fun setTextChangeListeners() {
        mEtDisplayName.mEtMain.afterTextChanged {
            when {
                it.length < 4 -> {
                    mEtDisplayName.setError(getString(R.string.must_be_4_char))
                    mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                }
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
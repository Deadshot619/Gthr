package com.gthr.gthrcollect.ui.settings.settingsscreen

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.databinding.SettingsFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.settings.SettingsViewModel
import com.gthr.gthrcollect.utils.customviews.CustomImageTextButton

class SettingsFragment : BaseFragment<SettingsViewModel, SettingsFragmentBinding>() {
    override fun getViewBinding() = SettingsFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SettingsViewModel by activityViewModels()

    private lateinit var mBtnEditAccountInfo: CustomImageTextButton

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnEditAccountInfo = btnEditAccountInfo
        }
    }

    private fun setUpClickListeners() {
        mBtnEditAccountInfo.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToEditAccountInfoFragment())
        }
    }

}
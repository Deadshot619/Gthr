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
    private lateinit var mBtnShippingAddress: CustomImageTextButton
    private lateinit var mBtnActiveOffers: CustomImageTextButton
    private lateinit var mBtnPayoutInfo: CustomImageTextButton
    private lateinit var mBtnTermsAndConditions: CustomImageTextButton
    private lateinit var mBtnFaqAndHelp: CustomImageTextButton

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnEditAccountInfo = btnEditAccountInfo
            mBtnShippingAddress = btnShippingAddress
            mBtnActiveOffers = btnActiveOffers
            mBtnPayoutInfo = btnPayoutInfo
            mBtnTermsAndConditions = btnTermsConditions
            mBtnFaqAndHelp = btnFaq
        }
    }

    private fun setUpClickListeners() {
        mBtnEditAccountInfo.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToEditAccountInfoFragment())
        }
        mBtnShippingAddress.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToEditShippingAddressFragment())
        }
        mBtnActiveOffers.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToActiveOffersFragment())
        }
        mBtnPayoutInfo.setOnClickListener {

        }
        mBtnTermsAndConditions.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToTermsAndConditionsFragment())
        }
        mBtnFaqAndHelp.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToFaqAndHelpFragment())
        }
    }

}
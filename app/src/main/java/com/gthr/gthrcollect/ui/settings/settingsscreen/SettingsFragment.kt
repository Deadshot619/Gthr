package com.gthr.gthrcollect.ui.settings.settingsscreen

import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.SettingsFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.settings.SettingsViewModel
import com.gthr.gthrcollect.ui.termsandfaq.TermsAndFaqActivity
import com.gthr.gthrcollect.utils.customviews.CustomImageTextButton
import com.gthr.gthrcollect.utils.enums.WebViewType

class SettingsFragment : BaseFragment<SettingsViewModel, SettingsFragmentBinding>() {
    override fun getViewBinding() = SettingsFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SettingsViewModel by activityViewModels()

    private lateinit var mBtnEditAccountInfo: CustomImageTextButton
    private lateinit var mBtnShippingAddress: CustomImageTextButton
    private lateinit var mBtnActiveOffers: CustomImageTextButton
    private lateinit var mBtnPayoutInfo: CustomImageTextButton
    private lateinit var mBtnTermsAndConditions: CustomImageTextButton
    private lateinit var mBtnFaqAndHelp: CustomImageTextButton
    private lateinit var mTvLogout: AppCompatTextView

    private val mLogoutDialog by lazy {
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.dialog_logout_title))
            .setMessage(getString(R.string.dialog_logout_body))
            .setPositiveButton(getString(R.string.dialog_logout_pos_btn)) { dialog, _ ->
                logout()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_logout_neg_btn)) { dialog, _ ->
                dialog.dismiss()
            }
    }

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
            mTvLogout = tvLogout
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
            startActivity(
                TermsAndFaqActivity.getInstance(
                    requireContext(),
                    WebViewType.TERMS_AND_CONDITION
                )
            )
        }
        mBtnFaqAndHelp.setOnClickListener {
            startActivity(TermsAndFaqActivity.getInstance(requireContext(), WebViewType.FAQ))
        }

        mTvLogout.setOnClickListener {
            mLogoutDialog.show()
        }
    }

    private fun logout() {
        Firebase.auth.signOut()
        GthrCollect.prefs?.clearPref()
        requireActivity().finish()
    }
}
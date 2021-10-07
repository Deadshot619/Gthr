package com.gthr.gthrcollect.ui.settings.settingsscreen

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.SettingsFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.askflow.afplaceyourask.AfPlaceYourAskFragment
import com.gthr.gthrcollect.ui.askflow.afplaceyourask.StripeAuth
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.homebottomnav.HomeBottomNavActivity
import com.gthr.gthrcollect.ui.settings.SettingsActivity
import com.gthr.gthrcollect.ui.settings.SettingsViewModel
import com.gthr.gthrcollect.ui.termsandfaq.TermsAndFaqActivity
import com.gthr.gthrcollect.utils.customviews.CustomImageTextButton
import com.gthr.gthrcollect.utils.enums.WebViewType
import com.gthr.gthrcollect.utils.extensions.getUserUID
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.logger.GthrLogger

class SettingsFragment : BaseFragment<SettingsViewModel, SettingsFragmentBinding>() {
    override fun getViewBinding() = SettingsFragmentBinding.inflate(layoutInflater)

  //  override val mViewModel: SettingsViewModel by activityViewModels()

      override val mViewModel: SettingsViewModel by activityViewModels {
        SettingsViewModelFactory(AskFlowRepository())
    }


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
        setupObservers()
        setUpClickListeners()
    }

    private fun setupObservers() {

        mViewModel.stripeAccId.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as SettingsActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as SettingsActivity)?.showProgressBar(false)
                        mViewModel.setPayoutAuth(it.data)
                        if (it.data) {
                            mViewModel.getPayoutLink()
                        } else {
                            startActivityForResult(
                                StripeAuth.getInstance(requireContext(),
                                    stripeAccCreatingURL
                                ),
                                STRIPE_AUTH
                            )
                        }

                    }
                    is State.Failed -> {
                        (activity as SettingsActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.payoutLink.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as SettingsActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as SettingsActivity)?.showProgressBar(false)
                        startActivityForResult(
                            StripeAuth.getInstance(requireContext(), it.data),
                            STRIPE_PAYOUT
                        )

                        GthrLogger.e("payoutLink", it.data)

                    }
                    is State.Failed -> {
                        (activity as SettingsActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

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
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToEditShippingAddressFragment(false))
        }
        mBtnActiveOffers.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToActiveOffersFragment())
        }
        mBtnPayoutInfo.setOnClickListener {
           // startActivity(Intent(requireContext(),AllUsersList::class.java))
            mViewModel.checkStripeAccId(GthrCollect.prefs?.getUserUID())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {

                AfPlaceYourAskFragment.STRIPE_AUTH -> if (resultCode == Activity.RESULT_OK) {
                    val auth = data.getIntExtra(AfPlaceYourAskFragment.STRIPE_AUTH_KEY, -0)
                  //  val code = data.getStringExtra(AfPlaceYourAskFragment.STRIPE_AUTH_CODE)
                    Log.i("STRIPE_AUTH", "onActivityResult: " + auth)
                    if (auth == 1) {
                        mViewModel.setPayoutAuth(true)
                        showToast(getString(R.string.stripe_account_create_success))
                    }
                }
            }
        }
    }


    private fun logout() {
        Firebase.auth.signOut()
        GthrCollect.prefs?.clearPref()
        startActivity(
            HomeBottomNavActivity.getInstance(requireContext(), goToProfileSignUp = true).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        activity?.finish()
    }

    companion object{

        const val STRIPE_AUTH = 100
        const val STRIPE_PAYOUT = 101
        const val stripeAccCreatingURL =
            "https://connect.stripe.com/express/oauth/authorize?client_id=ca_H0t1jArVq47Fpzm3txMvm0v8lVszMewb&state=sv_53124&redirect_uri=https://gthrcollect.page.link/redirect"

    }
}
package com.gthr.gthrcollect.ui.askflow.afbuyingdirectlyplace

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.AfBuyingDirectlyPlaceFragmentBinding
import com.gthr.gthrcollect.databinding.StripeBottomsheetBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.askflow.afplaceyourask.AfPlaceYourAskFragment
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.settings.SettingsActivity
import com.gthr.gthrcollect.ui.termsandfaq.TermsAndFaqActivity
import com.gthr.gthrcollect.utils.constants.StripeConstants
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.SettingFlowType
import com.gthr.gthrcollect.utils.enums.WebViewType
import com.gthr.gthrcollect.utils.extensions.*
import com.gthr.gthrcollect.utils.logger.GthrLogger
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.getPaymentIntentResult
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import kotlinx.coroutines.launch


class AfBuyDirectlyPlaceFragment :
    BaseFragment<AskFlowViewModel, AfBuyingDirectlyPlaceFragmentBinding>() {

    private lateinit var stripe: Stripe
    lateinit var dialog: BottomSheetDialog

    var mBuyerCharge: String? = null
    var mAppFee: String? = null
    var mSellerPayout: String? = null
    var mClientSecret: String? = null
    var mPaymentMethodId: String? = null
    var isPaymentComplete: Boolean = false


    override val mViewModel: AskFlowViewModel by activityViewModels {
        AskFlowViewModelFactory(AskFlowRepository())
    }

    override fun getViewBinding() = AfBuyingDirectlyPlaceFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnNext: CustomSecondaryButton
    private var isTnCCheked = false

    private lateinit var mIvTermsAndConditions: AppCompatImageView
    private lateinit var mTvTermsAndConditions: AppCompatTextView
    private lateinit var mIvBack: ImageView

    private lateinit var mGroup: Group
    private lateinit var mGroupBuy: Group

    private lateinit var mTvRate: AppCompatTextView
    private lateinit var mTvRateValue: AppCompatTextView
    private lateinit var mTvRow1: TextView
    private lateinit var mTvRow1Value: TextView
    private lateinit var mTvRow2: TextView
    private lateinit var mTvRow2Value: TextView
    private lateinit var mTvRow3: TextView
    private lateinit var mTvRow3Value: TextView
    private lateinit var mTvTotalTitle: TextView
    private lateinit var mTvTotalValue: TextView
    private lateinit var mAddressBtn: TextView
    private lateinit var mPayout: TextView

    override fun onBinding() {

        PaymentConfiguration.init(requireContext(), StripeConstants.STRIPE_PUBLISHABLE_KEY)
        stripe = Stripe(requireContext(), StripeConstants.STRIPE_PUBLISHABLE_KEY)

        setHasOptionsMenu(false)
        initViews()
        setUpOnClickListeners()
        setUpObserve()

    }

    override fun onResume() {
        super.onResume()
        mViewModel.checkAddress()
    }

    private fun setUpObserve() {
        mViewModel.mBuyingDirFromSomeOneProPrice.observe(this) {
            mTvRateValue.text = String.format(getString(R.string.rate_common), it)
        }

        mViewModel.shippingTierInfo.observe(viewLifecycleOwner, {
            it?.peekContent()?.let {
                when (it) {
                    is State.Failed -> {
                    }
                    is State.Loading -> {
                    }
                    is State.Success -> {

                        mTvRow1Value.text =
                            it.data.frontEndShippingProcessing.isValidPrice().getAddedRate()
                    }
                }
            }
        })

        mViewModel.paymentData.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)

                        GthrLogger.e("paymentData", it.data.clientSecret!!)

                        mBuyerCharge = it.data.buyerCharge
                        mAppFee = it.data.appFee
                        mSellerPayout = it.data.sellerPayout
                        mClientSecret = it.data.clientSecret

                        showBottomSheet(mClientSecret!!)
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.buyNowData.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)

                        //  showToast(it.data.toString())
                        GthrLogger.e("buyNowData", it.data.toString())

/*                        startActivity(
                            ReceiptDetailActivity.getInstance(
                                requireContext(),
                                ReceiptType.PURCHASED,
                                it.data,
                                getOrderStatusFromRaw(it.data.orderStatus.toString())
                            )
                        )*/
                        activity?.finish()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

    }

    private fun showBottomSheet(paymentIntentClientSecret: String) {

        val binding: StripeBottomsheetBinding
        binding = StripeBottomsheetBinding.inflate(layoutInflater)
        dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)

        binding.cancelButton.setOnClickListener {
            dialog.setCancelable(false)
            dialog.dismiss()
        }

        binding.payButtonId.setOnClickListener {
            binding.cardInputWidget.paymentMethodCreateParams?.let { params ->
                (activity as AskFlowActivity).showProgressBar()
                dialog.setCancelable(false)
                val confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                    params,
                    paymentIntentClientSecret
                )
                stripe.confirmPayment(this, confirmParams)
            }
        }
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        dialog.show()
    }

    private fun setUpOnClickListeners() {

        mIvBack.setOnClickListener {
            findNavController().navigateUp()
        }

        mIvTermsAndConditions.setOnClickListener {
            if (!isTnCCheked) {
                isTnCCheked = !isTnCCheked
                toggleTnC(true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mBtnNext.setState(CustomSecondaryButton.State.BLUE_GRADIENT)
                }
            } else {
                isTnCCheked = !isTnCCheked
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                }
                toggleTnC(false)
            }
        }

        mBtnNext.setOnClickListener {
            if(mViewModel.mAddress?.addresKey==null){
                showToast("please select address")
            }else{
                if (mClientSecret.isNullOrEmpty()) {
                    mViewModel.generateClientSecret(getAskRefKey(), getShippingTeirKey())
                } else {
                    showBottomSheet(mClientSecret!!)
                }
            }
        }

        mTvTermsAndConditions.setOnClickListener {
            startActivity(
                TermsAndFaqActivity.getInstance(
                    requireContext(),
                    WebViewType.TERMS_AND_CONDITION
                )
            )
        }

        mAddressBtn.setOnClickListener {

            startActivityForResult(
                SettingsActivity.getInstance(
                    requireContext(),
                    SettingFlowType.SHIPPING_ADDRESS
                ), AfPlaceYourAskFragment.ADDRESS_REQUEST_CODE
            )

        }

        mPayout.setOnClickListener {

            if(mViewModel.mAddress?.addresKey==null){
                showToast("Please select address!")
            }else{
                if (!isTnCCheked){
                    showToast(getString(R.string.term_condition_note))
                    return@setOnClickListener
                }
                if (mClientSecret.isNullOrEmpty()) {
                    mViewModel.generateClientSecret(getAskRefKey(), getShippingTeirKey())
                } else {
                    showBottomSheet(mClientSecret!!)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {

            if (stripe.isPaymentResult(requestCode, data)) {
                lifecycleScope.launch {
                    runCatching {
                        stripe.getPaymentIntentResult(requestCode, data!!).intent
                    }.fold(
                        onSuccess = { paymentIntent ->

                            val status = paymentIntent.status
                            if (status == StripeIntent.Status.Succeeded) {

                                (activity as AskFlowActivity)?.showProgressBar(true)


                                isPaymentComplete = true
                                mClientSecret = null
                                dialog.setCancelable(false)
                                mPaymentMethodId = paymentIntent.paymentMethod?.id.toString()

                                dialog.dismiss()

                                //    buyNowFunction(mPaymentMethodId.toString())

                                mViewModel.createBuyNow(
                                    getAskRefKey(),
                                    mBuyerCharge,
                                    mViewModel.mAddress?.addresKey.toString(),
                                    "0",
                                    getShippingTeirKey(),
                                    mAppFee,
                                    mPaymentMethodId,
                                    mSellerPayout
                                )


                                showToast("Payment succeeded")

                            } else if (status == StripeIntent.Status.RequiresPaymentMethod) {

                                dialog.setCancelable(false)
                                dialog.dismiss()
                                isPaymentComplete = false
                                mClientSecret = null
                                showToast("Payment failed")
                                (activity as AskFlowActivity)?.showProgressBar(false)

                            }
                        },
                        onFailure = {
                            dialog.setCancelable(false)
                            dialog.dismiss()
                            isPaymentComplete = false
                            mClientSecret = null
                            showToast("Payment failed")
                            (activity as AskFlowActivity)?.showProgressBar(false)
                        }
                    )
                }
            }

            if (requestCode == ADDRESS_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    val address = data.getParcelableExtra<ShippingAddressDomainModel>(KEY_ADDRESS)!!
                    mViewModel.setAddress(address)
                    (activity as AskFlowActivity)?.showProgressBar(false)

                    //  showToast(address.addresKey.toString())
                    GthrLogger.i("dsfbvjudrs", "onActivityResult: " + address.addresKey)
                }
            }
        }


    }


    private fun toggleTnC(toggleOn: Boolean) {
        if (toggleOn) {
            mIvTermsAndConditions.setImageResource(R.drawable.ic_terms_and_conditions_blue)
        } else {
            mIvTermsAndConditions.setImageResource(R.drawable.ic_terms_and_conditions)
        }
    }

    private fun initViews() {
        mViewBinding.run {
            mIvBack = ivBack
            mBtnNext = btnNext
            mIvTermsAndConditions = ivTermsAndConditions
            mTvTermsAndConditions = tvTermsAndConditions
            mGroup = group
            mGroupBuy = groupBuy
            mTvRate = tvRate
            mTvRateValue = tvRateValue
            mTvRow1 = tvSellingFee
            mTvRow1Value = tvSellingFeeValue
            mTvRow2 = tvPaymentProcessing
            mTvRow2Value = tvPaymentProcessingValue
            mTvRow3 = tvShippingReimbursement
            mTvRow3Value = tvShippingReimbursementValue
            mAddressBtn = tvAddress
            mTvTotalTitle = tvTotal
            mTvTotalValue = tvTotalValue

            mPayout = tvPayout
        }

        mTvTotalValue.text = String.format(
            getString(R.string.text_price_value),
            mViewModel.totalPaymentRate.toString().isValidPrice()
        )

        mGroup.visible()
        mGroupBuy.gone()
        mTvRate.text = getString(R.string.text_price)
        mTvRow1.text = getString(R.string.text_purchase_shipping)
        mTvRow2.text = getString(R.string.text_sales_tax)
        mTvRow2Value.text = getString(R.string.rate_positive, mViewModel.salesTax)
        mTvRow2.gone()
        mTvRow2Value.gone()

        mTvRow3.gone()
        mTvRow3Value.gone()
        mPayout.invisible()
        mBtnNext.text = getString(R.string.text_pay_now)
        mViewBinding.executePendingBindings()

    }

    fun String.getAddedRate(): String = "+$this"
    fun String.getSubtractedRate(): String = "-$this"

    fun getAskRefKey(): String {
        val ref = mViewModel.productDisplayModel?.forsaleItemNodel?.askRefKey.toString()
        GthrLogger.d("ref", "$ref")
        return ref
    }

    fun getShippingTeirKey(): String {
        return (mViewModel.shippingTierInfo.value?.peekContent() as State.Success).data.tierLevel.toString()
    }


    companion object {

        const val KEY_ADDRESS = "address"
        const val ADDRESS_REQUEST_CODE = 123


        fun getReturnIntent(shippingAddressDomainModel: ShippingAddressDomainModel) =
            Intent().apply {
                putExtra(KEY_ADDRESS, shippingAddressDomainModel)
            }
    }

}
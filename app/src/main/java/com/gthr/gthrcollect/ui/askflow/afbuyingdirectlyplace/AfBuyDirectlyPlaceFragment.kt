package com.gthr.gthrcollect.ui.askflow.afbuyingdirectlyplace

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.AfBuyingDirectlyPlaceFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ReceiptDomainModel
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.receiptdetail.ReceiptDetailActivity
import com.gthr.gthrcollect.ui.termsandfaq.TermsAndFaqActivity
import com.gthr.gthrcollect.utils.customviews.CustomDeliveryButton
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.enums.ReceiptType
import com.gthr.gthrcollect.utils.enums.WebViewType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.isValidPrice
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.extensions.visible

class AfBuyDirectlyPlaceFragment :
    BaseFragment<AskFlowViewModel, AfBuyingDirectlyPlaceFragmentBinding>() {

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
        setHasOptionsMenu(false)
        initViews()
        setUpOnClickListeners()
        setUpObserve()
    }

    private fun setUpObserve() {

        mViewModel.askPrice.observe(viewLifecycleOwner) {
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
                        when ((requireActivity() as AskFlowActivity).getAskFlowType()) {
                            AskFlowType.SELL, AskFlowType.COLLECT -> {
                                mTvRow3Value.text =
                                    it.data.frontEndShippingProcessing.isValidPrice().getAddedRate()
                            }
                            AskFlowType.BUY_DIRECTLY_FROM_SOMEONE -> {
                                mTvRow1Value.text =
                                    it.data.frontEndShippingProcessing.isValidPrice().getAddedRate()
                            }
                        }
                    }
                }
            }
        })
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
            startActivity(
                ReceiptDetailActivity.getInstance(
                    requireContext(),
                    ReceiptType.PURCHASED,
                    ReceiptDomainModel(),
                    CustomDeliveryButton.OrderStatus.ORDERED
                )
            )
            activity?.finish()
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

        }

        mPayout.setOnClickListener {
            mViewModel.checkStripeAccId(GthrCollect.prefs?.collectionInfoModel?.userRefKey)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                STRIPE_AUTH -> if (resultCode == Activity.RESULT_OK) {
                    val auth = data.getIntExtra(STRIPE_AUTH_KEY,-0)
                    Log.i("STRIPE_AUTH", "onActivityResult: "+auth)
                    if (auth==1){
                        mViewModel.setPayoutAuth(true)
                        showToast(getString(R.string.stripe_account_create_success))
                    }
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
            mViewModel.totalPayoutRate.toString().isValidPrice()
        )

        mGroup.visible()
        mGroupBuy.gone()
        mTvRate.text = getString(R.string.text_price)
        mTvRateValue.text = "$55.00"
        mTvRow1.text = getString(R.string.text_purchase_shipping)
        mTvRow1Value.text = "+0.0"
        mTvRow2.text = getString(R.string.text_sales_tax)
        mTvRow2Value.text = "+0.0"
        mTvRow3.gone()
        mTvRow3Value.gone()
        mBtnNext.text = getString(R.string.text_accept)
        mViewBinding.executePendingBindings()
    }

    fun String.getAddedRate(): String = "+$this"
    fun String.getSubtractedRate(): String = "-$this"


    companion object{

        const val KEY_ADDRESS = "address"
        const val STRIPE_AUTH = 100
        const val STRIPE_AUTH_KEY = "AUTH"

        fun getReturnIntent(shippingAddressDomainModel : ShippingAddressDomainModel) =  Intent().apply {
            putExtra(KEY_ADDRESS,shippingAddressDomainModel)
        }
    }
}
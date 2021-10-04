package com.gthr.gthrcollect.ui.askflow.afplaceyourask

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
import com.gthr.gthrcollect.databinding.AfPlaceYourAskFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ReceiptDomainModel
import com.gthr.gthrcollect.model.domain.ShippingAddressDomainModel
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.receiptdetail.ReceiptDetailActivity
import com.gthr.gthrcollect.ui.settings.SettingsActivity
import com.gthr.gthrcollect.ui.termsandfaq.TermsAndFaqActivity
import com.gthr.gthrcollect.utils.customviews.CustomDeliveryButton
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.*
import com.gthr.gthrcollect.utils.extensions.*
import com.gthr.gthrcollect.utils.getProductType
import com.gthr.gthrcollect.utils.helper.getEditionTypeFromRowType
import com.gthr.gthrcollect.utils.helper.getLanguageDomainModelFromKey
import com.gthr.gthrcollect.utils.logger.GthrLogger

class AfPlaceYourAskFragment : BaseFragment<AskFlowViewModel, AfPlaceYourAskFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels {
        AskFlowViewModelFactory(AskFlowRepository())
    }

    override fun getViewBinding() = AfPlaceYourAskFragmentBinding.inflate(layoutInflater)


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

    //Buylist
    private lateinit var mTvBuyListValue: AppCompatTextView
    private lateinit var mTvTotalBuyListValue: TextView
    private lateinit var mPayout: TextView

    override fun onBinding() {
        setHasOptionsMenu(false)
        initViews()
        setUpOnClickListeners()
        setUpObserve()
    }

    private fun setUpObserve() {

        mViewModel.insertCollectionRDB.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        mViewModel.setCollectionItemKey(it.data)
                        mViewModel.uploadFrontImage()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.frontImageUpload.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        mViewModel.setFrontImageDownloadUrl(it.data)
                        mViewModel.uploadBackImage()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.backImageUpload.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        mViewModel.setBackImageDownloadUrl(it.data)
                        mViewModel.insertAsk()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }
        mViewModel.insertAskRDB.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        mViewModel.setAskId(it.data)
                        mViewModel.updateCollection()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.updateCollectionRDB.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        if (mViewModel.productDisplayModel?.lowestAskCost!! > mViewModel.askPrice.value!! &&
                            mViewModel.productDisplayModel?.lowestAskID != mViewModel.mAskId
                        ) {
                            mViewModel.updateProductForAsk()
                        } else {
                            goToReceiptPage()
                            (activity as AskFlowActivity)?.finish()
                        }
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.updateProductForAskRDB.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        goToReceiptPage()
                        activity?.finish()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.stripeAccId.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        mViewModel.setPayoutAuth(it.data)
                        if (it.data) {
                            mViewModel.getPayoutLink()
                        } else {
                            startActivityForResult(
                                StripeAuth.getInstance(requireContext(), stripeAccCreatingURL),
                                STRIPE_AUTH
                            )
                        }

                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.stripeAccStatus.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        mViewModel.setPayoutAuth(it.data)
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }


        mViewModel.payoutLink.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        startActivityForResult(
                            StripeAuth.getInstance(requireContext(), it.data),
                            STRIPE_PAYOUT
                        )

                        GthrLogger.e("payoutLink", it.data)

                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }


        mViewModel.askPrice.observe(viewLifecycleOwner) {
            mTvRateValue.text = String.format(getString(R.string.rate_common), it)
        }

        mViewModel.buyListPrice.observe(viewLifecycleOwner) {
            mTvBuyListValue.text = String.format(getString(R.string.rate_common), it)
            mTvTotalBuyListValue.text = String.format(getString(R.string.rate_common), it)
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

        //Buylist observers

        mViewModel.insertBidRDB.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        mViewModel.setBidId(it.data)
                        mViewModel.insertBuy()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.insertBuyRDB.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        mViewModel.setBuyKey(it.data)
                        if (mViewModel.productDisplayModel?.highestBidCost!! < mViewModel.buyListPrice.value!! && mViewModel.mBidId != mViewModel.productDisplayModel?.highestBidID)
                            mViewModel.updateProductForBid()
                        else
                            (activity as AskFlowActivity)?.finish()
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.updateProductForBidRDB.observe(viewLifecycleOwner) {
            it.contentIfNotHandled.let {
                when (it) {
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        findNavController().navigate(AfPlaceYourAskFragmentDirections.actionAfPlaceYourAskFragmentToAfBuyListDetailsFragment())
                    }
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }


        //Edit
        mViewModel.editAsk.observe(this, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        goToReceiptPage()
                    }
                }
            }
        })
        mViewModel.editBid.observe(this, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Failed -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        showToast(it.message)
                    }
                    is State.Loading -> {
                        (activity as AskFlowActivity)?.showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity)?.showProgressBar(false)
                        findNavController().navigate(AfPlaceYourAskFragmentDirections.actionAfPlaceYourAskFragmentToAfBuyListDetailsFragment())
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
            val askFlowType = (requireActivity() as AskFlowActivity).getAskFlowType()
            when (askFlowType) {
                AskFlowType.BUY -> {
                    if (mViewModel.isEdit)
                        mViewModel.editBid(
                            mViewModel.productDisplayModel!!,
                            mViewModel.buyListPrice.value!!
                        )
                    else
                        mViewModel.insertBid()
                }
                else -> {
                    if (mViewModel.mAddress != null)
                        if (mViewModel.mIsPayoutAuth) {
                            if (askFlowType == AskFlowType.COLLECT && mViewModel.isEdit) {
                                mViewModel.setCollectionItemKey(mViewModel.productDisplayModel?.forsaleItemNodel?.collectionItemRefKey.toString())
                                mViewModel.uploadFrontImage()
                            } else if (mViewModel.isEdit)
                                mViewModel.editAsk(
                                    mViewModel.productDisplayModel!!,
                                    mViewModel.askPrice.value!!
                                )
                            else
                                mViewModel.insertCollection()
                        } else {
                            //    mViewModel.checkStripeAccId(GthrCollect.prefs?.collectionInfoModel?.userRefKey)
                            showToast(getString(R.string.stripe_acc_creating_msg))
                        }
                    else
                        showToast("Please select address")
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
                ), ADDRESS_REQUEST_CODE
            )
        }

        mPayout.setOnClickListener {
            mViewModel.checkStripeAccId(GthrCollect.prefs?.getUserUID())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                ADDRESS_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                    val address = data.getParcelableExtra<ShippingAddressDomainModel>(KEY_ADDRESS)!!
                    mViewModel.setAddress(address)


                    Log.i("dsfbvjudrs", "onActivityResult: " + address)
                }

                STRIPE_AUTH -> if (resultCode == Activity.RESULT_OK) {
                    val auth = data.getIntExtra(STRIPE_AUTH_KEY, -0)
                    val code = data.getStringExtra(STRIPE_AUTH_CODE)
                    Log.i("STRIPE_AUTH", "onActivityResult: " + auth)
                    if (auth == 1) {
                        //    mViewModel.createStripeAccount(code!!)
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

            mTvBuyListValue = tvBuyValue
            mTvTotalBuyListValue = tvBuyTotalValue
            mPayout = tvPayout
        }

        mTvTotalValue.text = String.format(
            getString(R.string.text_price_value),
            mViewModel.totalPayoutRate.toString().isValidPrice()
        )

        when ((requireActivity() as AskFlowActivity).getAskFlowType()) {
            AskFlowType.BUY -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mBtnNext.setState(CustomSecondaryButton.State.BLUE_GRADIENT)
                }
                mBtnNext.text = getString(R.string.text_add_to_buylist_ask_flow)
                mGroup.gone()
                mGroupBuy.visible()
            }
            AskFlowType.BUY_DIRECTLY_FROM_SOMEONE -> {
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
            else -> {
                mBtnNext.text = getString(R.string.text_place_your_ask)
                mTvRow1Value.text =
                    String.format(getString(R.string.rate_negative), mViewModel.sellingFee)
                "-${mViewModel.sellingFee}"
                mTvRow2Value.text =
                    String.format(getString(R.string.rate_negative), mViewModel.paymentProcessing)
                mTvRow3Value.text = "+0.00"
                mTvTotalTitle.text = getString(R.string.text_total_payout_usd)
                mGroup.visible()
                mGroupBuy.gone()
            }
        }
    }

    private fun goToReceiptPage() {
        val receiptDomainModel = if (mViewModel.isEdit)
            ReceiptDomainModel(
                orderStatus = CustomDeliveryButton.OrderStatus.ASK_PLACED,
                totalAskPrice = mViewModel.askPrice.value,
                shippingReimbursement = mViewModel.shippingProcessing,
                objectID = mViewModel.productDisplayModel?.objectID,
                productType = mViewModel.productType,
                lang = mViewModel.productDisplayModel?.forsaleItemNodel?.language,
                condition = mViewModel.productDisplayModel?.forsaleItemNodel?.condition,
                edition = getEditionTypeFromRowType(mViewModel.productDisplayModel?.forsaleItemNodel?.edition?:""),
                itemRefKey = mViewModel.productDisplayModel?.refKey.toString(),
                imageUrl = if ((requireActivity() as AskFlowActivity).getAskFlowType() == AskFlowType.COLLECT)
                    mViewModel.mFrontImageDownloadUrl
                else
                    mViewModel.productDisplayModel?.forsaleItemNodel?.frontImageURL
            )
        else
            ReceiptDomainModel(
                orderStatus = CustomDeliveryButton.OrderStatus.ASK_PLACED,
                totalAskPrice = mViewModel.askPrice.value,
                shippingReimbursement = mViewModel.shippingProcessing,
                objectID = mViewModel.productDisplayModel?.objectID,
                productType = mViewModel.productType,
                lang = mViewModel.selectedLanguage.value?.peekContent(),
                condition = mViewModel.selectedCondition.value?.peekContent(),
                edition = mViewModel.selectedEdition.value?.peekContent(),
                itemRefKey = mViewModel.productDisplayModel?.refKey,
                imageUrl = mViewModel.mFrontImageDownloadUrl
            )

        startActivity(
            ReceiptDetailActivity.getInstance(
                requireContext(),
                ReceiptType.ASK_PLACED,
                receiptDomainModel
            )
        )
        activity?.finish()
    }


    fun String.getAddedRate(): String = "+$this"
    fun String.getSubtractedRate(): String = "-$this"

    companion object {
        const val ADDRESS_REQUEST_CODE = 123
        const val KEY_ADDRESS = "address"
        const val STRIPE_AUTH = 100
        const val STRIPE_PAYOUT = 101
        const val STRIPE_AUTH_KEY = "AUTH"
        const val STRIPE_AUTH_CODE = "AUTH_CODE"
        const val STRIPE_URL = "url"
        const val stripeAccCreatingURL =
            "https://connect.stripe.com/express/oauth/authorize?client_id=ca_H0t1jArVq47Fpzm3txMvm0v8lVszMewb&state=sv_53124&redirect_uri=https://gthrcollect.page.link/redirect"
        fun getReturnIntent(shippingAddressDomainModel: ShippingAddressDomainModel) =
            Intent().apply {
                putExtra(KEY_ADDRESS, shippingAddressDomainModel)
            }

    }

}
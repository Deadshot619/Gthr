package com.gthr.gthrcollect.ui.receiptdetail.purchasedetails

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.ReceiptRepository
import com.gthr.gthrcollect.databinding.PurchaseDetailsFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ReceiptDomainModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.receiptdetail.ReceiptDetailViewModel
import com.gthr.gthrcollect.ui.receiptdetail.ReceiptDetailViewModelFactory
import com.gthr.gthrcollect.utils.constants.CalendarConstants
import com.gthr.gthrcollect.utils.constants.MailConstants
import com.gthr.gthrcollect.utils.customviews.CustomDeliveryButton
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.ConditionType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.enums.ReceiptType
import com.gthr.gthrcollect.utils.extensions.*
import com.gthr.gthrcollect.utils.getProductCategory
import com.gthr.gthrcollect.utils.helper.*
import io.ktor.util.*
import java.text.SimpleDateFormat
import java.util.*

class PurchaseDetailsFragment :
    BaseFragment<ReceiptDetailViewModel, PurchaseDetailsFragmentBinding>() {

    override val mViewModel: ReceiptDetailViewModel by activityViewModels {
        ReceiptDetailViewModelFactory(ReceiptRepository())
    }

    override fun getViewBinding() = PurchaseDetailsFragmentBinding.inflate(layoutInflater)

    private val args by navArgs<PurchaseDetailsFragmentArgs>()

    private lateinit var mReceiptType: ReceiptType
    private lateinit var mReceiptModel: ReceiptDomainModel
    private lateinit var mProductCategory: ProductCategory
    private lateinit var mOrderStatus: CustomDeliveryButton.OrderStatus

    private lateinit var mBtnReportIssue: CustomSecondaryButton
    private lateinit var mBtnConfirmReceived: CustomSecondaryButton
    private lateinit var mCdbOrderStatus: CustomDeliveryButton

    private lateinit var mTvUserName: TextView
    private lateinit var mIvUserProfilePic: ImageView
    private lateinit var mTvProductName: TextView
    private lateinit var mTvSetTitle: TextView
    private lateinit var mTvSetValue: TextView
    private lateinit var mTvHashNumberValue: TextView
    private lateinit var mTvRarityValue: TextView
    private lateinit var mTvConditionValue: TextView
    private lateinit var mTvCardLangValue: TextView
    private lateinit var mTvCardConditionTitle: TextView
    private lateinit var mTvCardConditionValue: TextView
    private lateinit var mTvCardEdition: TextView

    private lateinit var mTvSummaryTitle: TextView
    private lateinit var mTvSummaryR1C1: TextView
    private lateinit var mTvSummaryR1C2: TextView
    private lateinit var mTvSummaryR2C1: TextView
    private lateinit var mTvSummaryR2C2: TextView
    private lateinit var mTvSummaryR3C1: TextView
    private lateinit var mTvSummaryR3C2: TextView
    private lateinit var mTvSummaryR4C1: TextView
    private lateinit var mTvSummaryR4C2: TextView
    private lateinit var mTvTotalTitle: TextView
    private lateinit var mTvTotalValue: TextView

    private lateinit var mTvPaymentTitle: TextView
    private lateinit var mTvPaymentId: TextView
    private lateinit var mIvPaymentLogo: ImageView
    private lateinit var mTvOrderedDate: TextView

    private lateinit var llNumberRarity: LinearLayoutCompat
    private lateinit var llRarity: LinearLayoutCompat
    private lateinit var llCondition: LinearLayoutCompat
    private lateinit var llLangEdCond: LinearLayoutCompat
    private lateinit var mProductImage: AppCompatImageView

    private lateinit var mGroupAskFlow: Group

    private val mConfirmReceivedDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_confirm_received_title))
            .setMessage(getString(R.string.dialog_confirm_received_body))
            .setPositiveButton(getString(R.string.dialog_btn_yes)) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_btn_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    private val mConfirmShippedDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_confirm_shipped_title))
            .setMessage(getString(R.string.dialog_confirm_shipped_body))
            .setPositiveButton(getString(R.string.dialog_btn_yes)) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_btn_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    override fun onBinding() {
        mReceiptType = args.receiptType
        mReceiptModel = args.receiptDomainModel
        mProductCategory = getProductCategory(mReceiptModel.productType!!)!!
        mOrderStatus = args.buttonType

        initViews()
        setUpCardTypeViews(mProductCategory, mReceiptModel)
        setUpReceiptTypeViews(mReceiptType)
        initClickListeners()
        setUpObservers()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnReportIssue = btnReportIssue
            mBtnConfirmReceived = btnConfirmReceived
            mCdbOrderStatus = cdbOrderStatus

            mTvUserName = tvUserName
            mIvUserProfilePic = ivUserProfilePic
            mTvProductName = tvTitle
            mTvSetTitle = tvSet
            mTvSetValue = tvSetValue
            mTvHashNumberValue = tvNumberValue
            mTvRarityValue = tvRarityValue
            mTvConditionValue = tvConditionValue
            mTvCardLangValue = tvLangValue
            mTvCardConditionTitle = tvCardConditionTitle
            mTvCardConditionValue = tvCardConditionValue
            mTvCardEdition = tvEdition
            mProductImage = ivProductImage

            mTvSummaryTitle = tvPurchaseSummary
            mTvSummaryR1C1 = tvSummaryR1c1
            mTvSummaryR1C2 = tvSummaryR1c2
            mTvSummaryR2C1 = tvSummaryR2c1
            mTvSummaryR2C2 = tvSummaryR2c2
            mTvSummaryR3C1 = tvSummaryR3c1
            mTvSummaryR3C2 = tvSummaryR3c2
            mTvSummaryR4C1 = tvSummaryR4c1
            mTvSummaryR4C2 = tvSummaryR4c2
            mTvTotalTitle = tvTotal
            mTvTotalValue = tvTotalValue

            mTvPaymentTitle = tvPayment
            mTvPaymentId = tvPaymentId
            mIvPaymentLogo = ivPaymentLogo
            mTvOrderedDate = tvOrderedDate

            this@PurchaseDetailsFragment.llNumberRarity = llNumberRarity
            this@PurchaseDetailsFragment.llRarity = llRarity
            this@PurchaseDetailsFragment.llCondition = llCondition
            this@PurchaseDetailsFragment.llLangEdCond = llLangEdCond

            mGroupAskFlow = groupAskFlow

            initProgressBar(layoutProgress)
        }
    }

    private fun initClickListeners() {
        mBtnReportIssue.setOnClickListener {
            context?.sendMail(
                emailTo = MailConstants.SUPPORT_MAIL_ID,
                subject = MailConstants.SUPPORT_MAIL_SUBJECT
            )
        }

        mBtnConfirmReceived.setOnClickListener {
            when (args.receiptType) {
                ReceiptType.PURCHASED ->
                    if (mOrderStatus == CustomDeliveryButton.OrderStatus.ORDERED)
                        activity?.finish()
                    else
                        mConfirmReceivedDialog.show()
                ReceiptType.SOLD ->
                    mConfirmShippedDialog.show()
                ReceiptType.ASK_PLACED ->
                    activity?.finish()
            }
        }
        mProductImage.setOnClickListener {
            startActivity(FullProductImage.getInstance(requireContext(), mReceiptModel.imageUrl))
        }
    }

    private fun setUpReceiptTypeViews(receiptType: ReceiptType) {
        when (receiptType) {
            ReceiptType.PURCHASED -> {
                mTvSummaryTitle.text = getString(R.string.text_purchase_summary)
                mTvSummaryR1C1.text = getString(R.string.text_sale_price)
                mTvSummaryR1C2.text = "$22.00"
                mTvSummaryR2C1.text = getString(R.string.text_purchase_shipping)
                mTvSummaryR2C2.text = "+ \$2.95"
                mTvSummaryR3C1.text =
                    String.format(getString(R.string.text_purchase_sales_tax), "7.5")
                mTvSummaryR3C2.text = "+ \$0.86"
                mTvSummaryR4C1.gone()
                mTvSummaryR4C2.gone()

                if (mOrderStatus == CustomDeliveryButton.OrderStatus.ORDERED) {
                    mCdbOrderStatus.setType(CustomDeliveryButton.OrderStatus.ORDERED)
                    mBtnReportIssue.gone()
                    mBtnConfirmReceived.text = getString(R.string.finish)
                    mBtnConfirmReceived.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.ic_next_arrow, 0
                    )
                } else {
                    mCdbOrderStatus.setType(CustomDeliveryButton.OrderStatus.DELIVERED)
                    mBtnConfirmReceived.text =
                        getString(R.string.text_btn_purchase_detail_confirm_received)
                }
            }
            ReceiptType.SOLD -> {
                mTvSummaryTitle.text = getString(R.string.text_sold_summary)
                mTvSummaryR1C1.text = getString(R.string.text_sale_price)
                mTvSummaryR1C2.text = "$22.00"
                mTvSummaryR2C1.text = String.format(getString(R.string.text_selling_fee), "8.5")
                mTvSummaryR2C2.text = "- \$5.23"
                mTvSummaryR3C1.text =
                    String.format(getString(R.string.text_payment_processing), "2.9")
                mTvSummaryR3C2.text = "- \$1.60"
                mTvSummaryR4C1.text = getString(R.string.text_shipping_reimbursement)
                mTvSummaryR4C2.text = "+ \$0.55"

                if (mOrderStatus == CustomDeliveryButton.OrderStatus.ASK_PLACED) {
                    mCdbOrderStatus.setType(CustomDeliveryButton.OrderStatus.ASK_PLACED)
                    mBtnReportIssue.gone()
                    mBtnConfirmReceived.text = getString(R.string.finish)
                    mBtnConfirmReceived.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.ic_next_arrow, 0
                    )
                } else {
                    mCdbOrderStatus.setType(CustomDeliveryButton.OrderStatus.PENDING)
                    mBtnConfirmReceived.text = getString(R.string.dialog_confirm_shipped_title)
                }
            }
            ReceiptType.ASK_PLACED -> {
                mGroupAskFlow.gone()

                mTvUserName.text = GthrCollect.prefs?.collectionInfoModel?.collectionDisplayName
                mViewModel.setProductImage(GthrCollect.prefs?.collectionInfoModel?.profileImage.toString())
                mIvUserProfilePic.setProfileImage(GthrCollect.prefs?.collectionInfoModel?.profileImage.toString())

                mTvSummaryTitle.text = getString(R.string.text_ask_summary)
                mTvSummaryR1C1.text = getString(R.string.text_sale_price)
                mTvSummaryR1C2.text =
                    String.format(getString(R.string.rate_common), mReceiptModel.totalAskPrice)
                mTvSummaryR2C1.text = getString(R.string.text_selling_fee_8_5_ask_flow)
                mTvSummaryR2C2.text = String.format(
                    getString(R.string.rate_negative),
                    mReceiptModel.totalAskPrice.getSellingFee()
                )
                mTvSummaryR3C1.text = getString(R.string.text_payment_processing_2_9_ask_flow)
                mTvSummaryR3C2.text = String.format(
                    getString(R.string.rate_negative),
                    mReceiptModel.totalAskPrice.getPaymentProcessing()
                )
                mTvSummaryR4C1.text = getString(R.string.text_shipping_reimbursement)
                mTvSummaryR4C2.text = String.format(
                    getString(R.string.rate_positive),
                    mReceiptModel.shippingReimbursement ?: 0.0
                )
                mTvTotalTitle.text = getString(R.string.text_total_payout_usd)

                val totalPayout = (mReceiptModel.totalAskPrice ?: 0.0) -
                        mReceiptModel.totalAskPrice.getSellingFee() -
                        mReceiptModel.totalAskPrice.getPaymentProcessing() +
                        (mReceiptModel.shippingReimbursement ?: 0.0)
                mTvTotalValue.text = String.format(getString(R.string.rate_common), totalPayout)

                mProductImage.setImage(mReceiptModel.imageUrl.toString())
                mTvPaymentTitle.text = getString(R.string.text_payout)
                mTvPaymentId.text = GthrCollect.prefs?.userInfoModel?.emailId.toString()
                mIvPaymentLogo.gone()
                mTvOrderedDate.text = SimpleDateFormat(
                    CalendarConstants
                        .MONTH_DATE_YEAR, Locale.getDefault()
                ).format(Calendar.getInstance().time)

                mCdbOrderStatus.setType(CustomDeliveryButton.OrderStatus.ASK_PLACED)
                mBtnReportIssue.gone()
                mBtnConfirmReceived.text = getString(R.string.finish)
                mBtnConfirmReceived.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_next_arrow, 0
                )
            }
        }
    }

    private fun setUpCardTypeViews(type: ProductCategory, receiptDomainModel: ReceiptDomainModel) {
        when (type) {
            ProductCategory.CARDS -> {
                llCondition.gone()
                mTvCardLangValue.text = when (receiptDomainModel.productType) {
                    ProductType.POKEMON -> getPokemonLanguageDomainModel(
                        receiptDomainModel.lang ?: 0
                    ).abbreviatedName
                    ProductType.MAGIC_THE_GATHERING -> getMTGLanguage(
                        receiptDomainModel.lang ?: 0
                    ).abbreviatedName
                    /*ProductType.YUGIOH*/else -> getYugiohLanguageDomainModel(
                        receiptDomainModel.lang ?: 0
                    ).abbreviatedName
                }
                val condition = getConditionFromDisplayName(receiptDomainModel.condition.toString())
                mTvCardConditionTitle.text =
                    if (condition.type == ConditionType.UG) getString(R.string.raw) else condition.type.title.toUpperCase()
                mTvCardConditionValue.text = condition.abbreviatedName
                mTvCardEdition.text = receiptDomainModel.edition
            }
            ProductCategory.SEALED -> {
                llNumberRarity.gone()
                llLangEdCond.gone()
            }
            ProductCategory.TOYS -> {   //Funko
                llRarity.gone()
                llLangEdCond.gone()
                mTvSetTitle.text = getString(R.string.text_product_license) //Set License Text
            }
        }
    }

    private fun setUpObservers() {
        mViewModel.productDetailModel.observe(viewLifecycleOwner, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Failed -> {
                        showProgressBar(false)
                    }
                    is State.Loading -> {
                        showProgressBar()
                    }
                    is State.Success -> {
                        showProgressBar(false)
                        it.data?.let {
                            mTvProductName.text = it.name
                            when (it.productType) {
                                ProductType.MAGIC_THE_GATHERING,
                                ProductType.YUGIOH,
                                ProductType.POKEMON -> {
                                    mTvSetValue.text = it.description
                                    mTvHashNumberValue.text = it.productNumber
                                    mTvRarityValue.text = it.rarity
                                }
                                ProductType.FUNKO -> {
                                    mTvSetValue.text = it.description
                                    mTvHashNumberValue.text = it.productNumber
                                }
                                ProductType.SEALED_POKEMON,
                                ProductType.SEALED_YUGIOH,
                                ProductType.SEALED_MTG -> {
                                    mTvSetValue.text = it.description
                                }
                                null -> {
                                }
                            }
                        }
                    }
                }
            }
        })
    }
}
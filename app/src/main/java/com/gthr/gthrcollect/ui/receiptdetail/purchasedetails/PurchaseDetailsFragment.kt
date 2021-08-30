package com.gthr.gthrcollect.ui.receiptdetail.purchasedetails

import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.PurchaseDetailsFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.constants.MailConstants
import com.gthr.gthrcollect.utils.customviews.CustomDeliveryButton
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ReceiptType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.sendMail

class PurchaseDetailsFragment :
    BaseFragment<PurchaseDetailsViewModel, PurchaseDetailsFragmentBinding>() {

    override val mViewModel: PurchaseDetailsViewModel by viewModels()
    override fun getViewBinding() = PurchaseDetailsFragmentBinding.inflate(layoutInflater)

    private val args by navArgs<PurchaseDetailsFragmentArgs>()

    private lateinit var mReceiptType: ReceiptType
    private lateinit var mProductCategory: ProductCategory
    private lateinit var mButtonType: CustomDeliveryButton.Type

    private lateinit var mBtnReportIssue: CustomSecondaryButton
    private lateinit var mBtnConfirmReceived: CustomSecondaryButton
    private lateinit var mCdbOrderStatus: CustomDeliveryButton

    private lateinit var mTvSetTitle: TextView
    private lateinit var mTvSetValue: TextView
    private lateinit var mTvHashNumberValue: TextView
    private lateinit var mTvRarityValue: TextView
    private lateinit var mTvConditionValue: TextView
    private lateinit var mTvLangValue: TextView
    private lateinit var mTvPsaValue: TextView
    private lateinit var mTvCardTypeValue: TextView

    private lateinit var mTvSummaryTitle: TextView
    private lateinit var mTvSummaryR1C1: TextView
    private lateinit var mTvSummaryR1C2: TextView
    private lateinit var mTvSummaryR2C1: TextView
    private lateinit var mTvSummaryR2C2: TextView
    private lateinit var mTvSummaryR3C1: TextView
    private lateinit var mTvSummaryR3C2: TextView
    private lateinit var mTvSummaryR4C1: TextView
    private lateinit var mTvSummaryR4C2: TextView


    private lateinit var llNumberRarity: LinearLayoutCompat
    private lateinit var llRarity: LinearLayoutCompat
    private lateinit var llCondition: LinearLayoutCompat
    private lateinit var llDetails: LinearLayoutCompat
    private lateinit var mProductImage: AppCompatImageView


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
        mProductCategory = args.productCategory
        mButtonType = args.buttonType

        initViews()
        setUpCardTypeViews(ProductCategory.CARDS)
        setUpReceiptTypeViews(mReceiptType)
        initClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnReportIssue = btnReportIssue
            mBtnConfirmReceived = btnConfirmReceived
            mCdbOrderStatus = cdbOrderStatus

            mTvSetTitle = tvSet
            mTvSetValue = tvSetValue
            mTvHashNumberValue = tvNumberValue
            mTvRarityValue = tvRarityValue
            mTvConditionValue = tvConditionValue
            mTvLangValue = tvLangValue
            mTvPsaValue = tvPsaValue
            mTvCardTypeValue = tvCardType
            mProductImage =ivProductImage

            mTvSummaryTitle = tvPurchaseSummary
            mTvSummaryR1C1 = tvSummaryR1c1
            mTvSummaryR1C2 = tvSummaryR1c2
            mTvSummaryR2C1 = tvSummaryR2c1
            mTvSummaryR2C2 = tvSummaryR2c2
            mTvSummaryR3C1 = tvSummaryR3c1
            mTvSummaryR3C2 = tvSummaryR3c2
            mTvSummaryR4C1 = tvSummaryR4c1
            mTvSummaryR4C2 = tvSummaryR4c2

            this@PurchaseDetailsFragment.llNumberRarity = llNumberRarity
            this@PurchaseDetailsFragment.llRarity = llRarity
            this@PurchaseDetailsFragment.llCondition = llCondition
            this@PurchaseDetailsFragment.llDetails = llDetails
        }
    }

    private fun initClickListeners() {
        mBtnReportIssue.setOnClickListener {
            context?.sendMail(emailTo = MailConstants.SUPPORT_MAIL_ID, subject = MailConstants.SUPPORT_MAIL_SUBJECT)
        }

        mBtnConfirmReceived.setOnClickListener {
            when (args.receiptType) {
                ReceiptType.PURCHASED ->
                    if (mButtonType == CustomDeliveryButton.Type.ORDERED)
                        activity?.finish()
                    else
                        mConfirmReceivedDialog.show()
                ReceiptType.SOLD ->
                    if (mButtonType == CustomDeliveryButton.Type.ASK_PLACED)
                        activity?.finish()
                    else
                        mConfirmShippedDialog.show()
            }
        }
        mProductImage.setOnClickListener{
            startActivity(FullProductImage.getInstance(requireContext(),"vgfgg"))
        }
    }

    private fun setUpReceiptTypeViews(receiptType: ReceiptType){
        when(receiptType) {
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

                if (mButtonType == CustomDeliveryButton.Type.ORDERED) {
                    mCdbOrderStatus.setType(CustomDeliveryButton.Type.ORDERED)
                    mBtnReportIssue.gone()
                    mBtnConfirmReceived.text = getString(R.string.finish)
                    mBtnConfirmReceived.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_next_arrow,
                        0
                    )
                } else {
                    mCdbOrderStatus.setType(CustomDeliveryButton.Type.DELIVERED)
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

                if (mButtonType == CustomDeliveryButton.Type.ASK_PLACED) {
                    mCdbOrderStatus.setType(CustomDeliveryButton.Type.ASK_PLACED)
                    mBtnReportIssue.gone()
                    mBtnConfirmReceived.text = getString(R.string.finish)
                    mBtnConfirmReceived.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_next_arrow,
                        0
                    )
                } else {
                    mCdbOrderStatus.setType(CustomDeliveryButton.Type.PENDING)
                    mBtnConfirmReceived.text = getString(R.string.dialog_confirm_shipped_title)
                }
            }
        }
    }

    private fun setUpCardTypeViews(type: ProductCategory) {
        when (type) {
            ProductCategory.CARDS -> {
                llCondition.gone()
            }
            ProductCategory.SEALED -> {
                llNumberRarity.gone()
                llDetails.gone()
            }
            ProductCategory.TOYS -> {   //Funko
                llRarity.gone()
                llDetails.gone()

                mTvSetTitle.text = getString(R.string.text_product_license) //Set License Text
            }
        }
    }
}
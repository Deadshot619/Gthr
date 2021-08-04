package com.gthr.gthrcollect.ui.receiptdetail.purchasedetails

import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.PurchaseDetailsFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.constants.MailConstants
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.extensions.sendMail

class PurchaseDetailsFragment : BaseFragment<PurchaseDetailsViewModel, PurchaseDetailsFragmentBinding>() {

    override val mViewModel: PurchaseDetailsViewModel by viewModels()
    override fun getViewBinding() = PurchaseDetailsFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnReportIssue: CustomSecondaryButton
    private lateinit var mBtnConfirmReceived: CustomSecondaryButton


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

    override fun onBinding() {

        initViews()
        initClickListeners()
    }

    private fun initViews(){
        mViewBinding.run {
            mBtnReportIssue = btnReportIssue
            mBtnConfirmReceived = btnConfirmReceived
        }
    }

    private fun initClickListeners(){
        mBtnReportIssue.setOnClickListener {
            context?.sendMail(emailTo = MailConstants.SUPPORT_MAIL_ID, subject = MailConstants.SUPPORT_MAIL_SUBJECT)
        }

        mBtnConfirmReceived.setOnClickListener {
            mConfirmReceivedDialog.show()
        }
    }
}
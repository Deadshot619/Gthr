package com.gthr.gthrcollect.ui.askflow.afplaceyourask

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AfPlaceYourAskFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.receiptdetail.ReceiptDetailActivity
import com.gthr.gthrcollect.ui.termsandfaq.TermsAndFaqActivity
import com.gthr.gthrcollect.utils.customviews.CustomDeliveryButton
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ReceiptType
import com.gthr.gthrcollect.utils.enums.WebViewType

class AfPlaceYourAskFragment : BaseFragment<AskFlowViewModel, AfPlaceYourAskFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfPlaceYourAskFragmentBinding.inflate(layoutInflater)


    private lateinit var mBtnNext : CustomSecondaryButton
    private var isTnCCheked = false

    private lateinit var mIvTermsAndConditions: AppCompatImageView
    private lateinit var mTvTermsAndConditions: AppCompatTextView

    override fun onBinding() {
        initValue()
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        mIvTermsAndConditions.setOnClickListener {
            if (!isTnCCheked) {
                isTnCCheked = !isTnCCheked
                toggleTnC(true)
                mBtnNext.setState(CustomSecondaryButton.State.BLUE_GRADIENT)
            } else {
                isTnCCheked = !isTnCCheked
                mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                toggleTnC(false)
            }
        }

        mBtnNext.setOnClickListener {
            startActivity(
                ReceiptDetailActivity.getInstance(
                    requireContext(),
                    ReceiptType.SOLD,
                    ProductCategory.CARDS,
                    CustomDeliveryButton.Type.ASK_PLACED
                )
            )
        }

        mTvTermsAndConditions.setOnClickListener {
            startActivity(
                TermsAndFaqActivity.getInstance(
                    requireContext(),
                    WebViewType.TERMS_AND_CONDITION
                )
            )
        }
    }

    private fun toggleTnC(toggleOn: Boolean) {
        if (toggleOn) {
            mIvTermsAndConditions.setImageResource(R.drawable.ic_terms_and_conditions_blue)
        } else {
            mIvTermsAndConditions.setImageResource(R.drawable.ic_terms_and_conditions)
        }
    }


    private fun initValue() {
        mBtnNext = mViewBinding.btnNext
        mIvTermsAndConditions = mViewBinding.ivTermsAndConditions
        mTvTermsAndConditions = mViewBinding.tvTermsAndConditions
    }

}
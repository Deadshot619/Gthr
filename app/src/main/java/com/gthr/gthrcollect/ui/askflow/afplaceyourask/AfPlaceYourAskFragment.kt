package com.gthr.gthrcollect.ui.askflow.afplaceyourask

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AfPlaceYourAskFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.receiptdetail.ReceiptDetailActivity
import com.gthr.gthrcollect.ui.termsandfaq.TermsAndFaqActivity
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
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
        mBtnNext.setOnClickListener {
            startActivity(ReceiptDetailActivity.getInstance(requireContext(), ReceiptType.SOLD))
        }
        mIvTermsAndConditions.setOnClickListener {
            if (!isTnCCheked) {
                isTnCCheked = !isTnCCheked
                toggleTnC(true)
            } else {
                isTnCCheked = !isTnCCheked
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mBtnNext.setState(CustomSecondaryButton.State.DISABLE)
                }
                toggleTnC(false)
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
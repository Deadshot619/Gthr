package com.gthr.gthrcollect.ui.askflow.afplaceyourask

import android.os.Build
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AfPlaceYourAskFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.receiptdetail.ReceiptDetailActivity
import com.gthr.gthrcollect.ui.settings.addnewaddress.AddNewAddressFragmentArgs
import com.gthr.gthrcollect.ui.termsandfaq.TermsAndFaqActivity
import com.gthr.gthrcollect.utils.customviews.CustomDeliveryButton
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.enums.ReceiptType
import com.gthr.gthrcollect.utils.enums.WebViewType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class AfPlaceYourAskFragment : BaseFragment<AskFlowViewModel, AfPlaceYourAskFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels()
    override fun getViewBinding() = AfPlaceYourAskFragmentBinding.inflate(layoutInflater)


    private lateinit var mBtnNext : CustomSecondaryButton
    private var isTnCCheked = false

    private lateinit var mEtAsk: AppCompatTextView
    private lateinit var mIvTermsAndConditions: AppCompatImageView
    private lateinit var mTvTermsAndConditions: AppCompatTextView
    private lateinit var mIvBack: ImageView

    private lateinit var mGroup : Group
    private lateinit var mGroupBuy : Group

    override fun onBinding() {
        initValue()
        setUpOnClickListeners()
        setUpObserve()
    }

    private fun setUpObserve() {
        mViewModel.askPrice.observe(this){
            mEtAsk.text = "$ ${it}"
        }
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
            when ((requireActivity() as AskFlowActivity).getAskFlowType()) {
                AskFlowType.BUY -> {
                    findNavController().navigate(AfPlaceYourAskFragmentDirections.actionAfPlaceYourAskFragmentToAfBuyListDetailsFragment())
                }
                else ->{
                    startActivity(
                        ReceiptDetailActivity.getInstance(
                            requireContext(),
                            ReceiptType.SOLD,
                            ProductCategory.CARDS,
                            CustomDeliveryButton.Type.ASK_PLACED
                        )
                    )
                    activity?.finish()
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
    }

    private fun toggleTnC(toggleOn: Boolean) {
        if (toggleOn) {
            mIvTermsAndConditions.setImageResource(R.drawable.ic_terms_and_conditions_blue)
        } else {
            mIvTermsAndConditions.setImageResource(R.drawable.ic_terms_and_conditions)
        }
    }


    private fun initValue() {
        mIvBack = mViewBinding.ivBack
        mBtnNext = mViewBinding.btnNext
        mIvTermsAndConditions = mViewBinding.ivTermsAndConditions
        mTvTermsAndConditions = mViewBinding.tvTermsAndConditions
        mGroup = mViewBinding.group
        mGroupBuy = mViewBinding.groupBuy
        mEtAsk = mViewBinding.etAsk

        when ((requireActivity() as AskFlowActivity).getAskFlowType()) {
            AskFlowType.BUY -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mBtnNext.setState(CustomSecondaryButton.State.BLUE_GRADIENT)
                }
                mBtnNext.text = getString(R.string.text_add_to_buylist_ask_flow)
                mGroup.gone()
                mGroupBuy.visible()
            }
            else ->{
                mBtnNext.text = getString(R.string.text_place_your_ask)
                mGroup.visible()
                mGroupBuy.gone()
            }
        }
    }

}
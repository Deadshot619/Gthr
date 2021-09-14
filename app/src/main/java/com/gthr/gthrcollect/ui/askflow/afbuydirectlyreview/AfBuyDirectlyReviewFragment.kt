package com.gthr.gthrcollect.ui.askflow.afbuydirectlyreview

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AfBuyDirectlyReviewFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.productdetail.ProductDetailActivity
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.helper.getTier

class AfBuyDirectlyReviewFragment :
    BaseFragment<AskFlowViewModel, AfBuyDirectlyReviewFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels()
    override fun getViewBinding() = AfBuyDirectlyReviewFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnNext: CustomSecondaryButton
    private lateinit var mTvPrice: TextView
    private lateinit var mTvShippingValue: TextView
    private lateinit var mTvSalesTaxValue: TextView
    private lateinit var mTvTotalValue: TextView

    override fun onBinding() {
        setHasOptionsMenu(true)
        initViews()
        setUpClickListeners()
        setUpObservers()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnNext = btnNext
            mTvPrice = tvPrice
            mTvShippingValue = tvShippingValue
            mTvSalesTaxValue = tvSalesTaxValue
            mTvTotalValue = tvTotalValue

            mTvPrice.text = "-"
            mTvShippingValue.text = "-"
            mTvSalesTaxValue.text = "-"
            mTvTotalValue.text = "-"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.ask_flow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_info)
            startActivity(ProductDetailActivity.getInstance(this.requireContext(), null))
        return super.onOptionsItemSelected(item)
    }

    private fun setUpClickListeners() {
        mViewBinding.run {
            mBtnNext.setOnClickListener {
                getPriceValue()?.let {
                    val tier = getTier(mViewModel.productDisplayModel!!, it.toDouble()).toString()
                    mViewModel.getShippingTierInfo(tier)
                }
            }
        }
    }

    private fun setUpObservers() {
        mViewModel.shippingTierInfo.observe(viewLifecycleOwner, {
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Failed -> {
                        showToast(it.message)
                        (activity as AskFlowActivity).showProgressBar(false)
                    }
                    is State.Loading -> {
                        (activity as AskFlowActivity).showProgressBar()
                    }
                    is State.Success -> {
                        (activity as AskFlowActivity).showProgressBar(false)
                        goToNextPage()
                    }
                }
            }
        })
    }

    private fun getPriceValue(): Float? = mTvPrice.text.toString().toFloatOrNull()

    private fun goToNextPage() {
        mViewModel.setAskPrice(getPriceValue()!!)
        findNavController().navigate(AfBuyDirectlyReviewFragmentDirections.actionAfBuyDirectlyReviewFragmentToAfPlaceYourAskFragment())
    }
}
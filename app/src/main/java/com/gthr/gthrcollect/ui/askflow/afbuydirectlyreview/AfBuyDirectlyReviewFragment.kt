package com.gthr.gthrcollect.ui.askflow.afbuydirectlyreview

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AfBuyDirectlyReviewFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.productdetail.ProductDetailActivity
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.ProductType

class AfBuyDirectlyReviewFragment :
    BaseFragment<AskFlowViewModel, AfBuyDirectlyReviewFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels()
    override fun getViewBinding() = AfBuyDirectlyReviewFragmentBinding.inflate(layoutInflater)

    private lateinit var mBtnNext: CustomSecondaryButton
    override fun onBinding() {
        setHasOptionsMenu(true)
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mBtnNext = btnNext
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.ask_flow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_info)
            startActivity(
                ProductDetailActivity.getInstance(
                    this.requireContext(),
                    ProductType.FUNKO
                )
            )
        return super.onOptionsItemSelected(item)
    }

    private fun setUpClickListeners() {
        mViewBinding.run {
            mBtnNext.setOnClickListener {
                findNavController().navigate(AfBuyDirectlyReviewFragmentDirections.actionAfBuyDirectlyReviewFragmentToAfPlaceYourAskFragment())
            }
        }
    }
}
package com.gthr.gthrcollect.ui.productdetail.upforsell

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.data.repository.DynamicLinkRepository
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.data.repository.SearchRepository
import com.gthr.gthrcollect.databinding.UpForSellFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModel
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModelFactory
import com.gthr.gthrcollect.ui.productdetail.adapter.UpForSellAdapter
import com.gthr.gthrcollect.ui.productdetail.recentsell.RecentSellFragmentArgs
import com.gthr.gthrcollect.utils.customviews.CustomProductCell

class UpForSellFragment : BaseFragment<ProductDetailsViewModel,UpForSellFragmentBinding>() {

    override val mViewModel: ProductDetailsViewModel by activityViewModels {
        ProductDetailsViewModelFactory(
            ProductDetailsRepository(),
            DynamicLinkRepository(),
            SearchRepository()
        )
    }

    override fun getViewBinding() = UpForSellFragmentBinding.inflate(layoutInflater)

    private lateinit var rvUpForSell: RecyclerView
    private lateinit var mUpForSellAdapter: UpForSellAdapter

    private val args by navArgs<RecentSellFragmentArgs>()

    override fun onBinding() {
        setHasOptionsMenu(true)
        initViews()
        setUpUpForSell()
        setUpObservers()
    }

    private fun setUpUpForSell() {
        rvUpForSell.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            mUpForSellAdapter = UpForSellAdapter(CustomProductCell.State.FOR_SALE) {
                /*startActivity(
                    AskFlowActivity.getInstance(
                        requireContext(),
                        AskFlowType.BUY_DIRECTLY_FROM_SOMEONE,
                        it
                    )
                )*/
            }
            adapter = mUpForSellAdapter
        }
    }

    private fun initViews() {
        mViewBinding.let {
            rvUpForSell = it.rvUpForSell
        }
    }

    private fun setUpObservers() {
        mViewModel.upForSaleList.observe(viewLifecycleOwner, {
            it.peekContent().let {
                when (it) {
                    is State.Failed -> {
                    }
                    is State.Loading -> {
                    }
                    is State.Success -> {
                        mUpForSellAdapter.submitList(it.data)
                    }
                }
            }
        })
    }
}
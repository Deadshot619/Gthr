package com.gthr.gthrcollect.ui.productdetail.recentsell

import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.data.repository.ProductDetailsRepository
import com.gthr.gthrcollect.databinding.LayoutProductDetailCardTopBinding
import com.gthr.gthrcollect.databinding.LayoutProductDetailToyTopBinding
import com.gthr.gthrcollect.databinding.RecentSellFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModel
import com.gthr.gthrcollect.ui.productdetail.ProductDetailsViewModelFactory
import com.gthr.gthrcollect.ui.productdetail.adapter.RecentSellAdapter
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.setProfileImage
import com.gthr.gthrcollect.utils.extensions.visible
import com.gthr.gthrcollect.utils.logger.GthrLogger

class RecentSellFragment : BaseFragment<ProductDetailsViewModel, RecentSellFragmentBinding>() {


    override val mViewModel: ProductDetailsViewModel by activityViewModels{
        ProductDetailsViewModelFactory(
            ProductDetailsRepository()
        )
    }
    override fun getViewBinding() = RecentSellFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvProduct: AppCompatImageView
    private lateinit var rvRecentSell : RecyclerView

    private val args by navArgs<RecentSellFragmentArgs>()
    private lateinit var recentSaleAdapter : RecentSellAdapter

    override fun onBinding() {
        setHasOptionsMenu(true)
        initViews()
        setUpRecentSell()
        setUpObserver()
    }

    private fun setUpObserver() {
        mViewModel.mRecentSaleDomainModel.observe(viewLifecycleOwner) { it ->
            it.peekContent()?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Failed -> showProgressBar(false)
                    is State.Success -> {
                        recentSaleAdapter.submitList(it.data)
                        GthrLogger.i("dschjds", "Recent Sale : ${it.data}")
                        showProgressBar(false)
                    }
                }
            }
        }

        mViewModel.mProductImage.observe(viewLifecycleOwner) { it ->
            it.peekContent()?.let {
                when (it) {
                    is State.Success -> mIvProduct.setProfileImage(it.data)
                    is State.Failed -> {}
                    is State.Loading -> {}
                }
            }
        }
    }

    private fun initViews() {
        mViewBinding.let {
            rvRecentSell = it.rvRecentSell
            mIvProduct = it.ivProduct
        }
    }

    private fun setUpRecentSell() {
        recentSaleAdapter = RecentSellAdapter(args.type)
        rvRecentSell.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recentSaleAdapter
        }
    }




}